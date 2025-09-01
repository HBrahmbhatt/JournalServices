package com.hirbr.journalservices.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.UserRepository;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user != null) {
//			UserDetails userdetails = org.springframework.security.core.userdetails.User.builder()
//					.username(user.getUsername()).password(user.getPassword())
//					.roles(user.getRoles().toArray(new String[0])).build();
//			return userdetails;
			return new org.springframework.security.core.userdetails.User(
	                user.getUsername(),
	                user.getPassword(),
	                user.getRoles().stream()
	                        .map(SimpleGrantedAuthority::new)
	                        .collect(Collectors.toList())
	        );
		}
		throw new UsernameNotFoundException("User with username" + username + " not found");
	}

}
