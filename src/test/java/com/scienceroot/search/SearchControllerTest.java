/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.Language;
import com.scienceroot.user.Skill;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 *
 * @author husche
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ApplicationUserService service;

    @Autowired
    private SearchService searchService;

    public SearchControllerTest() {
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
     * Test of search method, of class SearchController.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String q = "";
        String type = "";
        SearchController instance = null;
        List<SearchResult> expResult = null;
        List<SearchResult> result = instance.search(q, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchPapers method, of class SearchController.
     */
    @Test
    public void testSearchPapers() {
        System.out.println("searchPapers");
        String q = "";
        SearchController instance = null;
        List<Paper> expResult = null;
        List<Paper> result = instance.searchPapers(q);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchPapersAdvanced method, of class SearchController.
     */
    @Test
    public void testSearchPapersAdvanced() throws Exception {
        System.out.println("searchPapersAdvanced");
        String title = "checkerboard";
        String author = "del_maestro";
        String abstractt = "";
        this.mockMvc.perform(get("/search/papers_advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ti", title)
                .param("au", author)
                .param("abs", abstractt))
                .andDo(print());
    }

    /**
     * Test of searchUsers method, of class SearchController.
     */
    @Test
    public void testSearchUsers() {
        System.out.println("searchUsers");
        String q = "";
        SearchController instance = null;
        List<ApplicationUser> expResult = null;
        List<ApplicationUser> result = instance.searchUsers(q);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchSkills method, of class SearchController.
     */
    @Test
    public void testSearchSkills() {
        System.out.println("searchSkills");
        String q = "";
        SearchController instance = null;
        List<Skill> expResult = null;
        List<Skill> result = instance.searchSkills(q);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchLanguages method, of class SearchController.
     */
    @Test
    public void testSearchLanguages() {
        System.out.println("searchLanguages");
        String q = "";
        SearchController instance = null;
        List<Language> expResult = null;
        List<Language> result = instance.searchLanguages(q);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
