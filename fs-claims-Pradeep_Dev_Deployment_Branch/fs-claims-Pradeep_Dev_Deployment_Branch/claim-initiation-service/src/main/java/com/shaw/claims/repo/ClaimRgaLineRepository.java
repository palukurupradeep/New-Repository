package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimRgaLine;

import java.util.List;

@Repository
public interface ClaimRgaLineRepository  extends JpaRepository<ClaimRgaLine, Integer>{

    @Query("SELECT crl FROM ClaimRgaLine crl WHERE crl.claimRgaHeader.claimRgaHeaderId =:claimRgaHeaderId AND crl.rgaStatus.rgaStatusId != 5")
    List<ClaimRgaLine> findByClaimRgaHeaderId(int claimRgaHeaderId);
    
    @Query("SELECT DISTINCT crh.rgaNumber FROM ClaimRgaLine crl inner join ClaimRgaHeader crh on crl.claimLineDetail.claimLineId =:claimLineId"
    		+ " AND crl.rgaStatus.rgaStatusId in (1,2) and crl.claimRgaHeader.claimRgaHeaderId=crh.claimRgaHeaderId")
    List<String> findPendingRgaNumbers(int claimLineId);
}
