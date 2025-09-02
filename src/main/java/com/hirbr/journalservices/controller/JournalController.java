package com.hirbr.journalservices.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/by-day")
	public ResponseEntity<?> getByDay(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "tz", defaultValue = "America/Los_Angeles") String tz) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			ZoneId zone = ZoneId.of(tz);
			ZonedDateTime startZdt = date.atStartOfDay(zone);
			ZonedDateTime endZdt = startZdt.plusDays(1);

			Date start = Date.from(startZdt.toInstant());
			Date end = Date.from(endZdt.toInstant());

			Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt", "_id");
			List<Journal> journals = journalService.findByCreatedAtRange(username, start, end, sort, limit);

			if (journals != null && !journals.isEmpty()) {
				return new ResponseEntity<>(journals, HttpStatus.OK);
			}
			return new ResponseEntity<>(journals, HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			log.error("Error occured in UpdateJournalById --> " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
