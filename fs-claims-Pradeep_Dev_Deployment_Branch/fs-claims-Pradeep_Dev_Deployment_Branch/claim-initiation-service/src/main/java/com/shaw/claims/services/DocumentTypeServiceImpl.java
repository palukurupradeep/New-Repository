package com.shaw.claims.services;

import com.shaw.claims.repo.DocumentTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService{
    Logger log = LogManager.getLogger(DocumentTypeServiceImpl.class);
    @Autowired
    DocumentTypeRepository documentTypeRepository;
    @Override
    public Integer getDocIdByDocumentTypeCode(String documentTypeCode) {
        return documentTypeRepository.getDocIdByDocumentTypeCode(documentTypeCode);
    }
}
