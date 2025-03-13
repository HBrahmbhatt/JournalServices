package com.hirbr.journalservices.controller;

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
@RequestMapping("/public")
public class PublicController {

	@Autowired
	UserService userService;

	@GetMapping("/health-check")
	public String HealthCheck() {
		return "Ok";
	}

	@PostMapping("/add-user")
	public ResponseEntity<?> addEntry(@RequestBody User user) {
		try {
			if (userService.saveUser(user)) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
