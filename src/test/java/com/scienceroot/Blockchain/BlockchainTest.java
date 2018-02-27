/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.Blockchain;

import com.scienceroot.blockchain.Blockchain;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author husche
 */
public class BlockchainTest {
    
    public BlockchainTest() {
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
     * Test of connectToBlockchain method, of class Blockchain.
     */
    @Test
    public void testConnectToBlockchain() {
        System.out.println("connectToBlockchain");
        Blockchain instance = new Blockchain();
        assertTrue(instance.sendInitialFunds("0x3D8c4e57d6b0c5Bba4AC591efA4D9469b872A346"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
