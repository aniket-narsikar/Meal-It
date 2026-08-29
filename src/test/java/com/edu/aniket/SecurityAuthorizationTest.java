package com.edu.aniket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.edu.aniket.dao.ItemDao;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.AuthResponse;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;
import com.edu.aniket.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAuthorizationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserDao userDao;

	@MockBean
	private ItemDao itemDao;

	@Test
	public void testPublicEndpointsPermitted() throws Exception {
		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
				.andExpect(status().isOk());

		mockMvc.perform(post("/user/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Test\",\"email\":\"test@example.com\",\"phoneNumber\":9876543210,\"password\":\"pass123\",\"role\":\"CUSTOMER\"}"))
				.andExpect(status().isOk());
	}

	@Test
	public void testProtectedEndpointUnauthenticatedReturns401() throws Exception {
		mockMvc.perform(get("/user/findAll"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/item/findAll"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	public void testAdminAuthorizedToSaveItem() throws Exception {
		mockMvc.perform(post("/item/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("[{\"name\":\"Burger\",\"price\":150.0,\"description\":\"Tasty\",\"type\":\"NON_VEG\",\"quantity\":1}]"))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "manager@example.com", roles = {"MANAGER"})
	public void testManagerAuthorizedToSaveItem() throws Exception {
		mockMvc.perform(post("/item/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("[{\"name\":\"Salad\",\"price\":100.0,\"description\":\"Healthy\",\"type\":\"VEG\",\"quantity\":1}]"))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "customer@example.com", roles = {"CUSTOMER"})
	public void testCustomerForbiddenFromSaveItem() throws Exception {
		mockMvc.perform(post("/item/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content("[{\"name\":\"Pizza\",\"price\":300.0,\"description\":\"Cheese\",\"type\":\"VEG\",\"quantity\":1}]"))
				.andExpect(status().isForbidden());
	}
}
