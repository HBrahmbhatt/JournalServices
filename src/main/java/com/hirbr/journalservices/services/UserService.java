package com.hirbr.journalservices.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private static PasswordEncoder pwdEncode = new BCryptPasswordEncoder();

	// User based function
	public boolean saveUser(User user) {
		try {
			user.setPassword(pwdEncode.encode(user.getPassword()));
			// Also adding user role temporarily over here
			user.setRoles(Arrays.asList("USER"));
			;
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// User based function
	public User updateUser(String username, User user) {
		// Fetch the user based on the authenticated username
		User fetchedUser = userRepository.findByUsername(username);
		// Check if the user exists
		if (fetchedUser != null) {
			// Only update the fields you want to modify (e.g., username, password)
			if (user.getUsername() != null && !user.getUsername().isEmpty()) {
				fetchedUser.setUsername(user.getUsername());
			}
			if (user.getPassword() != null && !user.getPassword().isEmpty()) {
				fetchedUser.setPassword(pwdEncode.encode(user.getPassword()));
			}
			fetchedUser.setRoles(Arrays.asList("USER"));
			// Save the updated user back to the repository
			return userRepository.save(fetchedUser);
		}
		// Return null if user doesn't exist
		return null;
	}

	// User and admin based function
	public User getByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return user;
		}
		return null;
	}

	// Admin based function
	public boolean deleteUser(String username) {
		try {
			User user = userRepository.findByUsername(username);
			userRepository.deleteById(user.getId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Admin based function
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	//
	public boolean saveAdmin(User user) {
		try {
			user.setRoles(Arrays.asList("ADMIN"));
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
