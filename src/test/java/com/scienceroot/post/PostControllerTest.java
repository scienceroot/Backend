package com.scienceroot.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scienceroot.industry.IndustryRepository;
import com.scienceroot.interest.InterestRepository;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.job.JobRepository;
import com.scienceroot.user.language.LanguageRepository;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author svenseemann
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ApplicationUserService userService;
    
    @Autowired
    private PostService postService;

    @Autowired
    private ApplicationUserRepository repository;
    
    @Autowired private InterestRepository interestRepository;
    @Autowired private LanguageRepository languageRepository;
    @Autowired private IndustryRepository industryRepository;
    @Autowired private JobRepository jobRepository;

    private ApplicationUser currentUser;

    @Before
    public void setUp() throws Exception {
            this.currentUser = new ApplicationUser();
            this.currentUser.setLastname("Test");
            this.currentUser.setForename("Test");
            this.currentUser = this.userService.save(this.currentUser);
            
            // just to be sure, you can validate the start settings, defined in setUp()
            assertThat(this.currentUser, notNullValue());
            assertThat(this.currentUser.getLastname(), is("Test"));
    }

    @After
    public void tearDown() throws Exception {
            this.repository.deleteAll();
    }
    
    @Test
    public void createPost() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Post toCreate = this.getTestPost();
        
        this.mockMvc
            .perform(post("/posts/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(toCreate))
            )

            .andDo(print())

            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.creator.uid").value(this.currentUser.getId().toString()))
            .andExpect(jsonPath("$.content").value(toCreate.getContent()));
    }
    
    @Test
    public void getEmptyPostsByUserId() throws Exception {
        this.mockMvc
            .perform(get("/posts/user/" + this.currentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )

            .andDo(print())

            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void getPostsByUserId() throws Exception {
        Post post = this.getTestPost();
        this.postService.save(post);
        
        this.mockMvc
            .perform(get("/posts/user/" + this.currentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )

            .andDo(print())

            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].content").value(post.getContent()))
            .andExpect(jsonPath("$[0].creator.uid").value(this.currentUser.getId().toString()));
    }
    
    private Post getTestPost() {
        Post toCreate = new Post();
        String content = "Some random test post.";
        
        toCreate.setContent(content);
        toCreate.setCreator(this.currentUser);
        
        return toCreate;
    }
}
