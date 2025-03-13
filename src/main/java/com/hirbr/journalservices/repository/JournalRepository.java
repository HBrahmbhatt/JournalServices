package com.hirbr.journalservices.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hirbr.journalservices.entity.JournalEntity;

@Repository
public interface JournalRepository extends MongoRepository<JournalEntity, ObjectId>{

}
