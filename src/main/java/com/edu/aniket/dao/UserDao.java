package com.edu.aniket.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.edu.aniket.entity.User;
import com.edu.aniket.exception.UserIdNotFound;
import com.edu.aniket.exception.UserWithEmailAndPasswordNotFound;
import com.edu.aniket.repostory.UserRespostory;

@Repository
public class UserDao {
	@Autowired
	private UserRespostory userRespostory;
 public User saveUser(User user){	 
	return userRespostory.save(user);
 }
 public User findUserById(long userId) {
	 Optional<User> optional = userRespostory.findById(userId);
	 if (optional.isPresent()){
		 return optional.get();
		 
	 }
	 throw new UserIdNotFound("No user with Given Id");
 }  
 public User findUserByEmailAndPassword(String email,String password){
	Optional<User> optional = userRespostory.findUserByEmailAndPassword(email, password);
	if(optional.isPresent())
	{
		return optional.get();
	}
	throw new UserWithEmailAndPasswordNotFound("No user with given Email and password");
 }
 public void  deleteUserById(long userId) {
	 userRespostory.delete(findUserById(userId));
 }
}
