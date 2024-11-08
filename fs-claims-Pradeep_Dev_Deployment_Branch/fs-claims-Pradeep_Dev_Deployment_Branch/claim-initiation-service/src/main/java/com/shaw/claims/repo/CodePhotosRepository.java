package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CodePhotos;

@Repository
public interface CodePhotosRepository extends JpaRepository<CodePhotos, Integer>{

	@Query("select c from CodePhotos c where c.claimPhotos.photoId=:photoId")
	List<CodePhotos> findCodePhotosWithphotoId(Integer photoId);
	
	@Query("select distinct(c.claimPhotos.photoId) from CodePhotos c where c.claimReasonDefinition.claimReasonId=:reasonId")
	List<Integer> findCodePhotosByReasonId(Integer reasonId);
}
