package com.shaw.claims.controller;

import java.util.List;

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

import com.shaw.claims.dto.NoteTemplateDTO;
import com.shaw.claims.services.NoteTemplateService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/noteTemplate/v1")
public class NoteTemplateController {

	Logger log = LogManager.getLogger(NoteTemplateController.class);
	
	@Autowired
	NoteTemplateService noteTemplateService;
	
	@PostMapping("/saveNoteTemplate")
	public ResponseEntity<Void> saveNoteTemplate(@RequestBody NoteTemplateDTO  noteTemplateDTO)
	{   log.info("Inside NoteTemplateController.saveNoteTemplate");
		noteTemplateService.saveNoteTemplate(noteTemplateDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/fetchNoteTemplateByNoteTypeAndNoteGroup")
    public ResponseEntity<List<NoteTemplateDTO>> fetchNoteTemplateByNoteTypeAndNoteGroup(@RequestParam(value = "noteTypeId")int noteTypeId, @RequestParam(value = "noteGroupId") int noteGroupId){
         log.info("Inside NoteTemplateController.fetchNoteTemplateByNoteTypeAndNoteGroup");
        return new ResponseEntity<>(noteTemplateService.fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId, noteGroupId),HttpStatus.OK);
    }
	
	@GetMapping("/fetchAllNoteTemplates")
    public ResponseEntity<List<NoteTemplateDTO>> fetchAllNoteTemplates(){
         log.info("Inside NoteTemplateController.fetchAllNoteTemplates");
        return new ResponseEntity<>(noteTemplateService.fetchAllNoteTemplates(),HttpStatus.OK);
    }
	
	
	@DeleteMapping("/deleteNoteTemplate")
    public ResponseEntity<Void> deleteNoteTemplate(@RequestParam(value = "noteTemplateId") Integer noteTemplateId){
         log.info("Inside NoteTemplateController.deleteNoteTemplate");
         noteTemplateService.deleteNoteTemplate(noteTemplateId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping("/fetchNoteTemplateBynoteTypeCode")
    public ResponseEntity<List<NoteTemplateDTO>> fetchNoteTemplateBynoteTypeCode(@RequestParam(value = "noteTypeCode")String noteTypeCode){
         log.info("Inside NoteTemplateController.fetchNoteTemplateBynoteTypeCode");
        return new ResponseEntity<>(noteTemplateService.fetchNoteTemplateBynoteTypeCode(noteTypeCode),HttpStatus.OK);
    }
}
