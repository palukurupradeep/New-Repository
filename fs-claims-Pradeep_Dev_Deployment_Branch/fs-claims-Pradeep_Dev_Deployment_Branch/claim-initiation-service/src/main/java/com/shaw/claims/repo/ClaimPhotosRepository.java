package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimPhotos;

@Repository
public interface ClaimPhotosRepository extends JpaRepository<ClaimPhotos, Integer> {

	ClaimPhotos findByphotoId(Integer photoId);
	@Query("select c from ClaimPhotos c where c.statusId=1")
	List<ClaimPhotos> findActiveClaimPhotos();
}
