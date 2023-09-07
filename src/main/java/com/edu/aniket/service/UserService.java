package com.edu.aniket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponcseStructure;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.User;



@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public ResponseEntity<ResponcseStructure<UserDto>> saveUser (User user)
	{
		ResponcseStructure<UserDto> responcseStructure = new ResponcseStructure<>();
		responcseStructure.setData(mapUserEntituToUserDto( userDao.saveUser(user)));
		responcseStructure.setMessage("Saved");
		responcseStructure.setStatus(HttpStatus.CREATED.value());
		ResponseEntity<ResponcseStructure<UserDto>> responseEntity = new ResponseEntity<>(responcseStructure,
				HttpStatus.CREATED);
		return responseEntity;
	}

	private UserDto mapUserEntituToUserDto(User user) 
	{
				return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(),user.getRole());
	}
	
	public ResponseEntity<ResponcseStructure<UserDto>> findUserByEmailAndPassword(String email, String password)
	{
		ResponcseStructure<UserDto> responcseStructure = new  ResponcseStructure<>();
		responcseStructure.setData(mapUserEntituToUserDto(userDao.findUserByEmailAndPassword(email, password)));
		responcseStructure.setMessage("Found");
		responcseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<> (responcseStructure, HttpStatus.OK);
 	}
}
