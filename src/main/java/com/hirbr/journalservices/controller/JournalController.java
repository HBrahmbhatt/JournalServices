package com.hirbr.journalservices.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hirbr.journalservices.entity.Journal;
import com.hirbr.journalservices.services.JournalService;
import com.hirbr.journalservices.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/journals")
public class JournalController {
	@Autowired
	JournalService journalService;

	@Autowired
	UserService userService;

	@PostMapping
	public ResponseEntity<?> addEntry(@RequestBody Journal journal) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName(); // get the logged-in username
			Journal savedJournal = journalService.saveEntry(journal, username);
			log.info("journal Id created: --> " + savedJournal.getId());
			return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error occured in addEntry --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<?> findAllJournals() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			List<Journal> journals = journalService.getAllJournals(username);
			if (journals != null && !journals.isEmpty()) {
				return new ResponseEntity<>(journals, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("Error occured in findAllJournals --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findJournalById(@PathVariable ObjectId id) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Journal journal = journalService.getJournalById(username, id);
			if (journal != null) {
				return new ResponseEntity<>(journal, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("Error occured in findJournalById --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> DeleteJournalById(@PathVariable ObjectId id) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			journalService.deleteJournalById(id, username);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error("Error occured in DeleteJournalById --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> UpdateJournalById(@PathVariable ObjectId id, @RequestBody Journal journal) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			Journal updatedJournal = journalService.updateJournalById(username, id, journal);
			if (updatedJournal != null) {
				return new ResponseEntity<>(updatedJournal, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("Error occured in UpdateJournalById --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
