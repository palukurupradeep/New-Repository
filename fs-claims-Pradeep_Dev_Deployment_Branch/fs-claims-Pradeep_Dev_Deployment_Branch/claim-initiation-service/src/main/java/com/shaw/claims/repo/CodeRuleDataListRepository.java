package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shaw.claims.model.CodeRuleDataList;

@Repository
public interface CodeRuleDataListRepository extends JpaRepository<CodeRuleDataList, Integer>{

	@Query("select c from CodeRuleDataList c where c.codeRuleDataType.codeRuleDataTypeid=:codeRuleDataTypeid")
	List<CodeRuleDataList> getCodeRuleDataList(Integer codeRuleDataTypeid);
	
	@Modifying
	@Transactional
	@Query("delete from  CodeRuleDataList c where c.codeRules.codeRuleId=:codeRuleId")
	void deleteCodeRuleDataListBycodeRuleId(Integer codeRuleId);
}
