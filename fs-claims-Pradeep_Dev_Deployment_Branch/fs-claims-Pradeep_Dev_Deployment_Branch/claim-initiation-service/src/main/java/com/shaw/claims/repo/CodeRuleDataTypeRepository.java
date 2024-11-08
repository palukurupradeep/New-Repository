package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleDataType;

@Repository
public interface CodeRuleDataTypeRepository extends JpaRepository<CodeRuleDataType, Integer> {
    @Query("SELECT crd FROM CodeRuleDataType crd WHERE crd.codeRuleDataTypeCode =:codeRuleDataTypeCode")
    CodeRuleDataType findByCodeRuleDataTypeCode(String codeRuleDataTypeCode);

	List<CodeRuleDataType> findByStatusId(int active);
}
