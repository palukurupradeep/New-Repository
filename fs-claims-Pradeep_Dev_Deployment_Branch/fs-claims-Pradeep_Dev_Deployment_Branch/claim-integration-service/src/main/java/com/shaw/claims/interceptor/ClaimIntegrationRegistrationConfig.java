package com.shaw.claims.interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ClaimIntegrationRegistrationConfig implements WebMvcConfigurer {  
		 @Autowired
		 ClaimIntegrationInterceptor claimintegrationinterceptor;
		   @Override
		   public void addInterceptors(InterceptorRegistry registry) {
			   
		      registry.addInterceptor(claimintegrationinterceptor);
		   }
}

