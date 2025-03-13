package com.hirbr.journalservices.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hirbr.journalservices.entity.JournalEntity;
import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.repository.JournalRepository;
import com.hirbr.journalservices.repository.UserRepository;

@Service
public class JournalService {

	private static Logger logger = LoggerFactory.getLogger(JournalService.class);

	@Autowired
	private JournalRepository journalRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void saveEntry(JournalEntity journalEntity, String username) {
		try {
			User user = userRepository.findByUsername(username);
			journalEntity.setCreatedAt(LocalDateTime.now());
			JournalEntity savedJournal = journalRepository.save(journalEntity);
			user.getJournalEntries().add(savedJournal);
			userRepository.save(user);
		} catch (Exception e) {
			logger.info("error:" + e);
			throw new RuntimeException(e);
		}
	}

	public List<JournalEntity> getAllJournals(String username) {
		User user = userRepository.findByUsername(username);
//		List<ObjectId> journalIds = user.getJournalEntries().stream().map(JournalEntity::getId)
//				.collect(Collectors.toList());
//		return journalRepository.findAllById(journalIds);
		return user.getJournalEntries();
	}

	public JournalEntity getJournalById(String username, ObjectId id) {
		User user = userRepository.findByUsername(username);
		List<JournalEntity> journalList = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
				.collect(Collectors.toList());
		if (journalList != null && !journalList.isEmpty()) {
			Optional<JournalEntity> journal = journalRepository.findById(id);
			if (journal.isPresent()) {
				return journal.get();
			}
		}
		return null;
	}

	@Transactional
	public void deleteJournalById(ObjectId id, String username) {
		User user = userRepository.findByUsername(username);
		user.getJournalEntries().removeIf(x -> x.getId().equals(id));
		userRepository.save(user);
		journalRepository.deleteById(id);
	}

	public JournalEntity updateJournalById(String username, ObjectId id, JournalEntity journalEntity) {
		// Find by username, then check if that user have this id journal and then
		// update
		User user = userRepository.findByUsername(username);
		List<JournalEntity> journal = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
				.collect(Collectors.toList());
		if (!journal.isEmpty()) {
			JournalEntity fetchedJournal = journalRepository.findById(id).orElse(null);
			if (fetchedJournal != null) {
				fetchedJournal.setTitle(journalEntity.getTitle() != null && !journalEntity.getTitle().isEmpty()
						? journalEntity.getTitle()
						: fetchedJournal.getTitle());
				fetchedJournal.setContent(journalEntity.getContent() != null && !fetchedJournal.getTitle().isEmpty()
						? journalEntity.getContent()
						: fetchedJournal.getContent());
				fetchedJournal.setUpdatedAt(LocalDateTime.now());
				journalRepository.save(fetchedJournal);
				return fetchedJournal;
			}
		}
		return null;
	}
}
