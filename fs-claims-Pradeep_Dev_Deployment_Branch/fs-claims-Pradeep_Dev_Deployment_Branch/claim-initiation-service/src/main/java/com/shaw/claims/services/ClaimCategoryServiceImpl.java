package com.shaw.claims.services;

import com.shaw.claims.model.ClaimCategory;
import com.shaw.claims.repo.ClaimCategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimCategoryServiceImpl implements ClaimCategoryService{
    Logger log = LogManager.getLogger(ClaimCategoryServiceImpl.class);
    @Autowired
    ClaimCategoryRepository repo;
    @Override
    public List<ClaimCategory> getClaimCategories() {
        log.info("ClaimCategoryServiceImpl.getClaimCategories");
        return repo.getClaimCategories();
    }
}
