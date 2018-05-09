/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

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
public class PasswordResetTest {
    
    public PasswordResetTest() {
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
     * Test of sendPasswordMail method, of class PasswordReset.
     */
    @Test
    @Ignore
    public void testSendPasswordMail() {
        String mail = "frederik.huschebeck@gmail.com";
        PasswordReset instance = new PasswordReset();
        assertTrue(instance.sendPasswordMail(mail));
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
