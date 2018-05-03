package com.scienceroot.util;

import static com.scienceroot.security.SecurityConstants.EXPIRATION_TIME_IN_MILLIS;
import static com.scienceroot.security.SecurityConstants.SECRET;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtHelper {

    public static String createJwt(String mail) {
        return Jwts.builder()
                .setSubject(mail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }
}
