package com.scienceroot;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author husche
 */
@SpringBootApplication
public class Application {

    /**
     *
     * @return
     */
    @Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
		org.apache.log4j.BasicConfigurator.configure();
		SpringApplication.run(Application.class, args);
	}
}
