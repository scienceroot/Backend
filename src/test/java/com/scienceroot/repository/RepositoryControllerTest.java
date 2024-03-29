package com.scienceroot.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.util.ApplicationUserHelper;
import com.scienceroot.util.JwtHelper;
import java.util.UUID;
import org.junit.After;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private ApplicationUserService userService;
    
    private ApplicationUser currentUser;
    private String jwt;

    public RepositoryControllerTest() {
    }

    @Before
    public void setUp() throws Exception {
        this.currentUser = ApplicationUserHelper.getTestUser();
        this.currentUser = this.userService.save(this.currentUser);

        this.jwt = JwtHelper.createJwt(this.currentUser.getMail());
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test()
    public void createRepository() throws Exception {
        Repository toCreate = new Repository();
        String repoName = "Some test repository";
        
        toCreate.setName(repoName);
        
        this.mockMvc
            .perform(post("/repositories/")
                .header("Authorization", this.jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJson(toCreate))
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(repoName))
            .andExpect(jsonPath("$.privateKey").exists())
            .andExpect(jsonPath("$.publicKey").exists())
            .andExpect(jsonPath("$.creator.uid").value(this.currentUser.getId().toString()));
    }
    
    @Test()
    public void getRepository() throws Exception {
        Repository toCreate = new Repository();
        String repoName = "Some test repository";
        
        toCreate.setName(repoName);
        toCreate.setCreator(this.currentUser);
        
        toCreate = this.repositoryService.create(toCreate);
        
        this.mockMvc
            .perform(get("/repositories/" + toCreate.getId().toString())
                .header("Authorization", this.jwt)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(toCreate.getId().toString()))
            .andExpect(jsonPath("$.name").value(repoName))
            .andExpect(jsonPath("$.privateKey").doesNotExist())
            .andExpect(jsonPath("$.publicKey").exists())
            .andExpect(jsonPath("$.creator.uid").value(this.currentUser.getId().toString()));
    }

    @Test()
    public void getRepositoryByUser() throws Exception {
        Repository toCreate = new Repository();
        String repoName = "Some test repository";
        
        toCreate.setName(repoName);
        toCreate.setCreator(this.currentUser);
        
        toCreate = this.repositoryService.create(toCreate);
        
        this.mockMvc
            .perform(get("/repositories/")
                .header("Authorization", this.jwt)
                .param("userId", this.currentUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(toCreate.getId().toString()))
            .andExpect(jsonPath("$[0].name").value(repoName))
            .andExpect(jsonPath("$[0].privateKey").doesNotExist())
            .andExpect(jsonPath("$[0].publicKey").exists())
            .andExpect(jsonPath("$[0].creator.uid").value(this.currentUser.getId().toString()));
    }
    
    @Test()
    public void getNotExistingRepository() throws Exception {
       
        this.mockMvc
            .perform(get("/repositories/" + UUID.randomUUID())
                .header("Authorization", this.jwt)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }
    
    @Test()
    public void storeData() throws Exception {
        Repository toCreate = new Repository();
        String repoName = "Some test repository";
        
        toCreate.setName(repoName);
        toCreate.setCreator(this.currentUser);
        
        toCreate = this.repositoryService.create(toCreate);
        String content = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." +   
            "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.";
        byte[] data = content.getBytes();
        DataRequestBody body = new DataRequestBody(data, toCreate.getPrivateKey());
        String repoId = toCreate.getId().toString();
        
        this.mockMvc
            .perform(post("/repositories/" + repoId)
                .header("Authorization", this.jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJson(body))
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    
    private String toJson(Object obj) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        
        return ow.writeValueAsString(obj);
    }
}
