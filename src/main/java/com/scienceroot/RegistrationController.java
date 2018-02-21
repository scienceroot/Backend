/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot;

import static com.scienceroot.security.SecurityConstants.EXPIRATION_TIME;
import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Headers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scienceroot.security.SecurityConstants;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author husche
 */
@RestController
@RequestMapping("")
@CrossOrigin()
public class RegistrationController {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public ApplicationUserRepository userRepository;

	public RegistrationController(@Autowired ApplicationUserRepository userRepository,
			@Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping(value = "/register")
	public ResponseEntity createUser(@RequestBody ApplicationUser user) throws JsonProcessingException {
		System.out.println(user.getUsername());
		if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
			return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
		} else {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			this.userRepository.save(user);

			String userStr = new ObjectMapper().writeValueAsString(user);
			String token = Jwts.builder().setSubject(user.getUsername())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();

			return ResponseEntity.status(HttpStatus.CREATED)
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
					.body(userStr);
		}
	}

	@PostMapping(value = "/login")
	public ResponseEntity login(@RequestBody ApplicationUser user) throws JsonProcessingException {

		Optional<ApplicationUser> dbUser = this.userRepository.findByUsername(user.getUsername());

		if (dbUser.isPresent()) {

			if (this.bCryptPasswordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
				String userStr = new ObjectMapper().writeValueAsString(dbUser);
				String token = Jwts.builder().setSubject(user.getUsername())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
				
				return ResponseEntity.status(HttpStatus.CREATED)
						.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
						.body(userStr);
			} else {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public ResponseEntity tokenStatus(@RequestHeader(value = "Authorization", required = false) String token) throws JsonProcessingException {
		Date expirationDate = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody().getExpiration();
		
		String user = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody().getSubject();
		
		
		
		if(expirationDate.after(new Date())) {
			String newToken = Jwts.builder().setSubject(user)
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + newToken)
					.header("Access-Control-Expose-Headers", SecurityConstants.HEADER_STRING
							+ " , Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, Pragma")
					.body(null);
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/hue")
	public String hue() {
		this.userRepository.findAll().forEach(
				user -> System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPassword()));

		return "hue";
	}

}
