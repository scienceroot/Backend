package com.scienceroot.security;

import static com.scienceroot.security.SecurityConstants.SECRET;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author husche
 */
@Ignore
public class JWTAuthorizationFilterTest {
    
    public JWTAuthorizationFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of doFilterInternal method, of class JWTAuthorizationFilter.
     */
    @Test
    public void testDoFilterInternal() throws Exception {
        String token = Jwts.builder()
                .setSubject("Ping")
                .setExpiration(new Date(System.currentTimeMillis()))// + EXPIRATION_TIME_IN_MILLIS
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();

        //HttpServletRequest req = null;
        //HttpServletResponse res = null;
        //FilterChain chain = null;
        //JWTAuthorizationFilter instance = null;
        //instance.doFilterInternal(req, res, chain);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
