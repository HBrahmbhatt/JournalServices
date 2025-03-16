package com.hirbr.journalservices.mapper;

import org.springframework.stereotype.Component;

import com.hirbr.journalservices.DTO.JournalDTO;
import com.hirbr.journalservices.entity.Journal;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JournalMapper {
	// Journal entity to JournalDTO
	public JournalDTO convertToJournalDto(@Valid Journal journal) {
		log.info("Inside convertToJournalDto method ...");
		JournalDTO journalDto = new JournalDTO(journal.getId(), journal.getTitle(), journal.getContent(), journal.getCreatedAt(),
				journal.getUpdatedAt(), journal.getAuthorId(), journal.getTags(), journal.getVisibility());
		return journalDto;
	}

	// JournalDTO to Journal entity
	public Journal convertToJournalEntity(@Valid JournalDTO journalDto) {
		log.info("Inside convertToJournalEntity method for saving...");
		Journal journal = new Journal();
		journal.setTitle(journalDto.getTitle());
		journal.setContent(journal.getContent());
		journal.setUpdatedAt(journal.getUpdatedAt());
		journal.setTags(journalDto.getTags());
		journal.setVisibility(journalDto.getVisibility());
		return journal;
	}
	
	// JournalDTO to Journal entity
		public Journal convertToJournalEntity(@Valid JournalDTO journalDto, @Valid Journal journal) {
			log.info("Inside convertToJournalEntity method for updating...");
			// Need to decide on this logic
			journal.setTitle(journalDto.getTitle() != null && !journalDto.getTitle().isEmpty()
					? journalDto.getTitle()
					: journal.getTitle());
			journal.setContent(journalDto.getContent() != null && !journal.getTitle().isEmpty()
					? journalDto.getContent()
					: journal.getContent());
			journal.setTags(journalDto.getTags());
			journal.setVisibility(journalDto.getVisibility());
			return journal;
		}
}
