/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.security;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author husche
 */
public class SecurityConstants {
    
    public static final String SECRET = "huehuehue";
    
    public static final long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(10);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/register";
    public static final String SIGN_IN_URL = "/login";
}
