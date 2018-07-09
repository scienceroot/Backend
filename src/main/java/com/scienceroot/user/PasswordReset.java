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
    private static final String APIKEY_ENV_VARIABLE = "SCR_MAILGUN_API_KEY";
    private static final String DOMAIN_ENV_VARIABLE = "SCR_MAILGUN_DOMAIN";
    private static final String LOGIN_ENV_VARIABLE = "SCR_MAILGUN_LOGIN";

    /**
     *
     */
    public PasswordReset() {
        this.config = new Configuration()
                //FIXME: effing environment variables still not loading, hardwiring for private beta...
                .apiKey("key-3c14d95e94ff3515c69e2a9199d1a127")
                .domain("app.scienceroot.com")
                .from("Scienceroot", "postmaster@app.scienceroot.com");

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
                .subject("ScienceRoot Password reset")
                .text("You seem to have forgotten your password, click here to change it: https://"+"app.scienceroot.com"+"/resetPassword?t=" + newToken )
                .build()
                .send();
        return resp.isOk();
    }

}
