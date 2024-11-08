package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.*;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.*;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.TDVServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimInitiationService extends ConstructClaimService {

    @Autowired
    private TDVServiceUtil tdvServiceUtil;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private NoteTypeRepository noteTypeRepository;
    @Autowired
    private LookupRepository lookupRepository;
    @Autowired
    private DialCodeRepository dialCodeRepository;
    @Autowired
    private NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private ClaimRepository claimRepository;

    public Claim getClaim(ClaimBuilderDTO claimBuilderDTO){
        Claim claim = constructClaim(claimBuilderDTO);
        claim.setEndUserInformation(getEndUserInformation(claim, claimBuilderDTO.getEndUserInformationDTO()));
        setAddWatchList(claimBuilderDTO, claim);
        if(claimBuilderDTO.getClaimNoteDTO() != null) {
            NoteTemplate noteTemplate = getNoteTemplate(claim, claimBuilderDTO.getClaimNoteDTO().getNoteTypeCode().equals(CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL) ? CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL : CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE);
            claim.setClaimNotes(List.of(getClaimNote(claim, claimBuilderDTO.getClaimNoteDTO().getNoteTypeCode(), claimBuilderDTO.getClaimNoteDTO().getClaimNoteText(), noteTemplate)));
            claim.setAuditHistory(List.of(getAuditHistory(claim, noteTemplate)));
        }
        return claim;
    }

    private AuditHistory getAuditHistory(Claim claim, NoteTemplate noteTemplate) {
        AuditHistory auditHistory = new AuditHistory();
        auditHistory.setClaim(claim);
        auditHistory.setNoteTemplate(noteTemplate);
        auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
        auditHistory.setCreatedByUserId(claim.getCreatedByUserId());
        auditHistory.setModifiedByUserId(claim.getModifiedByUserId());
        auditHistory.setAuditHistoryText(getAuditText(claim, noteTemplate));
        return auditHistory;
    }

    private String getAuditText(Claim claim, NoteTemplate noteTemplate) {
        switch (noteTemplate.getNoteTemplateName()){
            case CommonConstant.INITIATION_NOTES:
                return getClaimInitiateAuditText(claim, noteTemplate);
            default:
                return "Template not in this Claim";
        }
    }

    private static String getClaimInitiateAuditText(Claim claim, NoteTemplate noteTemplate) {
        return noteTemplate.getAuditHistoryTemplateText()
                .replace("#CAT", claim.getClaimCategory().getClaimCategoryName())
                .replace("#RC", claim.getClaimReasonDefinition().getClaimReasonCode());
    }

    private ClaimNote getClaimNote(Claim claim, String noteTypeCode, String userNotes, NoteTemplate noteTemplate) {
        ClaimNote claimNote = new ClaimNote();
        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode(noteTypeCode);
        claimNote.setClaimNoteText(getClaimNoteText(userNotes, noteTemplate));
        claimNote.setNoteType(noteTemplate.getNoteType());
        claimNote.setNoteGroup(noteType.getNoteGroup());
        claimNote.setNoteTemplate(noteTemplate);
        claimNote.setClaim(claim);
        claimNote.setLookup(lookupRepository.findByLookupCode("P"));
        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimNote.setCreatedByUserId(claim.getCreatedByUserId());
        claimNote.setModifiedByUserId(claim.getModifiedByUserId());
        return claimNote;
    }

    private String getClaimNoteText(String userNotes,NoteTemplate noteTemplate) {
        switch (noteTemplate.getNoteTemplateName()){
            case CommonConstant.INITIATION_NOTES:
                return userNotes;
            default:
                return "Template not in this Claim";
        }
    }

    public NoteTemplate getNoteTemplate(Claim claim, String noteTemplateName) {
        NoteTemplate noteTemplate = new NoteTemplate();
        if ((claim.getCreatedByUserId().equals(defaultAssignment("Shawonline"))
                && noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE))
                || noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL)) {
            noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.CLAIM_SUBMITTAL);
        } else if(noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE)){
            noteTemplate =  noteTemplateRepository.findByNoteTemplateName(CommonConstant.INITIATION_NOTES);
        }
        return noteTemplate;
    }

    private int defaultAssignment(String user) {
        int userId;
        userId = tdvServiceUtil.getUserID(user);
        return userId;
    }

    private void setAddWatchList(ClaimBuilderDTO claimBuilderDTO, Claim claim) {
        List<ClaimWatchlist> clmWatchlist = new ArrayList<>();
        if(claimBuilderDTO.getAddToWatchList()) {
            ClaimWatchlist claimWatchlist = new ClaimWatchlist();
            claimWatchlist.setClaim(claim);
            claimWatchlist.setStatusId(statusRepository.findStatusIdByStatusCode("Active"));
            claimWatchlist.setCreatedByUserId(claimBuilderDTO.getCreatedByUserId());
            claimWatchlist.setModifiedByUserId(claimBuilderDTO.getModifiedByUserId());
            clmWatchlist.add(claimWatchlist);
            claim.setClaimWatchlist(clmWatchlist);
        }
    }

    private EndUserInformation getEndUserInformation(Claim claim, ClaimBuilderEndUserInformationDTO endUserInformationDTO) {
        EndUserInformation endUserInformation = null;
        if (endUserInformationDTO == null && claim.getCustomerNumber().isEmpty()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Required customer number with out sending end user information");
        } else {
            if (endUserInformationDTO != null) {
                endUserInformation = new EndUserInformation();
                BeanUtils.copyProperties(endUserInformationDTO, endUserInformation);
                endUserInformation.setCreatedByUserId(claim.getCreatedByUserId());
                endUserInformation.setModifiedByUserId(claim.getModifiedByUserId());
                endUserInformation.setClaim(claim);
            }
        }
        return endUserInformation;
    }

    public Claim getClaimInitiation(String claimNumber) {
        return claimRepository.findClaimByClaimNumber(claimNumber);
    }
}
