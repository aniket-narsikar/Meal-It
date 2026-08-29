package com.edu.aniket.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edu.aniket.entity.User;
import com.edu.aniket.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		String roleName = user.getRole() != null ? user.getRole().name() : "CUSTOMER";
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword() != null ? user.getPassword() : "",
				Collections.singletonList(authority)
		);
	}
}
