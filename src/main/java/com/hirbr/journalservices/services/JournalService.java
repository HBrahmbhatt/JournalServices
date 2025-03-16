package com.hirbr.journalservices.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hirbr.journalservices.DTO.JournalDTO;
import com.hirbr.journalservices.entity.Journal;
import com.hirbr.journalservices.entity.User;
import com.hirbr.journalservices.mapper.JournalMapper;
import com.hirbr.journalservices.mapper.UserMapper;
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
	JournalMapper journalMapper;

	@Autowired
	UserMapper userMapper;

	public List<JournalDTO> getAllJournals(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		return user.getJournalEntries().stream().map(journalMapper::convertToJournalDto).toList();
	}

	public JournalDTO getJournalById(String username, ObjectId id) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		List<Journal> journalList = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
				.collect(Collectors.toList());
		if (journalList != null && !journalList.isEmpty()) {
			Optional<Journal> journal = journalRepository.findById(id);
			if (journal.isPresent()) {
				return journalMapper.convertToJournalDto(journal.get());
			}
		}
		return null;
	}

	@Transactional
	public JournalDTO saveEntry(JournalDTO journalDto, String username) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
			Journal journal = journalMapper.convertToJournalEntity(journalDto);
			Journal savedJournal = journalRepository.save(journal);
			user.getJournalEntries().add(savedJournal);
			userRepository.save(user);
			return journalMapper.convertToJournalDto(savedJournal);
		} catch (Exception e) {
			log.info("error:" + e);
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public JournalDTO updateJournalById(String username, ObjectId id, JournalDTO journalDto) {
		// Find by username, check if that user have this id journal and then update
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		List<Journal> journal = user.getJournalEntries().stream().filter(x -> x.getId().equals(id))
				.collect(Collectors.toList());
		if (!journal.isEmpty()) {
			Journal fetchedJournal = journalRepository.findById(id).orElse(null);
			if (fetchedJournal != null) {
				fetchedJournal = journalMapper.convertToJournalEntity(journalDto, fetchedJournal);
				journalRepository.save(fetchedJournal);
				return journalMapper.convertToJournalDto(fetchedJournal);
			}
		}
		return null;
	}

	@Transactional
	public void deleteJournalById(ObjectId id, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username" + username + " not found"));
		user.getJournalEntries().removeIf(x -> x.getId().equals(id));
		userRepository.save(user);
		journalRepository.deleteById(id);
	}

}
