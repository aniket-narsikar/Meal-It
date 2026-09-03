package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.AdminCreateUserRequest;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.Role;
import com.edu.aniket.service.UserService;

@RestController
@RequestMapping({"/api/admin/users", "/admin/users"})
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

	private final UserService userService;

	@Autowired
	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/manager")
	public ResponseEntity<ResponseStructure<UserDto>> createManager(@Validated @RequestBody AdminCreateUserRequest request) {
		return userService.createPrivilegedUser(request, Role.MANAGER);
	}

	@PostMapping("/chef")
	public ResponseEntity<ResponseStructure<UserDto>> createChef(@Validated @RequestBody AdminCreateUserRequest request) {
		return userService.createPrivilegedUser(request, Role.CHEF);
	}

	@PostMapping("/staff")
	public ResponseEntity<ResponseStructure<UserDto>> createStaff(@Validated @RequestBody AdminCreateUserRequest request) {
		return userService.createPrivilegedUser(request, Role.STAFF);
	}

	@PostMapping("/waiter")
	public ResponseEntity<ResponseStructure<UserDto>> createWaiter(@Validated @RequestBody AdminCreateUserRequest request) {
		return userService.createPrivilegedUser(request, Role.WAITER);
	}
}
