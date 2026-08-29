package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dto.PageResponse;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.User;
import com.edu.aniket.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<UserDto>> saveUser(@Validated @RequestBody User user) {
		return userService.saveUser(user);
	}

	@Deprecated
	@GetMapping("/login")
	public ResponseEntity<ResponseStructure<String>> deprecatedLogin() {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatus(HttpStatus.GONE.value());
		response.setMessage("GET /user/login is deprecated for security reasons. Please use POST /auth/login with a JSON body.");
		response.setData(null);
		return new ResponseEntity<>(response, HttpStatus.GONE);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<UserDto>> findUserById(@RequestParam long id) {
		return userService.findUserById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<UserDto>> getUserById(@PathVariable long id) {
		return userService.findUserById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<PageResponse<UserDto>>> findAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id,asc") String sort
	) {
		return userService.findAllUsersPaginated(page, size, sort);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> removeUserById(@RequestParam long id) {
		return userService.deleteUserById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteUserById(@PathVariable long id) {
		return userService.deleteUserById(id);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<UserDto>> updateUser(@Validated @RequestBody User user) {
		return userService.updateUser(user);
	}
}
