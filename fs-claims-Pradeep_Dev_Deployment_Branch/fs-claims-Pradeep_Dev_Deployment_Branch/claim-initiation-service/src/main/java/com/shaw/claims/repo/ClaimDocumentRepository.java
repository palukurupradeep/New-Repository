package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimDocument;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument,Integer> {
   // @Query("SELECT cd FROM ClaimDocument cd WHERE cd.documentNumber =:documentNumber AND cd.documentType.documentTypeId =:documentTypeId AND cd.claim.claimId !=:claimId")
   // List<ClaimDocument> getPriorClaimForInvoice(String documentNumber, String documentTypeId, String claimId);
    
    @Query("SELECT cd FROM ClaimDocument cd JOIN Claim c ON cd.claim.claimId = c.claimId AND cd.documentNumber =:documentNumber AND"
    		+ " cd.documentType.documentTypeId =:documentTypeId AND cd.claim.claimId !=:claimId AND c.customerNumber=:customerNumber")
    List<ClaimDocument> getPriorClaimForInvoice(String documentNumber, String documentTypeId, String claimId,String customerNumber);
    @Modifying
    @Transactional
    @Query("UPDATE ClaimDocument cd SET cd.statusId = :statusId, cd.modifiedByUserId = :loggedInUserId, cd.modifiedDateTime = CURRENT_TIMESTAMP WHERE cd.claimDocumentId = :claimDocumentId")
    void updateDeleteDocumentStatus(@Param("claimDocumentId") int claimDocumentId, @Param("statusId") int statusId, @Param("loggedInUserId") int loggedInUserId);
    @Query("SELECT cd.claimDocumentId FROM ClaimDocument cd WHERE cd.documentNumber =:documentNumber AND cd.claim.claimId = :claimId AND cd.isAssociate = true")
    int findClaimDocumentIdByDocumentNumberAndClaimIdAdIsAssociated(@Param("documentNumber") String documentNumber,@Param("claimId") int claimId);
    @Query("SELECT cd FROM ClaimDocument cd WHERE cd.claim.claimId =:claimId")
    List<ClaimDocument> getDocumentsByClaimId(int claimId);
    @Query("SELECT cd FROM ClaimDocument cd WHERE cd.documentNumber =:documentNumber AND cd.claim.claimId = :claimId")
    ClaimDocument findClaimDocumentIdByDocumentNumberAndClaimId(@Param("documentNumber") String documentNumber,@Param("claimId") int claimId);
}
