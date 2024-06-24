package com.gongzone.central.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretConfig {
	
	@Value("${key.secret.iamport}")
	private String secretKey;

	@Bean
	public String secretKey() {
		return secretKey;
	}

}
