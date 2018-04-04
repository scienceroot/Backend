/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.util.List;
import java.util.Set;
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
public class SearchServiceTest {
    
    public SearchServiceTest() {
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
     * Test of search method, of class SearchService.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String query = "DNA";
        SearchService instance = new SearchService();
        List<Paper> expResult = null;
        List<Paper> result = instance.search(query);
        assertEquals(result.size(), 40);
        
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of searchAdvanced method, of class SearchService.
     */
    @Test
    public void testSearchAdvanced() {
        System.out.println("searchAdvanced");
        SearchService instance = new SearchService();
        List<Paper> expResult = null;
        SearchParameters mp = new SearchParameters();
        mp.setTitle("electrons");
        mp.setAbstract("analysis");
        
        List<Paper> result = instance.searchAdvanced(mp);
        assertEquals(result.size(), 40);
        for (int i = 0; i < result.size(); i++){
            System.out.println(result.get(i).getTitle());
        }
        //List<Paper> result = instance.searchAdvanced(query);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
