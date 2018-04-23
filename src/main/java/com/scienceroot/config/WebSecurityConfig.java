package com.scienceroot.config;

import com.scienceroot.security.JWTAuthenticationFilter;
import com.scienceroot.security.JWTAuthorizationFilter;
import com.scienceroot.security.RestSecurityEntryPoint;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.scienceroot.security.SecurityConstants.SIGN_IN_URL;
import static com.scienceroot.security.SecurityConstants.SIGN_UP_URL;

/**
 *
 * @author husche
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RestSecurityEntryPoint restSecurityEntryPoint;

    /**
     *
     * @param userDetailsService
     * @param bCryptPasswordEncoder
     * @param restSecurityEntryPoint
     */
    public WebSecurityConfig(UserDetailsService userDetailsService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             RestSecurityEntryPoint restSecurityEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restSecurityEntryPoint = restSecurityEntryPoint;
    }
	
    /**
     *
     * @param http
     * @throws Exception
     */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(restSecurityEntryPoint)
			.and()
		        // don't create session
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		        .authorizeRequests()
	            		.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
	            		.antMatchers(HttpMethod.POST, SIGN_IN_URL).permitAll()
	            	.anyRequest()
	            		.authenticated()
			.and()
	        		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
	        		.addFilter(new JWTAuthorizationFilter(authenticationManager()));
		
		//http.headers().cacheControl();
	}
	
    /**
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
