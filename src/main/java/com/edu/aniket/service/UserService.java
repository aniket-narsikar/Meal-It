package com.edu.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.edu.aniket.config.ResponseStructure;
import com.edu.aniket.dao.UserDao;
import com.edu.aniket.dto.UserDto;
import com.edu.aniket.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<UserDto>> saveUser(User user) {
		User savedUser = userDao.saveUser(user);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(savedUser));
		responseStructure.setMessage("User Saved Successfully");
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<UserDto>> findUserByEmailAndPassword(String email, String password) {
		User user = userDao.findUserByEmailAndPassword(email, password);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(user));
		responseStructure.setMessage("User Found Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
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

	public ResponseEntity<ResponseStructure<String>> deleteUserById(long id) {
		userDao.deleteUserById(id);
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setData("User with ID " + id + " has been deleted");
		responseStructure.setMessage("User Deleted Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<UserDto>> updateUser(User user) {
		User updatedUser = userDao.updateUser(user);
		ResponseStructure<UserDto> responseStructure = new ResponseStructure<>();
		responseStructure.setData(mapUserEntityToUserDto(updatedUser));
		responseStructure.setMessage("User Updated Successfully");
		responseStructure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private UserDto mapUserEntityToUserDto(User user) {
		return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
	}
}
