package com.hirbr.journalservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.services.AdminService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	AdminService adminService;

	// add an api to see all users
	@GetMapping("/all-users")
	public ResponseEntity<?> getAllUsers() {
		log.info("Inside getAllUsers method...");
		try {
			List<User> users = adminService.getAllUsers();
			log.info("Retrieved {} users from the database.", (users != null ? users.size() : 0));

			if (users != null & !users.isEmpty()) {
				log.info("Users found, returning response with status OK.");
				return new ResponseEntity<>(users, HttpStatus.OK);
			}
			log.warn("No users found, returning response with status NOT_FOUND.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("An error occurred while fetching users: {}", e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// add an api to make somebody admin
	@PostMapping("/create-admin-user")
	public ResponseEntity<?> createAdminUser(@RequestBody User user) {
		try {
			log.info("Inside createAdminUser method...");
			log.info("Received request to create admin user with username: {}", user.getUsername());
			boolean response = adminService.saveAdmin(user);
			if (response) {
				log.info("Admin user with username: {} successfully created.", user.getUsername());
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			log.warn("Failed to create admin user with username: {}. Returning INTERNAL_SERVER_ERROR.",
					user.getUsername());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("An error occurred while creating admin user with username: {}: {}", user.getUsername(),
					e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
