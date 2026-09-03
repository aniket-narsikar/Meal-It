package com.edu.aniket;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.edu.aniket.config.AdminUserSeeder;
import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.AdminCreateUserRequest;
import com.edu.aniket.dto.AuthResponse;
import com.edu.aniket.dto.LoginRequest;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.repository.UserRepository;
import com.edu.aniket.security.JwtUtil;
import com.edu.aniket.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleBasedSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdminUserSeeder adminUserSeeder;

	@Autowired
	private JwtUtil jwtUtil;

	@BeforeEach
	public void cleanDatabase() {
		userRepository.deleteAll();
	}

	// 1. Public signup without role creates CUSTOMER
	@Test
	public void testPublicSignupWithoutRoleCreatesCustomer() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Alice\",\"email\":\"alice@example.com\",\"phoneNumber\":9876543210,\"password\":\"pass123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("CUSTOMER"));

		User saved = userRepository.findByEmail("alice@example.com").orElseThrow();
		assertEquals(Role.CUSTOMER, saved.getRole());
		assertTrue(passwordEncoder.matches("pass123", saved.getPassword()));
	}

	// 2. Public signup with role=CUSTOMER creates CUSTOMER
	@Test
	public void testPublicSignupWithCustomerRoleCreatesCustomer() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Bob\",\"email\":\"bob@example.com\",\"phoneNumber\":9876543211,\"password\":\"pass123\",\"role\":\"CUSTOMER\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("CUSTOMER"));

		User saved = userRepository.findByEmail("bob@example.com").orElseThrow();
		assertEquals(Role.CUSTOMER, saved.getRole());
	}

	// 3. Public signup with role=ADMIN is rejected
	@Test
	public void testPublicSignupWithAdminRoleIsRejected() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Attacker\",\"email\":\"attacker_admin@example.com\",\"phoneNumber\":9876543212,\"password\":\"pass123\",\"role\":\"ADMIN\"}"))
				.andExpect(status().isBadRequest());

		assertTrue(userRepository.findByEmail("attacker_admin@example.com").isEmpty());
	}

	// 4. Public signup with role=MANAGER is rejected
	@Test
	public void testPublicSignupWithManagerRoleIsRejected() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Attacker\",\"email\":\"attacker_manager@example.com\",\"phoneNumber\":9876543213,\"password\":\"pass123\",\"role\":\"MANAGER\"}"))
				.andExpect(status().isBadRequest());

		assertTrue(userRepository.findByEmail("attacker_manager@example.com").isEmpty());
	}

	// 5. Public signup with role=CHEF is rejected
	@Test
	public void testPublicSignupWithChefRoleIsRejected() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Attacker\",\"email\":\"attacker_chef@example.com\",\"phoneNumber\":9876543214,\"password\":\"pass123\",\"role\":\"CHEF\"}"))
				.andExpect(status().isBadRequest());

		assertTrue(userRepository.findByEmail("attacker_chef@example.com").isEmpty());
	}

	// 6. Public signup with role=STAFF is rejected
	@Test
	public void testPublicSignupWithStaffRoleIsRejected() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Attacker\",\"email\":\"attacker_staff@example.com\",\"phoneNumber\":9876543215,\"password\":\"pass123\",\"role\":\"STAFF\"}"))
				.andExpect(status().isBadRequest());

		assertTrue(userRepository.findByEmail("attacker_staff@example.com").isEmpty());
	}

	// 7. Public signup with role=WAITER is rejected
	@Test
	public void testPublicSignupWithWaiterRoleIsRejected() throws Exception {
		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Attacker\",\"email\":\"attacker_waiter@example.com\",\"phoneNumber\":9876543216,\"password\":\"pass123\",\"role\":\"WAITER\"}"))
				.andExpect(status().isBadRequest());

		assertTrue(userRepository.findByEmail("attacker_waiter@example.com").isEmpty());
	}

	// 8. Unauthenticated user cannot access admin user creation endpoints
	@Test
	public void testUnauthenticatedCannotAccessAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/manager")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Mgr\",\"email\":\"mgr@example.com\",\"phoneNumber\":9876543217,\"password\":\"pass123\"}"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(post("/api/admin/users/chef")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Chef\",\"email\":\"chef@example.com\",\"phoneNumber\":9876543218,\"password\":\"pass123\"}"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(post("/api/admin/users/staff")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Staff\",\"email\":\"staff@example.com\",\"phoneNumber\":9876543219,\"password\":\"pass123\"}"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(post("/api/admin/users/waiter")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Waiter\",\"email\":\"waiter@example.com\",\"phoneNumber\":9876543220,\"password\":\"pass123\"}"))
				.andExpect(status().isUnauthorized());
	}

	// 9. CUSTOMER cannot access admin user creation endpoints
	@Test
	@WithMockUser(username = "customer@example.com", roles = {"CUSTOMER"})
	public void testCustomerForbiddenFromAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/manager")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Mgr\",\"email\":\"mgr@example.com\",\"phoneNumber\":9876543217,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/admin/users/chef")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Chef\",\"email\":\"chef@example.com\",\"phoneNumber\":9876543218,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/admin/users/staff")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Staff\",\"email\":\"staff@example.com\",\"phoneNumber\":9876543219,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/admin/users/waiter")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Waiter\",\"email\":\"waiter@example.com\",\"phoneNumber\":9876543220,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());
	}

	// 10. MANAGER cannot create privileged users
	@Test
	@WithMockUser(username = "mgr@example.com", roles = {"MANAGER"})
	public void testManagerForbiddenFromAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/chef")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Chef\",\"email\":\"chef@example.com\",\"phoneNumber\":9876543218,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());
	}

	// 11. CHEF cannot create privileged users
	@Test
	@WithMockUser(username = "chef@example.com", roles = {"CHEF"})
	public void testChefForbiddenFromAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/waiter")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Waiter\",\"email\":\"waiter@example.com\",\"phoneNumber\":9876543220,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());
	}

	// 12. STAFF cannot create privileged users
	@Test
	@WithMockUser(username = "staff@example.com", roles = {"STAFF"})
	public void testStaffForbiddenFromAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/waiter")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Waiter\",\"email\":\"waiter@example.com\",\"phoneNumber\":9876543220,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());
	}

	// 13. WAITER cannot create privileged users
	@Test
	@WithMockUser(username = "waiter@example.com", roles = {"WAITER"})
	public void testWaiterForbiddenFromAdminEndpoints() throws Exception {
		mockMvc.perform(post("/api/admin/users/manager")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Mgr\",\"email\":\"mgr@example.com\",\"phoneNumber\":9876543217,\"password\":\"pass123\"}"))
				.andExpect(status().isForbidden());
	}

	// 14. ADMIN can create MANAGER, CHEF, STAFF, WAITER
	@Test
	@WithMockUser(username = "admin@mealit.com", roles = {"ADMIN"})
	public void testAdminCanCreatePrivilegedEmployees() throws Exception {
		// Create Manager
		mockMvc.perform(post("/api/admin/users/manager")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Manager Mike\",\"email\":\"mike@mealit.com\",\"phoneNumber\":9876543221,\"password\":\"pass123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("MANAGER"))
				.andExpect(jsonPath("$.data.email").value("mike@mealit.com"));

		User manager = userRepository.findByEmail("mike@mealit.com").orElseThrow();
		assertEquals(Role.MANAGER, manager.getRole());
		assertTrue(passwordEncoder.matches("pass123", manager.getPassword()));

		// Create Chef
		mockMvc.perform(post("/api/admin/users/chef")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Chef Charlie\",\"email\":\"charlie@mealit.com\",\"phoneNumber\":9876543222,\"password\":\"pass123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("CHEF"));

		User chef = userRepository.findByEmail("charlie@mealit.com").orElseThrow();
		assertEquals(Role.CHEF, chef.getRole());

		// Create Staff
		mockMvc.perform(post("/api/admin/users/staff")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Staff Sam\",\"email\":\"sam@mealit.com\",\"phoneNumber\":9876543223,\"password\":\"pass123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("STAFF"));

		User staff = userRepository.findByEmail("sam@mealit.com").orElseThrow();
		assertEquals(Role.STAFF, staff.getRole());

		// Create Waiter
		mockMvc.perform(post("/api/admin/users/waiter")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Waiter Wendy\",\"email\":\"wendy@mealit.com\",\"phoneNumber\":9876543224,\"password\":\"pass123\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.role").value("WAITER"));

		User waiter = userRepository.findByEmail("wendy@mealit.com").orElseThrow();
		assertEquals(Role.WAITER, waiter.getRole());
	}

	// 15. User update cannot elevate role (e.g. CUSTOMER cannot become ADMIN via update)
	@Test
	public void testUpdateUserCannotElevateRole() {
		User user = new User();
		user.setName("Normal User");
		user.setEmail("normal@example.com");
		user.setPhoneNumber(9876543225L);
		user.setPassword(passwordEncoder.encode("pass123"));
		user.setRole(Role.CUSTOMER);
		User saved = userRepository.save(user);

		// Attempt to update with role=ADMIN
		User updatePayload = new User();
		updatePayload.setId(saved.getId());
		updatePayload.setName("Updated User");
		updatePayload.setEmail("normal@example.com");
		updatePayload.setPhoneNumber(9876543225L);
		updatePayload.setPassword("pass123");
		updatePayload.setRole(Role.ADMIN);

		ResponseEntity<ResponseStructure<UserDto>> response = userService.updateUser(updatePayload);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Role.CUSTOMER, response.getBody().getData().getRole());

		User fromDb = userRepository.findById(saved.getId()).orElseThrow();
		assertEquals(Role.CUSTOMER, fromDb.getRole(), "User role must remain CUSTOMER after update");
	}

	// 16. Admin seeder creates ADMIN account idempotently with hashed password
	@Test
	public void testAdminSeederIsIdempotent() {
		userRepository.deleteAll();

		// First run: should create admin
		adminUserSeeder.seedAdminUser();
		Optional<User> adminOpt = userRepository.findByEmail("admin@mealit.com");
		assertTrue(adminOpt.isPresent());
		User admin = adminOpt.get();
		assertEquals(Role.ADMIN, admin.getRole());
		assertTrue(passwordEncoder.matches("Admin@1234", admin.getPassword()));

		long countAfterFirst = userRepository.count();
		assertEquals(1, countAfterFirst);

		// Second run: should not duplicate or change password
		adminUserSeeder.seedAdminUser();
		long countAfterSecond = userRepository.count();
		assertEquals(1, countAfterSecond);
	}

	// 17. Seeded Admin and Customer login via existing /auth/login
	@Test
	public void testAdminAndCustomerLoginAndJwtRoleGeneration() throws Exception {
		// Seed Admin
		adminUserSeeder.seedAdminUser();

		// Seed Customer
		User customer = new User();
		customer.setName("Customer Carla");
		customer.setEmail("carla@example.com");
		customer.setPhoneNumber(9876543226L);
		customer.setPassword(passwordEncoder.encode("carlaPass"));
		customer.setRole(Role.CUSTOMER);
		userRepository.save(customer);

		// 17a. Admin login
		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"admin@mealit.com\",\"password\":\"Admin@1234\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.user.role").value("ADMIN"))
				.andExpect(jsonPath("$.data.token").isNotEmpty());

		// 17b. Customer login
		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"carla@example.com\",\"password\":\"carlaPass\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.user.role").value("CUSTOMER"))
				.andExpect(jsonPath("$.data.token").isNotEmpty());
	}
}
