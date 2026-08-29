package com.edu.aniket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIdNotFoundException;
import com.edu.aniket.exception.UserWithEmailAndPasswordNotFound;
import com.edu.aniket.repository.UserRepository;

@Repository
public class UserDao {

	private final UserRepository userRepository;

	@Autowired
	public UserDao(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
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

	public Page<User> findAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
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
