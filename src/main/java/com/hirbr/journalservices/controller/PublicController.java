package com.hirbr.journalservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.DTO.UserDTO;
import com.hirbr.journalservices.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

	@Autowired
	UserService userService;

	@GetMapping("/health-check")
	public String HealthCheck() {
		return "Ok";
	}

	@PostMapping("/create-user")
	public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDto) {
		log.info("Received request to create user with username: {}", userDto.getUsername());
		try {
			UserDTO savedUser = userService.saveUser(userDto);
			log.info("User successfully created with username: {}", savedUser.getUsername());
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error("Unexpected error while creating user: {}", userDto.getUsername(), e);
			return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
