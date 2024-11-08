package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRules;

@Repository
public interface CodeRulesRepository extends JpaRepository<CodeRules, Integer>{
	List<CodeRules> findByClaimReasonDefinition_ClaimReasonCode(String reasonCode);
	@Query("select cr from CodeRules cr where cr.claimReasonDefinition.claimReasonId=:reasonId and cr.statusId=1")
	List<CodeRules> fetchCodeRuleByReasonId(Integer reasonId);
}
