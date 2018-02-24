/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot;

import static com.scienceroot.security.SecurityConstants.EXPIRATION_TIME;
import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/")
@CrossOrigin()
public class RegistrationController {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public ApplicationUserRepository userRepository;

	public RegistrationController(@Autowired ApplicationUserRepository userRepository,
			@Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping(value = "register")
	public ResponseEntity createUser(@RequestBody ApplicationUser user) throws JsonProcessingException {

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
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token).body(userStr);
		}
	}


	@RequestMapping(value = "token", method = RequestMethod.GET)
	public ResponseEntity tokenStatus(@RequestHeader(value = "Authorization", required = false) String token)
			throws JsonProcessingException {
		Date expirationDate = Jwts.parser().setSigningKey(SECRET.getBytes())
				.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getExpiration();

		String user = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody().getSubject();

		if (expirationDate.after(new Date())) {
			String newToken = Jwts.builder().setSubject(user)
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();

			return ResponseEntity.status(HttpStatus.CREATED)
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + newToken).body(user);
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
