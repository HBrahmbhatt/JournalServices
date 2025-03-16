package com.hirbr.journalservices.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {

	@Id
	private ObjectId id;

	@NonNull
	@Indexed(unique = true)
	private String username;

	@NonNull
	private String password;

	private List<String> roles;

	@DBRef
	private List<Journal> journalEntries = new ArrayList<>();
}
