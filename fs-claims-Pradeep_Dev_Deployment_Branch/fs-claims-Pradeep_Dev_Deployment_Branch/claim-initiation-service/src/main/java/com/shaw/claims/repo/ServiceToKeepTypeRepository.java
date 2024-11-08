package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ServiceToKeepType;

@Repository
public interface ServiceToKeepTypeRepository extends JpaRepository<ServiceToKeepType,Integer> {

	List<ServiceToKeepType> findAll();

	
}
