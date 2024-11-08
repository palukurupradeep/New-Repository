package com.shaw.claims.repo;

import com.shaw.claims.model.DocumentType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType,Integer> {
    @Query("SELECT d FROM DocumentType d WHERE d.documentTypeCode =:documentTypeCode")
    DocumentType findByDocumentTypeCode(@Param("documentTypeCode") String documentTypeCode);
    @Query("SELECT d.documentTypeId FROM DocumentType d WHERE d.documentTypeCode =:documentTypeCode")
    Integer getDocIdByDocumentTypeCode(@Param("documentTypeCode") String documentTypeCode);
	List<DocumentType> findByStatusId(int active);
}
