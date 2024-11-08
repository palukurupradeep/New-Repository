package com.shaw.claims.mapper;


import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.shaw.claims.model.ClaimCrm;

@Component
public class ClaimCrmRowMapper implements RowMapper<ClaimCrm> {
	
	 public static final Logger logger = LoggerFactory.getLogger(ClaimCrmRowMapper.class);
    @Override
    public ClaimCrm mapRow(ResultSet rs, int i) throws SQLException {
    	ClaimCrm ClaimObj = new ClaimCrm();
    	ClaimObj.setClaimNumber(rs.getString("ClaimNumber"));
    	ClaimObj.setSellingCompany(rs.getString("SellingCompany"));
    	ClaimObj.setClaimRegion(rs.getString("Region"));
    	ClaimObj.setClaimTerritory(rs.getString("Territory")); 	
    	ClaimObj.setDocumentNumber(rs.getString("DocumentNumber"));
    	ClaimObj.setAmountUsd(rs.getBigDecimal("AmountUsd"));
    	ClaimObj.setDocumentDate(rs.getDate("DocumentDate"));
    	ClaimObj.setOrderNumber(rs.getString("OrderNumber"));
    	ClaimObj.setPurchaseOrderNumber(rs.getString("PurchaseOrderNumber"));
        return ClaimObj;
    }

}

