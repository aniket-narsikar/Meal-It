package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIdNotFoundException;
import com.edu.aniket.exception.UserWithEmailAndPasswordNotFound;
import com.edu.aniket.repository.UserRepository;

@Repository
public class UserDao {

	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public User findUserById(long userId) {
		Optional<User> optional = userRepository.findById(userId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new UserIdNotFoundException("User not found with id: " + userId);
	}

	public User findUserByEmailAndPassword(String email, String password) {
		Optional<User> optional = userRepository.findUserByEmailAndPassword(email, password);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new UserWithEmailAndPasswordNotFound("No user found with given email and password");
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public void deleteUserById(long userId) {
		User user = findUserById(userId);
		userRepository.delete(user);
	}

	public User updateUser(User user) {
		findUserById(user.getId());
		return userRepository.save(user);
	}
}
