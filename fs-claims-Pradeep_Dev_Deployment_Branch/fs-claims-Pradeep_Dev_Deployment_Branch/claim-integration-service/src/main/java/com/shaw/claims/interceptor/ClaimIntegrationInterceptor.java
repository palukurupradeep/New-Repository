package com.shaw.claims.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;	
	@Component
	public class ClaimIntegrationInterceptor implements HandlerInterceptor {
		 private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

		Logger log = LogManager.getLogger(ClaimIntegrationInterceptor.class);
		 @Override
		   public boolean preHandle
		      (HttpServletRequest request, HttpServletResponse response, Object handler) 
		      throws Exception {	      
			  String correlationId;
			  
			  correlationId = request.getHeader("Correalation-Id");
			  
			  if (correlationId == null) {
		   		   log.info("In generateUniqueCorrelationId");
		           correlationId = generateUniqueCorrelationId();
		       } 
			  log.info("Correaltion Id =  " + correlationId);
		   	  ThreadContext.put(CORRELATION_ID_LOG_VAR_NAME,correlationId);
		      return true;
		   }
		
		   private String generateUniqueCorrelationId() {
			   return UUID.randomUUID().toString();
		}
			
	}
