package com.shaw.claims;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClaimBatchMain {
	public static void main(String[] args) {
		SpringApplication.run(ClaimBatchMain.class, args);
	}
}
