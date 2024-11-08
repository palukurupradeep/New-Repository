package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleLevel;

@Repository
public interface CodeRuleLevelRepository extends JpaRepository<CodeRuleLevel, Integer> {
    @Query("SELECT crl FROM CodeRuleLevel crl WHERE crl.codeRuleLevelCode =:codeRuleLevelCode")
    CodeRuleLevel findByCodeRuleLevelCode(String codeRuleLevelCode);

	List<CodeRuleLevel> findByStatusId(int active);
}
