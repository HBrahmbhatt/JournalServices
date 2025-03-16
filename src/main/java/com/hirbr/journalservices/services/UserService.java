package com.hirbr.journalservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hirbr.journalservices.DTO.UserDTO;
import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.mapper.UserMapper;
import com.hirbr.journalservices.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserMapper userMapper;
	
	public UserDTO getByUsername(String username) {
		log.info("Searching for user with username: {}", username);
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		return userMapper.userToUserDTO(user);
	}

	
	public UserDTO updateUser(String username, UserDTO userDto) {
		log.info("Fetching user for update: {}", username);
		User fetchedUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		log.info("Updating fields for user: {}", username);
		fetchedUser = UserMapper.userDtoToUser(userDto, fetchedUser);
		User savedUser = userRepository.save(fetchedUser);
		log.info("User successfully updated and saved: {}", username);
		return userMapper.userToUserDTO(savedUser);
	}

	
	public UserDTO saveUser(@Valid UserDTO userDto) {
		log.info("Attempting to save user with username: {}", userDto.getUsername());
		User user = userMapper.userDtoToUser(userDto);
		User savedUser = userRepository.save(user);
		log.info("User successfully saved with username: {}", savedUser.getUsername());
		return userMapper.userToUserDTO(savedUser);
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
