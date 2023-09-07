package com.edu.aniket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.User;

@RestController
public class DashBoardController {
	
	private UserDao dao;
	
	@GetMapping("/display")
public String display()
{
return "<h1>Welcome to Meal It App</h1>";	
}
//	@GetMapping("/login")
//	public User checkUserDao()
//	{
//		User user=new User();
//		user.setEmail("sachin@java.com");
//		user.setName("Sachin");
//		user.setPassword("Sachin#123");
//		dao.saveUser(user);
//		return dao.findUserByEmailAndPassword("sachin@java.com", "Sachin#123");
//	}
}
