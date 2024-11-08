package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleCategory;
@Repository
public interface CodeRuleCategoryRepository extends JpaRepository<CodeRuleCategory, Integer>{
    @Query("SELECT crc FROM CodeRuleCategory crc WHERE crc.codeRuleCategoryCode =:codeRuleCategoryCode")
    CodeRuleCategory findByCodeRuleCategoryCode(String codeRuleCategoryCode);

	List<CodeRuleCategory> findByStatusId(int active);
}
