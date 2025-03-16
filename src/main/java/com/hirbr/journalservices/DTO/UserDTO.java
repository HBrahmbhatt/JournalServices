package com.hirbr.journalservices.DTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

	@NotNull
	@NotBlank(message = "Username cannot be blank")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	private List<String> roles;
	
	// Constructor
	public UserDTO(String username, List<String> roles) {
		this.username = username;
		this.roles = roles;
	}

}
