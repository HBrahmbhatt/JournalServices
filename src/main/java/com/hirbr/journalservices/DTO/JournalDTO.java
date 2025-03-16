package com.hirbr.journalservices.DTO;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class JournalDTO {
	// Validations on entities, and DTOs
	@Id
	private ObjectId id;
	@NonNull
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String authorId;
	private List<String> tags;
	private String visibility;

}
