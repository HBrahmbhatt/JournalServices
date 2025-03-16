package com.hirbr.journalservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.DTO.UserDTO;
import com.hirbr.journalservices.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	/**
	 * This Controller manages user-related operations, including: 1 - Retrieving
	 * the current username, 2 - Updating the username and password for an existing
	 * account, 3 - Deleting a user account
	 **/

	@GetMapping("/get-user")
	public ResponseEntity<?> getUserDetails() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			log.info("Fetching user details for username: {}", username);

			UserDTO userDetails = userService.getByUsername(username);
			if (userDetails == null) {
				log.warn("User not found: {}", username);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
			return ResponseEntity.ok(userDetails);
		} catch (Exception e) {
			log.error("Error fetching user details", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
		}
	}

	@PutMapping("/update-user")
	public ResponseEntity<?> updateUserById(@RequestBody @Valid UserDTO userDto) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			log.info("Updating user details for username: {}", username);

			UserDTO updatedUser = userService.updateUser(username, userDto);
			if (updatedUser != null) {
				log.info("User updated successfully: {}", username);
				return ResponseEntity.ok(updatedUser);
			} else {
				log.warn("User not found for update: {}", username);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
		} catch (Exception e) {
			log.error("Error updating user details for username: {}", userDto.getUsername(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating user details");
		}
	}

	@DeleteMapping("/delete-user")
	public ResponseEntity<?> deleteUserById() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		log.info("Received request to delete user: {}", username);

		try {
			boolean isDeleted = userService.deleteUser(username);
			if (isDeleted) {
				log.info("User successfully deleted: {}", username);
				return ResponseEntity.noContent().build();
			} else {
				log.warn("Failed to delete user: {} (User not found or deletion issue)", username);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or could not be deleted.");
			}
		} catch (Exception e) {
			log.error("Unexpected error while deleting user: {}", username, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
		}
	}

}
