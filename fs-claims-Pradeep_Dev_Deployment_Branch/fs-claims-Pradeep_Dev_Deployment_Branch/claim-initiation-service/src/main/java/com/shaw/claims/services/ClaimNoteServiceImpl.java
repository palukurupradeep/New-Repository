package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.constant.ErrorCodes;
import com.shaw.claims.controller.ClaimNoteController;
import com.shaw.claims.dto.ClaimNoteRequestDTO;
import com.shaw.claims.dto.ResponseDTO;
import com.shaw.claims.exception.CustomValidationException;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.ResponseUtil;
import com.shaw.claims.util.TDVServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClaimNoteServiceImpl {

    Logger log = LogManager.getLogger(ClaimNoteController.class);

    @Autowired
    private ClaimNoteRepository claimNoteRepository;
    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private TDVServiceUtil tdvServiceUtil;
    @Autowired
    private NoteTypeRepository noteTypeRepository;
    @Autowired
    private NoteGroupRepository noteGroupRepository;
    @Autowired
    private NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private ClaimRepository claimRepository;

   public ResponseDTO claimNote(ClaimNoteRequestDTO claimNoteRequestDTO) {
        log.info("ClaimServiceImpl.claimNote");
        ClaimNote claimNote = new ClaimNote();
        setClaimNote(claimNoteRequestDTO, claimNote);
        claimNoteRepository.save(claimNote);
        return responseUtil.getResponse(null);
    }

    private void setClaimNote(ClaimNoteRequestDTO claimNoteRequestDTO, ClaimNote claimNote) {
        BeanUtils.copyProperties(claimNoteRequestDTO,claimNote);
        int userId = tdvServiceUtil.getUserID(claimNoteRequestDTO.getLoggedInUser());
        claimNote.setNoteType(Optional.ofNullable(noteTypeRepository.fetchNoteTypeByNoteTypeCode(claimNoteRequestDTO.getNoteTypeCode())).orElseGet(()-> {
            throw new CustomValidationException(ErrorCodes.NO_RECORD_FOUND,"Note type not found");
        }));
        claimNote.setNoteGroup(noteGroupRepository.findByNoteGroupCode(claimNote.getNoteType().getNoteGroup().getNoteGroupCode()));
        claimNote.setNoteTemplate(Optional.ofNullable(noteTemplateRepository.findByNoteTemplateName(claimNoteRequestDTO.getNoteTemplateName()))
                .orElse(noteTemplateRepository.findByNoteTemplateName("UNKN")));
        claimNote.setClaim(
                Optional.ofNullable(claimRepository.findClaimByClaimNumber(claimNoteRequestDTO.getClaimNumber()))
                        .orElseThrow(() -> new CustomValidationException(ErrorCodes.NO_RECORD_FOUND, "Claim not found for this claim number")));

        claimNote.setLookup(claimNote.getNoteGroup().getLookup());
        claimNote.setStatusId(CommonConstant.ACTIVE);
        claimNote.setCreatedByUserId(userId);
        claimNote.setModifiedByUserId(userId);
    }
}
