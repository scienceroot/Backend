/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import static com.scienceroot.security.SecurityConstants.EXPIRATION_TIME_IN_MILLIS;
import static com.scienceroot.security.SecurityConstants.SECRET;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;

/**
 *
 * @author husche
 */
public class PasswordReset {

    private final Configuration config;

    /**
     *
     */
    public PasswordReset() {
        this.config = new Configuration()
                .apiKey("key-3c14d95e94ff3515c69e2a9199d1a127")
                .domain("sandbox6fcc129c89bf4b5390715241f60d9c33.mailgun.org")
                .from("Scienceroot", "frederik.huschebeck@scienceroot.com");

    }

    /**
     *
     * @param mail
     * @return
     */
    public Boolean sendPasswordMail(String mail) {
        
        String newToken = Jwts.builder()
                .setSubject(mail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
                
        Response resp = Mail.using(config)
                .to(mail)
                .subject("Password reset")
                .text("You seem to have forgotten your password, click here to change it: ")
                .build()
                .send();
        return resp.isOk();
    }

}
