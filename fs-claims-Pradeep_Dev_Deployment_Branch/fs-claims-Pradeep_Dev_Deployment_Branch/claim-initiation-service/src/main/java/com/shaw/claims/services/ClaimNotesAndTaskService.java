package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimNoteDTO;
import com.shaw.claims.dto.NoteAndTaskRequestDTO;
import com.shaw.claims.dto.NoteGroupDTO;
import com.shaw.claims.dto.PendingNotesAndTaskRequestDTO;

public interface ClaimNotesAndTaskService {
    void notesAndTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO);
    void pendNotesAndTask(PendingNotesAndTaskRequestDTO pendingNotesAndTaskRequestDTOO);
	List<ClaimNoteDTO> fetchClaimNotes(String claimId);
    String fetchClaimNoteText(String claimNoteId);
    List<NoteGroupDTO> noteGroup();
    List<String> userNamesForClaimNotes(String claimId);
	void deleteNotesAndTask(String claimNoteId);
}
