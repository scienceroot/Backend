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
        SearchService instance = new SearchService(40);
        List<Paper> result = instance.search(query);
        assertEquals(40, result.size());
        
    }

    /**
     * Test of searchAdvanced method, of class SearchService.
     */
    @Test
    public void testSearchAdvanced() {
        SearchService instance = new SearchService(60);
        SearchParameters mp = new SearchParameters();
        mp.setTitle("electron microscope");
        mp.setAbstract("electron");
        
        List<Paper> result = instance.searchAdvanced(mp);
        assertEquals(40, result.size());
    }
    
    @Test 
    public void testFilter(){
        int maxResults = 20;
        String query = "Paper";
        SearchService instance = new SearchService(40);
        List<Paper> result = instance.search(query);
        result = SearchFilter.filter(result, maxResults);
        assertEquals(maxResults, result.size());
    }
    
}
