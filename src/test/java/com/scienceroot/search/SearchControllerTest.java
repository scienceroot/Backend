package com.scienceroot.search;

import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.skill.Skill;
import com.scienceroot.util.ApplicationUserHelper;
import com.scienceroot.util.JwtHelper;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    
    private ApplicationUser currentUser;
    private String jwt;
    
    public SearchControllerTest() {
    }

    @Before
    public void setUp() {
        this.currentUser = ApplicationUserHelper.getTestUser();
        this.currentUser = this.service.save(this.currentUser);

        this.jwt = JwtHelper.createJwt(this.currentUser.getMail());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class SearchController.
     */
    @Test
    @Ignore
    public void testSearch() throws IOException {
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
    public void testSearchPapers() throws Exception {
        String q = "Paper";
        MvcResult andReturn = this.mockMvc.perform(get("/search/papers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.jwt)
                .param("q", q))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resp = andReturn.getResponse().getContentAsString();
// TODO review the generated test code and remove the default call to fail.
    }
    
    @Test
    @Ignore
    public void testSearchpreprints() throws Exception {
        this.mockMvc.perform(get("/search/preprints")
                .contentType(MediaType.APPLICATION_JSON)
                .param("q", "Car"))
                .andDo(print());
    }

    /**
     * Test of searchPapersAdvanced method, of class SearchController.
     */
    @Test
    public void testSearchPapersAdvanced() throws Exception {
        String title = "checkerboard";
        String author = "del_maestro";
        String abstractText = "";
        this.mockMvc.perform(get("/search/papers_advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.jwt)
                .param("ti", title)
                .param("au", author)
                .param("abs", abstractText))
                .andDo(print());
    }

    /**
     * Test of searchUsers method, of class SearchController.
     */
    @Test
    @Ignore
    public void testSearchUsers() throws Exception {
        String q = "";
        List<ApplicationUser> expResult = null;
        
        this.mockMvc.perform(get("/search/papers_advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .param("q", q))
                .andDo(print());
    }

    /**
     * Test of searchSkills method, of class SearchController.
     */
    @Test
    @Ignore
    public void testSearchSkills() {
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
    @Ignore
    public void testSearchLanguages() throws Exception {
        String q = "afrikaans";
        
        this.mockMvc
            .perform(get("/search/languages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.jwt)
                            .param("q", q)
            )
                .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Afrikaans"))
            .andReturn();
    }
    
    private String createForbiddenJwt() {
        return JwtHelper.createJwt("forbidden@forbidden.com");
    }
}
