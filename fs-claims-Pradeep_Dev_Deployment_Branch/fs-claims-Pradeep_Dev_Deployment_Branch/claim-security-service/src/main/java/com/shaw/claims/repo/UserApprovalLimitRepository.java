package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.UserApprovalLimit;

@Repository
public interface UserApprovalLimitRepository extends JpaRepository<UserApprovalLimit,Integer> {

	List<UserApprovalLimit> findByUsers_UserId(int userId);
}
