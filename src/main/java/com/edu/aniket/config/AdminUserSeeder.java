package com.edu.aniket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.edu.aniket.dao.UserDao;
import com.edu.aniket.entity.Role;
import com.edu.aniket.entity.User;

@Component
public class AdminUserSeeder implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserSeeder.class);

	@Value("${admin.email:}")
	private String adminEmail;

	@Value("${admin.password:}")
	private String adminPassword;

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AdminUserSeeder(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		seedAdminUser();
	}

	public void seedAdminUser() {
		if (adminEmail == null || adminEmail.trim().isEmpty() || adminPassword == null || adminPassword.trim().isEmpty()) {
			logger.info("Admin seeding skipped: ADMIN_EMAIL or ADMIN_PASSWORD not configured.");
			return;
		}

		String normalizedEmail = adminEmail.trim();
		java.util.Optional<User> existingUserOpt = userDao.findByEmail(normalizedEmail);
		if (existingUserOpt.isPresent()) {
			User existingUser = existingUserOpt.get();
			boolean modified = false;
			if (existingUser.getRole() != Role.ADMIN) {
				existingUser.setRole(Role.ADMIN);
				modified = true;
			}
			if (existingUser.getPassword() == null || !passwordEncoder.matches(adminPassword.trim(), existingUser.getPassword())) {
				existingUser.setPassword(passwordEncoder.encode(adminPassword.trim()));
				modified = true;
			}
			if (modified) {
				userDao.updateUser(existingUser);
				logger.info("Admin user credentials/role synchronized for email: {}", normalizedEmail);
			} else {
				logger.info("Admin user already up to date with email: {}", normalizedEmail);
			}
			return;
		}

		logger.info("Seeding default ADMIN user with email: {}", normalizedEmail);
		User admin = new User();
		admin.setName("Administrator");
		admin.setEmail(normalizedEmail);
		admin.setPhoneNumber(9999999999L);
		admin.setPassword(passwordEncoder.encode(adminPassword.trim()));
		admin.setRole(Role.ADMIN);

		userDao.saveUser(admin);
		logger.info("Default ADMIN account seeded successfully.");
	}
}
