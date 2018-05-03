package com.scienceroot.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scienceroot.industry.IndustryRepository;
import com.scienceroot.interest.InterestRepository;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.job.JobRepository;
import com.scienceroot.user.language.LanguageRepository;
import com.scienceroot.util.ApplicationUserHelper;
import com.scienceroot.util.JwtHelper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private String jwt;

    @Before
    public void setUp() throws Exception {
            this.currentUser = ApplicationUserHelper.getTestUser();
            this.currentUser = this.userService.save(this.currentUser);
            
            this.jwt = JwtHelper.createJwt(this.currentUser.getMail());
    }

    @After
    public void tearDown() throws Exception {
            this.repository.deleteAll();
    }
    
    @Test
    public void createPost() throws Exception {
        Post toCreate = this.getCurrentUserPost();
        
        this.mockMvc
            .perform(post("/posts/")
                    .header("Authorization", this.jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.getPostAsString(toCreate))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.creator.uid").value(this.currentUser.getId().toString()))
            .andExpect(jsonPath("$.content").value(toCreate.getContent()));
    }
    
    @Test
    public void createPostForbidden() throws Exception {
        Post toCreate = this.getCurrentUserPost();
        
        ApplicationUser wrongUser = new ApplicationUser();
        wrongUser.setMail("wrong@wrong.com");
        wrongUser = this.userService.save(wrongUser);
        
        toCreate.setCreator(wrongUser);
        
        this.mockMvc
            .perform(post("/posts/")
                    .header("Authorization", this.jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.getPostAsString(toCreate))
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void deletePost() throws Exception {
        Post toDelete = this.getCurrentUserPost();
        
        toDelete = this.postService.save(toDelete);
        
        this.mockMvc
            .perform(delete("/posts/" + toDelete.getId())
                    .header("Authorization", this.jwt)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
        
        Optional<Post> after = this.postService.findById(toDelete.getId());
        assertThat(after.isPresent(), is(false));
    }
    
    @Test
    public void deletePostForbidden() throws Exception {
        Post toDelete = this.getCurrentUserPost();
        ApplicationUser wrongUser = new ApplicationUser();
        
        wrongUser.setMail("wrong@wrong.com");
        wrongUser = this.userService.save(wrongUser);
        
        toDelete.setCreator(wrongUser);
       
        toDelete = this.postService.save(toDelete);
        
        this.mockMvc
            .perform(delete("/posts/" + toDelete.getId())
                    .header("Authorization", this.jwt)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden())
            .andReturn();
    }
    
     @Test
    public void deletePostNotExisting() throws Exception {
        UUID notExisting = UUID.fromString("8589f23c-174e-471e-998c-9cbeb2893ae7");
        
        this.mockMvc
            .perform(delete("/posts/" + notExisting)
                    .header("Authorization", this.jwt)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound())
            .andReturn();
    }
    
    @Test
    public void getEmptyPostsByUserId() throws Exception {
        this.mockMvc
            .perform(get("/posts/user/" + this.currentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void getPostsByUserId() throws Exception {
        Post post = this.postService.save(this.getCurrentUserPost());
        
        this.mockMvc
            .perform(get("/posts/user/" + this.currentUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].content").value(post.getContent()))
            .andExpect(jsonPath("$[0].creator.uid").value(this.currentUser.getId().toString()));
    }
    
    @Test
    public void getPostsFeed() throws Exception {
        /**
         * Create followed users for currentUser
         */
        ApplicationUser userA = this.userService.save(ApplicationUserHelper.getTestUser("A"));
        ApplicationUser userB = this.userService.save(ApplicationUserHelper.getTestUser("B"));
        
        List<ApplicationUser> follows = new LinkedList<>();
        follows.add(userA);
        follows.add(userB);
        
        this.currentUser.setFollows(follows);
        this.currentUser = this.userService.save(this.currentUser);
        
        /**
         * Create posts for followed users
         */
        this.postService.save(this.getTestPost(userA));
        this.postService.save(this.getTestPost(userB));
        
        this.mockMvc
            .perform(get("/posts/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.jwt)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].creator.uid").value(userA.getId().toString()))
            .andExpect(jsonPath("$[1].creator.uid").value(userB.getId().toString()));
    }
    
    private Post getCurrentUserPost() {
        return this.getTestPost(this.currentUser);
    }
    
    private Post getTestPost(ApplicationUser creator) {
        Post toCreate = new Post();
        String content = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lore";
        
        toCreate.setContent(content);
        toCreate.setCreator(creator);
        
        return toCreate;
    }
    
    private String getPostAsString(Post toString) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        
        return ow.writeValueAsString(toString);
    }
}
