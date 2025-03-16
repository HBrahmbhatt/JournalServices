package com.hirbr.journalservices.mapper;

import java.util.Collections;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hirbr.journalservices.DTO.UserDTO;
import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.util.JournalServicesConstants;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserMapper {

	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserDTO userToUserDTO(@Valid User user) {
		log.info("Inside user-To-UserDTO conversion method ...");
		log.info("Converting user {} to UserDTO", user.getUsername());
		return new UserDTO(user.getUsername(), user.getRoles());
	}

	// For updating a user
	public static User userDtoToUser(@Valid UserDTO userDto, @Valid User user) {
		log.info("Inside userDto-To-User conversion method for updating existing user ...");
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
			user.setRoles(userDto.getRoles());
		}
		return user;
	}

	// For saving a new user
	public User userDtoToUser(@Valid UserDTO userDto) {
		log.info("Inside userDto-To-User conversion method for creating a new user ...");
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
			userDto.setRoles(Collections.singletonList(JournalServicesConstants.USER_ROLE));
		}
		user.setRoles(userDto.getRoles());
		return user;
	}

}
