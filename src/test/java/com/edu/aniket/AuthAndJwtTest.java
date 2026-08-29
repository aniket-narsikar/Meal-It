package com.edu.aniket;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.AuthResponse;
import com.edu.aniket.dto.LoginRequest;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserWithEmailAndPasswordNotFound;
import com.edu.aniket.security.CustomUserDetailsService;
import com.edu.aniket.security.JwtUtil;
import com.edu.aniket.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthAndJwtTest {

	@Mock
	private UserDao userDao;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private CustomUserDetailsService userDetailsService;

	private PasswordEncoder passwordEncoder;

	private UserService userService;

	@BeforeEach
	public void setUp() {
		passwordEncoder = new BCryptPasswordEncoder();
		userService = new com.edu.aniket.service.UserService(userDao, passwordEncoder, jwtUtil, userDetailsService);
	}

	@Test
	public void testUserRegistrationPasswordHashing() {
		User user = new User();
		user.setName("John Doe");
		user.setEmail("john@example.com");
		user.setPassword("rawPassword123");
		user.setRole(Role.CUSTOMER);

		when(userDao.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<ResponseStructure<UserDto>> response = userService.saveUser(user);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotEquals("rawPassword123", user.getPassword());
		assertTrue(passwordEncoder.matches("rawPassword123", user.getPassword()));
		assertNotNull(response.getBody().getData());
		assertEquals("john@example.com", response.getBody().getData().getEmail());
	}

	@Test
	public void testSuccessfulLoginWithBCrypt() {
		String hashed = passwordEncoder.encode("secretPass");
		User user = new User(1L, "Alice", "alice@example.com", 9876543210L, hashed, Role.CUSTOMER, null);

		when(userDao.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername("alice@example.com").password(hashed).authorities("ROLE_CUSTOMER").build();
		when(userDetailsService.loadUserByUsername("alice@example.com")).thenReturn(userDetails);
		when(jwtUtil.generateToken(any(), anyString())).thenReturn("mocked.jwt.token");

		LoginRequest loginRequest = new LoginRequest("alice@example.com", "secretPass");
		ResponseEntity<ResponseStructure<AuthResponse>> response = userService.login(loginRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody().getData());
		assertEquals("mocked.jwt.token", response.getBody().getData().getToken());
		assertEquals("alice@example.com", response.getBody().getData().getUser().getEmail());
	}

	@Test
	public void testInvalidLoginCredentials() {
		String hashed = passwordEncoder.encode("secretPass");
		User user = new User(1L, "Alice", "alice@example.com", 9876543210L, hashed, Role.CUSTOMER, null);

		when(userDao.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

		LoginRequest loginRequest = new LoginRequest("alice@example.com", "wrongPassword");
		assertThrows(UserWithEmailAndPasswordNotFound.class, () -> userService.login(loginRequest));
	}

	@Test
	public void testLegacyPlainTextPasswordMigrationOnLogin() {
		// User with plain-text password "legacyPass"
		User user = new User(1L, "Bob", "bob@example.com", 9876543210L, "legacyPass", Role.CUSTOMER, null);

		when(userDao.findByEmail("bob@example.com")).thenReturn(Optional.of(user));
		when(userDao.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername("bob@example.com").password("legacyPass").authorities("ROLE_CUSTOMER").build();
		when(userDetailsService.loadUserByUsername("bob@example.com")).thenReturn(userDetails);
		when(jwtUtil.generateToken(any(), anyString())).thenReturn("migrated.jwt.token");

		LoginRequest loginRequest = new LoginRequest("bob@example.com", "legacyPass");
		ResponseEntity<ResponseStructure<AuthResponse>> response = userService.login(loginRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		// Verify password was updated to BCrypt hash
		assertTrue(user.getPassword().startsWith("$2a$"));
		assertTrue(passwordEncoder.matches("legacyPass", user.getPassword()));
		verify(userDao, times(1)).updateUser(user);
	}

	@Test
	public void testJwtUtilTokenGenerationAndValidation() {
		JwtUtil realJwtUtil = new JwtUtil();
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "jwtExpiration", 86400000L);

		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername("test@example.com").password("pass").authorities("ROLE_ADMIN").build();

		String token = realJwtUtil.generateToken(userDetails, "ADMIN");
		assertNotNull(token);

		String extractedEmail = realJwtUtil.extractEmail(token);
		assertEquals("test@example.com", extractedEmail);

		String extractedRole = realJwtUtil.extractRole(token);
		assertEquals("ADMIN", extractedRole);

		assertTrue(realJwtUtil.isTokenValid(token, userDetails));
		assertFalse(realJwtUtil.isTokenExpired(token));
	}

	@Test
	public void testExpiredJwtToken() {
		JwtUtil realJwtUtil = new JwtUtil();
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
		// Set expiration to negative value (-1000ms) to generate an expired token
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "jwtExpiration", -1000L);

		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername("expired@example.com").password("pass").authorities("ROLE_CUSTOMER").build();

		String token = realJwtUtil.generateToken(userDetails, "CUSTOMER");
		assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> realJwtUtil.isTokenExpired(token));
	}

	@Test
	public void testInvalidJwtTokenSignature() {
		JwtUtil realJwtUtil = new JwtUtil();
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
		org.springframework.test.util.ReflectionTestUtils.setField(realJwtUtil, "jwtExpiration", 86400000L);

		String invalidToken = "invalid.jwt.token";
		assertThrows(Exception.class, () -> realJwtUtil.extractEmail(invalidToken));
	}
}
