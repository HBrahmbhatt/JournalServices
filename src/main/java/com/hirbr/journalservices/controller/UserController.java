package com.hirbr.journalservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@PutMapping("/update-user")
	public ResponseEntity<?> UpdateUserById(@RequestBody User user) {
		try {
			// Get the authenticated user's username
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName(); // get the logged-in username
			// Pass the username and updated user details to the service
			User updatedUser = userService.updateUser(username, user);
			// Check if user update is successful
			if (updatedUser != null) {
				return new ResponseEntity<>(HttpStatus.OK); // OK status is better when the user is updated
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Make this role based function, as in if an admin only then allow
	@GetMapping("/find-all-users")
	public ResponseEntity<?> findAllUsers() {
		try {
			List<User> users = userService.getAllUsers();
			if (users != null && !users.isEmpty()) {
				return new ResponseEntity<>(users, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("find-by-username/{username}")
	public ResponseEntity<?> findUserById(@PathVariable String username) {
		try {
			User user = userService.getByUsername(username);
			if (user != null) {
				return new ResponseEntity<>(user, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete-user")
	public ResponseEntity<?> DeleteJournalById() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			if (userService.deleteUser(username)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
