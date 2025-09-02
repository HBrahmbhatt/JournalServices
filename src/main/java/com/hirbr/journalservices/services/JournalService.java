package com.hirbr.journalservices.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hirbr.journalservices.entity.Journal;
import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.JournalRepository;
import com.hirbr.journalservices.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JournalService {

	@Autowired
	private JournalRepository journalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<Journal> getAllJournals(String username) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			return user.getJournalEntries();
		} catch (UsernameNotFoundException e) {
			log.error("Exception occured --> " + e);
			throw new RuntimeException(e);
		}
	}

	public Journal getJournalById(String username, ObjectId id) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			List<Journal> journalList = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
					.collect(Collectors.toList());
			if (journalList != null && !journalList.isEmpty()) {
				Optional<Journal> journal = journalRepository.findById(id);
				if (journal.isPresent()) {
					return journal.get();
				}
			}
			return null;
		} catch (UsernameNotFoundException e) {
			log.error("Exception occured in getJournalById --> " + e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public Journal saveEntry(Journal journal, String username) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			journal.setCreatedAt(LocalDateTime.now());
			journal.setAuthorId(username);
			Journal savedJournal = journalRepository.save(journal);
			log.info(savedJournal.getId().toString());
			user.getJournalEntries().add(savedJournal);
			userRepository.save(user);
			return savedJournal;
		} catch (Exception e) {
			log.error("Exception occured in saveEntry--> " + e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public Journal updateJournalById(String username, ObjectId id, Journal updatedJournal) {
		// Find by username, check if that user have this id journal and then update
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			List<Journal> journal = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
					.collect(Collectors.toList());
			if (!journal.isEmpty()) {
				Journal fetchedJournal = journalRepository.findById(id).orElse(null);
				if (fetchedJournal != null) {
					fetchedJournal.setTitle(updatedJournal.getTitle());
					fetchedJournal.setContent(updatedJournal.getContent());
					fetchedJournal.setMood(updatedJournal.getMood());
					fetchedJournal.setUpdatedAt(LocalDateTime.now());
					fetchedJournal.setCreatedAt(fetchedJournal.getCreatedAt());
					fetchedJournal = journalRepository.save(fetchedJournal);
					return fetchedJournal;
				}
			}
			return null;
		} catch (Exception e) {
			log.error("Exception occured in updateJournalById --> " + e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void deleteJournalById(ObjectId id, String username) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			user.getJournalEntries().removeIf(x -> x.getId().equals(id));
			userRepository.save(user);
			journalRepository.deleteById(id);
		} catch (Exception e) {
			log.error("Exception occured in deleteJournalById --> " + e);
			throw new RuntimeException(e);
		}
	}

	public List<Journal> findByCreatedAtRange(String username, Date start, Date end, Sort sort, Integer limit) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			if (user == null) {
				throw new UsernameNotFoundException(username);
			}
			log.info("by-day authorId='{}' start(UTC)={} end(UTC)={}", username, start.toInstant(), end.toInstant());

			Criteria c = new Criteria().andOperator(Criteria.where("authorId").is(user.getUsername()),
					Criteria.where("createdAt").gte(start).lt(end));
			Query q = new Query(c).with(sort);
			if (limit != null && limit > 0)
				q.limit(limit);
			return mongoTemplate.find(q, Journal.class);
		} catch (Exception e) {
			log.error("Exception occured in findByCreatedAtRange --> " + e);
			throw new RuntimeException(e);
		}
	}
}
