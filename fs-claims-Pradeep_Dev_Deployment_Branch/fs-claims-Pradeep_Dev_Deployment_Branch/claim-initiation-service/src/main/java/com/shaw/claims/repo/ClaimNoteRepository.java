package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimNote;

@Repository
public interface ClaimNoteRepository extends JpaRepository<ClaimNote,Integer> {

	List<ClaimNote> findByClaim_ClaimId(String claimId);
	@Query("SELECT DISTINCT cn.createdByUserId FROM ClaimNote cn WHERE cn.claim.claimId = :claimId")
	List<Integer> getDistinctUsers(String claimId);
	@Query("SELECT cn.claimNoteText FROM ClaimNote cn WHERE cn.claimNoteId = :claimNoteId")
	String fetchClaimNoteText(String claimNoteId);
	
	@Query("SELECT cn FROM ClaimNote cn WHERE  cn.claim.claimId = :claimId and cn.noteType.noteTypeId=:noteTypeId")
	List<ClaimNote> getClaimNotesByclaimIdAndnoteTypeId(int claimId,int noteTypeId);
	ClaimNote findByClaimNoteId(String claimNoteId);
}
