package com.shaw.claims.controller;

import java.util.List;

import com.shaw.claims.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.services.ClaimNotesAndTaskService;
import com.shaw.claims.services.NoteTemplateService;
import com.shaw.claims.services.NoteTypeService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/notesandtasks/v1")
public class NotesAndTasksController {

	Logger log = LogManager.getLogger(NotesAndTasksController.class);
	
	@Autowired
	NoteTypeService noteTypeService;
	@Autowired
	NoteTemplateService noteTemplateService;
	@Autowired
	private ClaimNotesAndTaskService claimNotesAndTaskService;
	
	@GetMapping("/fetchNoteType")
    public ResponseEntity<List<NoteTypeDTO>> fetchNoteType(){
         log.info("Inside NotesAndTasksController.fetchNoteType");
        return new ResponseEntity<>(noteTypeService.fetchNoteType(),HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<Void> notesAndTask (@RequestBody NoteAndTaskRequestDTO noteAndTaskRequestDTO){
		log.info("Inside DashBoardController.notesAndTask");
		claimNotesAndTaskService.notesAndTask(noteAndTaskRequestDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/pend")
	public ResponseEntity<Void> pendNotesAndTask (@RequestBody PendingNotesAndTaskRequestDTO pendingNotesAndTaskRequestDTO){
		log.info("Inside DashBoardController.pendNotesAndTask");
		claimNotesAndTaskService.pendNotesAndTask(pendingNotesAndTaskRequestDTO
		);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping("/fetchTraceType")
    public ResponseEntity<List<TraceTypeDTO>> fetchTraceType(){
         log.info("Inside NotesAndTasksController.fetchTraceType");
        return new ResponseEntity<>(noteTypeService.fetchTraceType(),HttpStatus.OK);
    }
	
	@GetMapping("/fetchClaimNotes")
    public ResponseEntity<List<ClaimNoteDTO>> fetchClaimNotes(@RequestParam(value = "claimId") String claimId){
         log.info("Inside NotesAndTasksController.fetchClaimNotes");
        return new ResponseEntity<>(claimNotesAndTaskService.fetchClaimNotes(claimId),HttpStatus.OK);
    }

	@GetMapping("/fetchClaimNoteText")
	public ResponseEntity<String> fetchClaimNoteText(@RequestParam(value = "claimNoteId") String claimNoteId){
		log.info("Inside NotesAndTasksController.fetchClaimNoteText");
		return new ResponseEntity<>(claimNotesAndTaskService.fetchClaimNoteText(claimNoteId),HttpStatus.OK);
	}

	@GetMapping("/noteGroup")
	public ResponseEntity<List<NoteGroupDTO>> noteGroup(){
		log.info("Inside NotesAndTasksController.noteGroup");
		return new ResponseEntity<>(claimNotesAndTaskService.noteGroup(),HttpStatus.OK);
	}
	@GetMapping("/userNamesForClaimNotes")
	public ResponseEntity<List<String>> userNamesForClaimNotes(@RequestParam(value = "claimId") String claimId){
		log.info("Inside NotesAndTasksController.userNamesForClaimNotes");
		return new ResponseEntity<>(claimNotesAndTaskService.userNamesForClaimNotes(claimId),HttpStatus.OK);
	}
	
	 @DeleteMapping("/deleteNotesAndTask")
	    public void deleteNotesAndTask(@RequestParam(value = "claimNoteId") String claimNoteId){
	        log.info("Inside NotesAndTasksController.deleteNotesAndTask");
	        claimNotesAndTaskService.deleteNotesAndTask(claimNoteId);
	    }
}
