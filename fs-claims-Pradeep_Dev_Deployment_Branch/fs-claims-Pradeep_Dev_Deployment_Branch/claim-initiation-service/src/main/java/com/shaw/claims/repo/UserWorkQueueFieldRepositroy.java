package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shaw.claims.model.UserWorkQueueField;

@Repository
public interface UserWorkQueueFieldRepositroy extends JpaRepository<UserWorkQueueField, Integer> {

	
	@Modifying
	@Transactional
	@Query(value = "delete from UserWorkQueueField u WHERE u.createdByUserId=:createdByUserId")
	void deleteByUserWorkQueueFields(Integer createdByUserId);
	@Query("Select u from UserWorkQueueField u where u.createdByUserId=:createdByUserId and u.statusId=1")
	List<UserWorkQueueField>findUserWorkQueueFieldsByUserId(Integer createdByUserId);
}
