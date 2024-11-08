package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodeRuleReturnType;

@Repository
public interface CodeRuleReturnTypeRepository extends JpaRepository<CodeRuleReturnType, Integer> {

	CodeRuleReturnType findByCodeRuleReturnTypeCode(String codeRuleReturnTypeCode);

	List<CodeRuleReturnType> findByStatusId(int active);

}
