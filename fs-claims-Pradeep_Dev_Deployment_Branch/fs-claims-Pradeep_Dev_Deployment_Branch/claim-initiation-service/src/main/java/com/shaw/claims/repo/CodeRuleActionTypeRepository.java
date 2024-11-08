package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleActionType;

@Repository
public interface CodeRuleActionTypeRepository extends JpaRepository<CodeRuleActionType, Integer> {
    @Query("SELECT cra FROM CodeRuleActionType cra WHERE cra.codeRuleActionTypeCode =:codeRuleActionTypeCode")
    CodeRuleActionType findByCodeRuleActionTypeCode(String codeRuleActionTypeCode);

	List<CodeRuleActionType> findByStatusId(int active);
}
