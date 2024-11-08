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
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClaimSubmittalService extends ConstructClaimService {

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
    @Lazy
    private ClaimBatchHeaderServiceImpl claimBatchHeaderService;
    @Autowired
    @Lazy
    private ClaimDetailsServiceImpl claimDetailsService;
    @Autowired
    private DialCodeRepository dialCodeRepository;
    @Autowired
    private NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private AddressTypeRepository addressTypeRepository;

    public Claim getClaim(ClaimBuilderDTO claimBuilderDTO){
        Claim claim = constructClaim(claimBuilderDTO);
        claim.setEndUserInformation(getEndUserInformation(claim, claimBuilderDTO.getEndUserInformationDTO()));
        setAddWatchList(claimBuilderDTO, claim);
        if(claimBuilderDTO.getClaimNoteDTO() != null) {
            NoteTemplate noteTemplate = getNoteTemplate(claim.getCreatedByUserId(), claimBuilderDTO.getClaimNoteDTO().getNoteTypeCode(), claimBuilderDTO.getSubmitterId());
            claim.setClaimNotes(List.of(getClaimNote(claim, claimBuilderDTO, noteTemplate)));
            claim.setAuditHistory(List.of(getAuditHistory(claim, noteTemplate)));
        }
        if(claimBuilderDTO.getClaimReasonCode().equalsIgnoreCase("P01")) {
            claim.setClaimRoutes(inStoreRouting(claimBuilderDTO, claim));
        }
        return claim;
    }

    private NoteTemplate getNoteTemplate(int createdByUserId, String noteTemplateName, String submitterId) {
        if ((createdByUserId == getUserId(submitterId)) || (noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL))) {
            return noteTemplateRepository.findByNoteTemplateName(CommonConstant.CLAIM_SUBMITTAL);
        }
        return noteTemplateRepository.findByNoteTemplateName(noteTemplateName);
    }


    private Integer getUserId(String submitterId) {
        return tdvServiceUtil.getUserID(submitterId);
    }

    private EndUserInformation getEndUserInformation(Claim claim, ClaimBuilderEndUserInformationDTO endUserInformationDTO) {
        EndUserInformation endUserInformation = null;
        if (endUserInformationDTO == null && claim.getCustomerNumber().isEmpty()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Required customer number with out sending end user information");
        } else {
            if (endUserInformationDTO != null) {
                endUserInformation = new EndUserInformation();
                BeanUtils.copyProperties(endUserInformationDTO, endUserInformation);
                endUserInformation.setCountryId(countryRepository.findCountryIdByIsoCountryCode(endUserInformationDTO.getCountryCode()));
                endUserInformation.setStateId(stateRepository.findStateIdByIsoStateCodeAndCountryId(endUserInformationDTO.getStateCode(),endUserInformation.getCountryId()));
                endUserInformation.setBusinessPhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(endUserInformationDTO.getBusinessPhoneDialCode()));
                endUserInformation.setCellPhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(endUserInformationDTO.getCellPhoneDialCode()));
                endUserInformation.setHomePhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(endUserInformationDTO.getHomePhoneDialCode()));
                endUserInformation.setCreatedByUserId(claim.getCreatedByUserId());
                endUserInformation.setModifiedByUserId(claim.getModifiedByUserId());
                endUserInformation.setClaim(claim);
            }
        }
        return endUserInformation;
    }

    private List<ClaimRoute> inStoreRouting(ClaimBuilderDTO claimBuilderDTO, Claim claim) {
        ClaimRoutedDTO claimRoutedDTO  = new ClaimRoutedDTO();
        int userGroupId = tdvServiceUtil.getUserGroupsMapping(Integer.parseInt(claimBuilderDTO.getTerritory())).get(0).getUserGroupId();
        claimRoutedDTO.setUserGroupCode(claimBatchHeaderService.getUserGroups(userGroupId).getUserGroupCode());
        claimRoutedDTO.setRoutedUserId(Integer.parseInt(claimBuilderDTO.getTerritory()));
        claimRoutedDTO.setUserId(claimBuilderDTO.getCreatedByUserId());
        claimRoutedDTO.setNote("InStore Claim Routing");
        return claimDetailsService.routedClaim(claimRoutedDTO,claim,true);
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
        return noteTemplate.getAuditHistoryTemplateText()
                .replace("#ORGCTYPE", claim.getClaimCategory().getClaimCategoryName())
                .replace("#INVNUM", claim.getClaimDocuments() != null ? claim.getClaimDocuments().stream()
                        .map(ClaimDocument::getDocumentNumber)
                        .findFirst()
                        .orElse("Not Document In This Claim ") : " ");
    }

    private ClaimNote getClaimNote(Claim claim, ClaimBuilderDTO claimBuilderDTO, NoteTemplate noteTemplate) {
        ClaimNote claimNote = new ClaimNote();
        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode(claimBuilderDTO.getClaimNoteDTO().getNoteTypeCode());
        claimNote.setClaimNoteText(getClaimNoteText(claim, claimBuilderDTO));
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

    private String getClaimNoteText(Claim claim, ClaimBuilderDTO claimBuilderDTO) {
        StringBuilder noteText = new StringBuilder();
        ClaimAddress claimAddress = claim.getClaimAddresses().stream()
                .filter(address -> address.getAddressTypeId() == addressTypeRepository.findAddressTypeIdByAddressTypeCode(CommonConstant.DEALER_ADDRESS_TYPE_CODE))
                .findAny().orElse(null);
        if(claimBuilderDTO.getSubmitterId() != null)
            noteText.append("SUBMITTER ID : ").append(claimBuilderDTO.getSubmitterId()).append(" \\n ");
        if(claimBuilderDTO.getSubmitterName() != null)
            noteText.append("SUBMITTER NAME : ").append(claimBuilderDTO.getSubmitterName()).append(" \\n ");
        if(claimBuilderDTO.getSubmitterEmail() != null)
            noteText.append("SUBMITTER EMAIL : ").append(claimBuilderDTO.getSubmitterEmail()).append(" \\n ");
        if(claimAddress != null) {
            noteText.append("DEALER CONTACT NAME : ").append(claimAddress.getFullName()).append(" \\n ");
            noteText.append("DEALER CONTACT PHONE : ").append(claimAddress.getPhoneNumber()).append(" \\n ");
            noteText.append("DEALER EMAIL ").append(claimAddress.getEmailAddress()).append(" \\n ");
        }
        if(claim.getClaimCategory().getClaimCategoryCode() != null)
            noteText.append("ORIGINAL CATEGORY : ").append(claim.getClaimCategory().getClaimCategoryName()).append(" \\n ");
        if(claimBuilderDTO.getSampleAvailable() != null)
            noteText.append("SAMPLES AVAILABLE : ").append(claimBuilderDTO.getSampleAvailable() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getFlooringInstalled() != null) {
            if (claimBuilderDTO.getFlooringInstalled()) {
                noteText.append("FLOORING INSTALLED : ").append("YES").append(" \\n ");
                noteText.append("INSTALL DATE : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getInstallDate()).append(" \\n ");
                noteText.append("COMPLAINT DATE : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getComplaintDate()).append(" \\n ");
                noteText.append("INSTALL TYPE : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getInstallType()).append(" \\n ");
                noteText.append("PERCENT INSTALLED : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getPercentInstalled()).append(" \\n ");
                noteText.append("PROFESSIONALLY CLEANED : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getProfessionallyCleaned() ? "YES" : "NO").append(" \\n ");
                noteText.append("CLEANING METHOD : ").append(claimBuilderDTO.getInstalledFlooringDataDTO().getCleaningMethod()).append(" \\n ");
            } else {
                noteText.append("FLOORING INSTALLED : ").append("NO").append(" \\n ");
            }
        }
        if(claimBuilderDTO.getClaimReason() != null)
            noteText.append("CLAIM REASON : ").append(claimBuilderDTO.getClaimReason()).append(" \\n ");
        if(claimBuilderDTO.getClaimResolution() != null)
            noteText.append("CLAIM RESOLUTION : ").append(claimBuilderDTO.getClaimResolution()).append(" \\n ");
        if(claimBuilderDTO.getAttachments() != null)
            noteText.append("ATTACHMENTS INCLUDED : ").append(claimBuilderDTO.getAttachments() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getFlooringInstalled() != null)
            noteText.append(claimBuilderDTO.getFlooringInstalled() ? " DAMAGE VISIBLE AT RECEIPT " : " DAMAGE NOT VISIBLE ").append(" \\n ");
        if(claimBuilderDTO.getTearsDamage() != null)
            noteText.append("TEARS DAMAGE : ").append(claimBuilderDTO.getTearsDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getPoleDamage() != null)
            noteText.append("POLE DAMAGE : ").append(claimBuilderDTO.getPoleDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getWrinkleDamage() != null)
            noteText.append("WRINKLE DAMAGE : ").append(claimBuilderDTO.getWrinkleDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getCreaseDamage() != null)
            noteText.append("CREASE DAMAGE : ").append(claimBuilderDTO.getCreaseDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getDirtyDamage() != null)
            noteText.append("DIRT OR GREASE DAMAGE : ").append(claimBuilderDTO.getDirtyDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getTelescopedDamage() != null)
            noteText.append("TELESCOPED DAMAGE : ").append(claimBuilderDTO.getTelescopedDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getWetDamage() != null)
            noteText.append("WET DAMAGE : ").append(claimBuilderDTO.getWetDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getOtherDamage() != null)
            noteText.append("OTHER DAMAGE : ").append(claimBuilderDTO.getOtherDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getTrailerDamage() != null)
            noteText.append("TRAILER DAMAGE : ").append(claimBuilderDTO.getTrailerDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getNailsDamage() != null)
            noteText.append("'TRUCK NAILS DAMAGE : ").append(claimBuilderDTO.getNailsDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getRibsDamage() != null)
            noteText.append("'TRUCK RIBS DAMAGE : ").append(claimBuilderDTO.getRibsDamage() ? "YES" : "NO").append(" \\n ");
        if(claimBuilderDTO.getWallDamage() != null)
            noteText.append("'TRUCK WALL DAMAGE : ").append(claimBuilderDTO.getWallDamage() ? "YES" : "NO").append(" \\n ");
        return noteText.toString();
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
}
