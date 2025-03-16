package com.hirbr.journalservices.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.UserRepository;
import com.hirbr.journalservices.util.JournalServicesConstants;

@Service
public class AdminService {

	@Autowired
	UserRepository userRepository;

	// Admin based function
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	//
	public boolean saveAdmin(User user) {
		try {
			user.setRoles(Arrays.asList(JournalServicesConstants.ADMIN_ROLE));
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
