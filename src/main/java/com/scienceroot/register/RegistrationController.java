/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.register;

import com.scienceroot.security.SecurityConstants;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import static com.scienceroot.security.SecurityConstants.*;

/**
 * @author husche
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class RegistrationController {

    private Logger LOG = Logger.getLogger(RegistrationController.class.getName());
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ApplicationUserRepository userRepository;

    public RegistrationController(
            @Autowired ApplicationUserRepository userRepository,
            @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.LOG.warning("RegistrationController#init");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "register")
    public ResponseEntity<ApplicationUser> registerUser(
            @RequestBody ApplicationUser user
    ) {
        Optional<ApplicationUser> dbUser = userRepository.findByMail(user.getMail());

        if (dbUser.isPresent()) {
            throw new IllegalStateException("user already exists");
        }

        String password = user.getPassword();
        String passwordHashed = bCryptPasswordEncoder.encode(password);
        user.setPassword(passwordHashed);

        user = this.userRepository.save(user);

        String token = Jwts.builder()
                .setSubject(user.getMail())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .body(user);
    }


    @RequestMapping(value = "token", method = RequestMethod.GET)
    public ResponseEntity tokenStatus(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {

        Date expirationDate = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getExpiration();

        String mail = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        if (expirationDate.after(new Date())) {

            String newToken = Jwts.builder()
                    .setSubject(mail)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                    .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                    .compact();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + newToken)
                    .build();
        } else {

            LOG.info("token status expired");

            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}
