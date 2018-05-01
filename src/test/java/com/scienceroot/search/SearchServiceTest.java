package com.scienceroot.search;

import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.springframework.cache.annotation.EnableCaching;

/**
 *
 * @author husche
 */

@EnableCaching
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
        String query = "electron microscope";
        SearchService instance = new SearchService();
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
        SearchService instance = new SearchService();
        SearchParameters mp = new SearchParameters();
        mp.setTitle("electron microscope");
        mp.setAbstract("analysis");
        
        List<Paper> result = instance.searchAdvanced(mp);
        assertEquals(result.size(), 30);
    }
    
    @Test
    @Ignore
    public void testSearchCache(){
        String query = "DNA";

        SearchService instance = new SearchService();
        List<Paper> result = instance.search(query);
        assertEquals(result.size(), 40);
        result = instance.search(query);
        assertEquals(result.size(), 40);
    }
    
}
