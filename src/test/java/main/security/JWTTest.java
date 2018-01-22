/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.security;

import junit.framework.TestCase;

/**
 *
 * @author husche
 */
public class JWTTest extends TestCase {

    public JWTTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        JWT jwt = new JWT();
        String generatedJWT = jwt.createJWT("1", "ich", "hue", 100000);
        System.out.println(generatedJWT);
        assert (jwt.isValidJWT(generatedJWT));

    }

}
