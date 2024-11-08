package com.shaw.claims.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DateUtil {
	 public LocalDateTime getLocalDateTime(String date){
	        LocalDate localDate = LocalDate.parse(date);
	        return localDate.atStartOfDay();
	    }
    
    public boolean isWithinLastDay(LocalDate date) {
        // Check if the given LocalDate is within the last day
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return date.isEqual(yesterday);
    }

    public Date getDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(date);
        return d;
    }
}
