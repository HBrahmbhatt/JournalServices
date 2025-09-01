package com.hirbr.journalservices.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Document(collection = "journals")
@Data
@NoArgsConstructor
public class Journal {

	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId id;

	@NonNull
	private String title;
	private String content;

	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedBy
	private LocalDateTime updatedAt;

	private String authorId;
	private List<String> tags;
	private List<String> mood;
}
