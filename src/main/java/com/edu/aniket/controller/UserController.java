package com.edu.aniket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.User;
import com.edu.aniket.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<UserDto>> saveUser(@Validated @RequestBody User user) {
		return userService.saveUser(user);
	}

	@GetMapping("/login")
	public ResponseEntity<ResponseStructure<UserDto>> findUserByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
		return userService.findUserByEmailAndPassword(email, password);
	}

	@GetMapping("/findById")
	public ResponseEntity<ResponseStructure<UserDto>> findUserById(@RequestParam long id) {
		return userService.findUserById(id);
	}

	@GetMapping("/findAll")
	public ResponseEntity<ResponseStructure<List<UserDto>>> findAllUsers() {
		return userService.findAllUsers();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ResponseStructure<String>> removeUserById(@RequestParam long id) {
		return userService.deleteUserById(id);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<UserDto>> updateUser(@Validated @RequestBody User user) {
		return userService.updateUser(user);
	}
}
