/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

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
public class ArxivTest {
    
    public ArxivTest() {
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
     * Test of runSearch method, of class Arxiv.
     */
    @Test
    public void testRunSearch() {
        System.out.println("runSearch");
        String url = "http://export.arxiv.org/api/query?search_query=au:del_maestro";
        Arxiv instance = new Arxiv();
        Paper[] expResult = null;
        Paper[] result = instance.runSearch(url);
        for(Paper pape : result){
            System.out.println(pape.toString());
        }
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
