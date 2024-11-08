package com.shaw.claims.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleException(RuntimeException ex){
        log.info("Inside ExceptionHandlerAdvice.handleException()");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        log.error("Exception Occurred : {}",ex.getMessage());
        return problemDetail;
    }
    
    @ExceptionHandler(CommonException.class)
    public ProblemDetail globalException(CommonException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getErrorMessage());
    	return problemDetail;
    }
}
