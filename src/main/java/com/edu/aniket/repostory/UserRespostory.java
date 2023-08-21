package com.edu.aniket.repostory;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import com.edu.aniket.entity.User;
//
//public interface UserRespostory extends JpaRepository<User, Long>{
//	@Query("SELECT u User WHERE u.email=?1 AND u.password=?2")
//	//SELECT u User WHERE u.email=?1 AND u.password=?2
//Optional<User>	getUserBYEmailAndPassword(String email,String password);
//
//Optional<User> findUserByEmailAndPassword(String email, String password);
//}
//package com.edu.aniket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.aniket.entity.User;

public interface UserRespostory extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2")
    Optional<User> getUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByEmailAndPassword(String email, String password);
}
