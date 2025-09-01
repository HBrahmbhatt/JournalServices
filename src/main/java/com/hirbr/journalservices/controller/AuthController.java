package com.hirbr.journalservices.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.services.UserService;
import com.hirbr.journalservices.util.ApiError;
import com.hirbr.journalservices.util.CookieUtil;
import com.hirbr.journalservices.util.JwtUtil;
import com.hirbr.journalservices.util.LoginRequest;
import com.hirbr.journalservices.util.LoginResponse;
import com.hirbr.journalservices.util.Tokens;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = userService.getByUsername(request.getUsername());
			String access = jwtUtil.generateAccess(user.getUsername(), user.getRoles(), Tokens.ACCESS_TTL_SECONDS);
			String refresh = jwtUtil.generateRefresh(user.getUsername(), Tokens.REFRESH_TTL_SECONDS);

			return ResponseEntity.status(HttpStatus.OK)
					.header(HttpHeaders.SET_COOKIE,
							CookieUtil.refreshCookie(refresh, Tokens.REFRESH_TTL_SECONDS).toString())
					.body(new LoginResponse(access, user.getUsername(), user.getRoles()));
		} catch (Exception ex) {
			log.error("Login failed", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError("SERVER_ERROR", "Unexpected error."));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody LoginRequest request) {
		try {
			if (userService.getByUsername(request.getUsername()) != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(new ApiError("USER_EXISTS", "User already exists."));
			}

			User user = new User();
			user.setUsername(request.getUsername());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setRoles(List.of("USER"));
			userService.saveUser(user);

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			log.info("Generating tokens...");
			String access = jwtUtil.generateAccess(user.getUsername(), user.getRoles(), Tokens.ACCESS_TTL_SECONDS);
			String refresh = jwtUtil.generateRefresh(user.getUsername(), Tokens.REFRESH_TTL_SECONDS);
			log.info("Tokens generated...");
			return ResponseEntity.status(HttpStatus.CREATED)
					.header(HttpHeaders.SET_COOKIE,
							CookieUtil.refreshCookie(refresh, Tokens.REFRESH_TTL_SECONDS).toString())
					.body(new LoginResponse(access, user.getUsername(), user.getRoles()));

		} catch (Exception ex) {
			log.error("Registration failed", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError("SERVER_ERROR", "Unexpected error."));
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshCookie) {
		try {
			if (refreshCookie == null || refreshCookie.isBlank()) {
				log.warn("[refresh] missing refresh cookie");
				return ResponseEntity.status(401).body(Map.of("message", "No refresh token"));
			}
			if (!jwtUtil.isRefreshValid(refreshCookie)) {
				log.warn("[refresh] invalid/expired refresh token");
				return ResponseEntity.status(401).body(Map.of("message", "Invalid refresh token"));
			}

			final String username = jwtUtil.extractUsername(refreshCookie);
			final User user = userService.getByUsername(username);
			if (user == null) {
				log.warn("[refresh] user not found for token sub={}", username);
				return ResponseEntity.status(401).body(Map.of("message", "User not found"));
			}

			final String newAccess = jwtUtil.generateAccess(username, user.getRoles(), Tokens.ACCESS_TTL_SECONDS);
			final String newRefresh = jwtUtil.generateRefresh(username, Tokens.REFRESH_TTL_SECONDS);

			log.info("[refresh] rotated tokens for user={}", username);
			
			return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE,
							CookieUtil.refreshCookie(newRefresh, Tokens.REFRESH_TTL_SECONDS).toString())
					.body(Map.of("token", newAccess));

		} catch (Exception ex) {
			log.error("[refresh] unexpected error", ex);
			return ResponseEntity.status(500).body(Map.of("message", "Server error"));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest req) {
		String ua = req.getHeader("User-Agent");
		String ip = req.getRemoteAddr();
		try {
			log.info("Logout requested from ip={}, ua={}", ip, ua);

			// Clear the refresh cookie
			var clear = CookieUtil.clearRefreshCookie().toString();
			log.debug("Setting Set-Cookie to clear refresh cookie: {}", clear);

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, clear).build();

		} catch (Exception ex) {
			log.error("Logout failed", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError("SERVER_ERROR", "Unexpected error."));
		}
	}

}
