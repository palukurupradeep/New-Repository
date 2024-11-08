package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleImpactArea;

@Repository
public interface CodeRuleImpactAreaRepository extends JpaRepository<CodeRuleImpactArea, Integer> {
    @Query("SELECT cri FROM CodeRuleImpactArea cri WHERE cri.codeRuleImpactAreaCode =:codeRuleImpactAreaCode")
    CodeRuleImpactArea findByCodeRuleImpactAreaCode(String codeRuleImpactAreaCode);

	List<CodeRuleImpactArea> findByStatusId(int active);
}
