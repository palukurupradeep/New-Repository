package com.shaw.claims.exception;

import com.shaw.claims.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleException(RuntimeException ex){
        log.info("Inside ExceptionHandlerAdvice.handleException()");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.error("Exception Occurred : {}", ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ClaimInitiateException.class)
    public ProblemDetail handleClaimInitiateException(ClaimInitiateException ex){
        log.info("Inside ExceptionHandlerAdvice.handleInvalidClaimDataException()");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.error("Invalid claim data exception:", ex);
        return problemDetail;
    }
    
    @ExceptionHandler(CommonException.class)
    public ProblemDetail globalException(CommonException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getErrorMessage());
    	return problemDetail;
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ResponseDTO> handleCustomValidationException(CustomValidationException ex) {
        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setIsSuccess(false);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setErrorCode(ex.getErrorCode());
        errorResponse.setData(null);
        return new ResponseEntity<>(errorResponse, HttpStatus.EXPECTATION_FAILED);
    }
}
