package com.scienceroot.config;

import static com.scienceroot.security.SecurityConstants.SIGN_UP_URL;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.scienceroot.security.JWTAuthenticationFilter;
import com.scienceroot.security.JWTAuthorizationFilter;
import com.scienceroot.security.RestSecurityEntryPoint;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RestSecurityEntryPoint restSecurityEntryPoint;

    public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, RestSecurityEntryPoint restSecurityEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restSecurityEntryPoint = restSecurityEntryPoint;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(restSecurityEntryPoint)
			.and()
		        // don't create session
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		        .authorizeRequests()
		        		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            		.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
	            	.anyRequest()
	            		.authenticated()
			.and()
				.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
	        		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
	        		.addFilter(new JWTAuthorizationFilter(authenticationManager()));
		
		http.headers().cacheControl();
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

	private CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
