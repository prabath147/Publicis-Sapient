package com.admin.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClient;

import feign.RequestInterceptor;

@SpringBootApplication
@EnableFeignClients
public class AdminServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}
	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder();
	}
	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper =  new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE).setAmbiguityIgnored(true);

		return mapper;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		
		return requestTemplate -> {
			String token = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.getCredentials() != null) {				
				token = authentication.getCredentials().toString();				
			}
			
			requestTemplate.header("Authorization", "Bearer "+token);	
		};
	}

}
