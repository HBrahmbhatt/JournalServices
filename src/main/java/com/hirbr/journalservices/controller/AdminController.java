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
import com.hirbr.journalservices.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UserService userservice;

	// add an api to see all users
	@GetMapping("/all-users")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<User> users = userservice.getAllUsers();
			if (users != null & !users.isEmpty()) {
				return new ResponseEntity<>(users, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// add an api to make somebody admin
	@PostMapping("/create-admin-user")
	public ResponseEntity<?> createAdminUser(@RequestBody User user) {
		boolean response = userservice.saveAdmin(user);
		if (response) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// add an api to remove access

}
