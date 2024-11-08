package com.shaw.claims.repo;

import com.shaw.claims.model.SellingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellingCompanyRepository extends JpaRepository<SellingCompany,Integer> {

    SellingCompany findBySellingCompanyCode(String sellingCompanyCode);
    List<SellingCompany> findByStatusId(Integer statusId);
    @Query("SELECT s.sellingCompanyName FROM SellingCompany s WHERE s.sellingCompanyCode =:sellingCompanyCode")
    String findSellingCompnayNameBySellingCompanyCode(String sellingCompanyCode);
    @Query("SELECT s FROM SellingCompany s WHERE s.sellingCompanyCode =:sellingCompanyCode")
    Optional<SellingCompany> findSellingCompnayBySellingCompanyCode(String sellingCompanyCode);

}
