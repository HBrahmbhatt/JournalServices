package com.hirbr.journalservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User getByUsername(String username) {
		try {
			log.info("Searching for user with username: {}", username);
			User user = userRepository.findByUsername(username).orElse(null);
			return user;
		} catch (Exception e) {
			log.error("Error in getByUsername user: {}", username, e);
			return null;
		}
	}

	public User updateUser(String username, User user) {
		try {
			log.info("Fetching user for update: {}", username);
			User fetchedUser = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			log.info("Updating fields for user: {}", username);
			fetchedUser.setUsername(user.getUsername() != null && !user.getUsername().isBlank() ? user.getUsername()
					: fetchedUser.getUsername());
			fetchedUser.setPassword(user.getPassword() != null && !user.getPassword().isBlank() ? user.getPassword()
					: fetchedUser.getPassword());
			fetchedUser.setRoles(
					user.getRoles() != null && !user.getRoles().isEmpty() ? user.getRoles() : fetchedUser.getRoles());
			User savedUser = userRepository.save(fetchedUser);
			log.info("User successfully updated and saved: {}", username);
			return savedUser;
		} catch (UsernameNotFoundException e) {
			log.error("Error in updateUser user: {}", username, e);
			return null;
		}
	}

	public User saveUser(@Valid User user) {
		try {
			log.info("Attempting to save user with username: {}", user.getUsername());
			User savedUser = userRepository.save(user);
			log.info("User successfully saved with username: {}", savedUser.getUsername());
			return savedUser;
		} catch (Exception e) {
			log.error("Error in saveUser user: {}", user.getUsername(), e);
			return null;
		}
	}

	public boolean deleteUser(String username) {
		log.info("Attempting to delete user with username: {}", username);

		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));

			if (user == null) {
				log.warn("User not found: {}", username);
				return false;
			}

			userRepository.deleteById(user.getId());
			log.info("User successfully deleted: {}", username);
			return true;

		} catch (Exception e) {
			log.error("Error deleting user: {}", username, e);
			return false;
		}
	}

}
