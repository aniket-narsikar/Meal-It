package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.AdminCreateUserRequest;
import com.edu.aniket.dto.AuthResponse;
import com.edu.aniket.dto.LoginRequest;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIdNotFoundException;
import com.edu.aniket.exception.UserWithEmailAndPasswordNotFound;
import com.edu.aniket.security.CustomUserDetailsService;
import com.edu.aniket.security.JwtUtil;

@Service
public class UserService {

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Autowired
	public UserService(
			UserDao userDao,
			PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil,
			CustomUserDetailsService userDetailsService
	) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	public ResponseEntity<ResponseStructure<UserDto>> saveUser(User user) {
		if (user.getRole() != null && user.getRole() != Role.CUSTOMER) {
			throw new IllegalArgumentException("Public registration cannot specify privileged roles: " + user.getRole());
		}
		user.setRole(Role.CUSTOMER);
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		User savedUser = userDao.saveUser(user);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(savedUser));
		responseStructure.setMessage("User Saved Successfully");
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<AuthResponse>> login(LoginRequest loginRequest) {
		User user = userDao.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UserWithEmailAndPasswordNotFound("Invalid email or password"));

		String rawPassword = loginRequest.getPassword();
		String storedPassword = user.getPassword();

		boolean matches = false;
		if (storedPassword != null) {
			if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
				matches = passwordEncoder.matches(rawPassword, storedPassword);
			} else {
				// Legacy plain-text password check with automatic migration
				if (storedPassword.equals(rawPassword)) {
					matches = true;
					user.setPassword(passwordEncoder.encode(rawPassword));
					userDao.updateUser(user);
				}
			}
		}

		if (!matches) {
			throw new UserWithEmailAndPasswordNotFound("Invalid email or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		String roleName = user.getRole() != null ? user.getRole().name() : "CUSTOMER";
		String token = jwtUtil.generateToken(userDetails, roleName);

		UserDto userDto = mapUserEntityToUserDto(user);
		AuthResponse authResponse = new AuthResponse(token, userDto);

		ResponseStructure<AuthResponse> responseStructure = new ResponseStructure<>();
		responseStructure.setData(authResponse);
		responseStructure.setMessage("Login successful");
		responseStructure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Deprecated
	public ResponseEntity<ResponseStructure<UserDto>> findUserByEmailAndPassword(String email, String password) {
		LoginRequest request = new LoginRequest(email, password);
		ResponseEntity<ResponseStructure<AuthResponse>> response = login(request);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(response.getBody().getData().getUser());
		responseStructure.setMessage(response.getBody().getMessage());
		responseStructure.setStatus(response.getBody().getStatus());
		return new ResponseEntity<>(responseStructure, HttpStatus.valueOf(response.getBody().getStatus()));
	}

	public ResponseEntity<ResponseStructure<UserDto>> findUserById(long id) {
		User user = userDao.findUserById(id);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(user));
		responseStructure.setMessage("User Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<UserDto>>> findAllUsers() {
		List<User> users = userDao.findAllUsers();
		List<UserDto> dtos = new ArrayList<>();
		for (User u : users) {
			dtos.add(mapUserEntityToUserDto(u));
		}
		ResponseStructure<List<UserDto>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(dtos);
		responseStructure.setMessage("All Users Retrieved");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PageResponse<UserDto>>> findAllUsersPaginated(int page, int size, String sort) {
		if (page < 0) page = 0;
		if (size <= 0) size = 10;
		if (size > 100) size = 100;

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
				? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<User> userPage = userDao.findAllUsers(pageable);
		Page<UserDto> userDtoPage = userPage.map(this::mapUserEntityToUserDto);

		PageResponse<UserDto> pageResponse = PageResponse.fromPage(userDtoPage);

		ResponseStructure<PageResponse<UserDto>> responseStructure = new ResponseStructure<>();
		responseStructure.setData(pageResponse);
		responseStructure.setMessage("Users fetched successfully");
		responseStructure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> deleteUserById(long id) {
		userDao.deleteUserById(id);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("User with ID " + id + " has been deleted");
		responseStructure.setMessage("User Deleted Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<UserDto>> updateUser(User user) {
		User existingUser = userDao.findUserById(user.getId());
		user.setRole(existingUser.getRole());
		if (user.getPassword() != null && !user.getPassword().isEmpty()
				&& !user.getPassword().equals(existingUser.getPassword())) {
			if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$") && !user.getPassword().startsWith("$2y$")) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
		} else {
			user.setPassword(existingUser.getPassword());
		}
		User updatedUser = userDao.updateUser(user);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(updatedUser));
		responseStructure.setMessage("User Updated Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<UserDto>> createPrivilegedUser(AdminCreateUserRequest request, Role assignedRole) {
		if (userDao.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
		}
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(assignedRole);

		User savedUser = userDao.saveUser(user);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(savedUser));
		responseStructure.setMessage("User Created Successfully with Role: " + assignedRole);
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<UserDto>> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
			String email = authentication.getName();
			User user = userDao.findByEmail(email)
					.orElseThrow(() -> new UserIdNotFoundException("Current user not found"));
			ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
			responseStructure.setData(mapUserEntityToUserDto(user));
			responseStructure.setMessage("Current user details retrieved");
			responseStructure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<>(responseStructure, HttpStatus.OK);
		}
		throw new UserIdNotFoundException("No authenticated user session found");
	}

	public UserDto mapUserEntityToUserDto(User user) {
		return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
	}
}
