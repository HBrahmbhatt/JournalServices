package com.hirbr.journalservices.entity;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NonNull;

@Document(collection = "journals")
@Data
public class JournalEntity {

    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorId;
    private List<String> tags;
    private String category;
    private String visibility;

}
