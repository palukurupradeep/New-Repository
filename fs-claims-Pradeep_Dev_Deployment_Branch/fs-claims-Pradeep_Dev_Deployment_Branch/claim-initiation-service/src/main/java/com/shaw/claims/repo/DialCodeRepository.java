package com.shaw.claims.repo;

import com.shaw.claims.model.DialCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialCodeRepository extends JpaRepository<DialCode, Integer> {
    @Query("SELECT dc FROM DialCode dc WHERE dc.statusId = 1")
    List<DialCode> getDialCodes();
    @Query("SELECT dc.isoDialingCodeId FROM DialCode dc WHERE dc.isoCountryDialingCode =:businessPhoneDialCode")
    int findIsoDialingCodeIdByCountryDialCode(@Param("businessPhoneDialCode") String businessPhoneDialCode);
}
