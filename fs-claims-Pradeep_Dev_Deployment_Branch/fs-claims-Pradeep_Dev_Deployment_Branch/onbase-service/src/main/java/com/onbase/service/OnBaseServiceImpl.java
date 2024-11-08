package com.onbase.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class OnBaseServiceImpl implements OnBaseService{
    Logger log = LogManager.getLogger(OnBaseServiceImpl.class);
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Value("${onbase.query.claimcount}")
    private String claimCountQry;
    @Override
    public Integer getClaimCount(String activeDirId) {
        Integer claimCount = jdbcTemplate.query(claimCountQry, new OnBaseResultSetExtractor(), activeDirId);
        log.info("Claim Count :: "+claimCount);
        return claimCount;
    }
    private static class OnBaseResultSetExtractor implements ResultSetExtractor<Integer> {
        @Override
        public Integer extractData(ResultSet rs) throws SQLException {
            Integer count = null;
            while (rs.next()) {
                count = rs.getInt("CLAIM_COUNT");
            }
            return count;
        }
    }
}
