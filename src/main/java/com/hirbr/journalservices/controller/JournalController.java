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

import com.hirbr.journalservices.entity.JournalEntity;
import com.hirbr.journalservices.services.JournalService;
import com.hirbr.journalservices.services.UserService;

@RestController
@RequestMapping("journaling-services")
public class JournalController {
	@Autowired
	JournalService journalService;

	@Autowired
	UserService userService;

//	@GetMapping("get-journal-of-user/")
//	public ResponseEntity<?> getAllJournalsOfUser() {
//		try {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			String username = auth.getName(); // get the logged-in username
//			List<JournalEntity> journals = userService.getJournalsOfUsers(username);
//			if (journals != null && !journals.isEmpty()) {
//				return new ResponseEntity<>(journals, HttpStatus.OK);
//			}
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@PostMapping("/add-entry")
	public ResponseEntity<?> addEntry(@RequestBody JournalEntity journalEntity) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName(); // get the logged-in username
			journalService.saveEntry(journalEntity, username);
			return new ResponseEntity<>(journalEntity, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Make it user particular...
	@GetMapping("/find-all")
	public ResponseEntity<?> findAllJournals() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			List<JournalEntity> journals = journalService.getAllJournals(username);
			if (journals != null && !journals.isEmpty()) {
				return new ResponseEntity<>(journals, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("find-by-id/{id}")
	public ResponseEntity<?> findJournalById(@PathVariable ObjectId id) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			JournalEntity journal = journalService.getJournalById(username, id);
			if (journal != null) {
				return new ResponseEntity<>(journal, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("delete-journal-by-id/{id}")
	public ResponseEntity<?> DeleteJournalById(@PathVariable ObjectId id) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			journalService.deleteJournalById(id, username);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("update-journal-by-id/{id}")
	public ResponseEntity<?> UpdateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntity journalEntity) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			JournalEntity journal = journalService.updateJournalById(username, id, journalEntity);
			if (journal != null) {
				return new ResponseEntity<>(journal, HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
