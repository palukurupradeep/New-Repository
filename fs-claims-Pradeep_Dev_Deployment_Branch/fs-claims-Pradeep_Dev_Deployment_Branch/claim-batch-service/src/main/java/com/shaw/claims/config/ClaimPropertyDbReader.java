package com.shaw.claims.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import  org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	public class ClaimPropertyDbReader implements EnvironmentPostProcessor {
		
		/**
		 * Name of the custom property source added by this post processor class
		 */
		private static final String PROPERTY_SOURCE_NAME = "databaseProperties";

		private static final Logger log = LoggerFactory.getLogger(ClaimPropertyDbReader.class);


		/**
		 * Adds Spring Environment custom logic. This custom logic fetch properties from database and setting highest precedence
		 */
		@Override
		public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		    Map<String, Object> propertySource = new HashMap<>();

		    try {

		        DataSource ds = DataSourceBuilder
		                .create()
		                .username(environment.getProperty("spring.datasource.username"))
		                .password(environment.getProperty("spring.datasource.password"))
		                .url(environment.getProperty("spring.datasource.url"))
		    		    .build();

		        // Fetch all properties
	    
		        Connection connection = ds.getConnection();
		        log.info("Property read successfully");

		        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ConfigurationKey,ConfigurationValue FROM clm.JobConfiguration");
		        ResultSet rs = preparedStatement.executeQuery();

		     // Populate all properties into the property source
		        while (rs.next()) {
		          propertySource.put(rs.getString("ConfigurationKey"), rs.getString("ConfigurationValue"));
		        }
		        rs.close();
		        preparedStatement.clearParameters();
		        preparedStatement.close();
		        connection.close();

		  // Create a custom property source with the highest precedence and add it to Spring Environment
		        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, propertySource));
		        log.info("Property read successfully {}",propertySource.get("batch.job.clm001"));
		        
		    } catch (Throwable e) {
		        throw new RuntimeException(e);
		    }
		}
	} 


