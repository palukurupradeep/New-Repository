package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleType;

@Repository
public interface CodeRuleTypeRepository extends JpaRepository<CodeRuleType, Integer>{
    @Query("SELECT crt FROM CodeRuleType crt WHERE crt.codeRuleTypeCode =:codeRuleTypeCode")
    CodeRuleType findByCodeRuleTypeCode(String codeRuleTypeCode);

	List<CodeRuleType> findByStatusId(int active);
}
