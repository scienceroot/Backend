package com.scienceroot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Configuration
public class MockMvcConfig {

	@Bean
	public MockMvc mockMvc(WebApplicationContext context) {
		return webAppContextSetup(context).build();
	}
}
