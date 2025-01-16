package com.edu.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.entity.User;
import com.edu.aniket.config.ResponcseStructure;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/save")
	public ResponseEntity<ResponcseStructure<UserDto>> saveUser (@Validated @RequestBody User user )
	{
	return 	userService.saveUser(user);
	}
	
	@GetMapping("/login")
	public ResponseEntity<ResponcseStructure<UserDto>> findUserByEmailAndPasswords(@RequestParam String email, String password)
	{
		return userService.findUserByEmailAndPassword(email, password);
	}
	
	@DeleteMapping("/delete")
	public void removeUserById(@RequestParam long id)
	{

	}
	
	@GetMapping("/findById")
	public void findUserById(@RequestParam long id)
	{
		
	}
}
