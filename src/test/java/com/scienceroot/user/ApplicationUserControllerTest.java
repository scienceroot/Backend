package com.scienceroot.user;

import com.scienceroot.user.language.Language;
import com.scienceroot.user.language.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scienceroot.industry.Industry;
import com.scienceroot.industry.IndustryRepository;
import com.scienceroot.interest.Interest;
import com.scienceroot.interest.InterestRepository;
import static com.scienceroot.security.SecurityConstants.EXPIRATION_TIME_IN_MILLIS;
import static com.scienceroot.security.SecurityConstants.SECRET;
import com.scienceroot.user.job.Job;
import com.scienceroot.user.job.JobRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.springframework.security.core.userdetails.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class ApplicationUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ApplicationUserService service;

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
            this.currentUser = new ApplicationUser();
            this.currentUser.setLastname("Test");
            this.currentUser.setForename("Test");
            this.currentUser.setMail("test@test.de");
            this.currentUser = this.service.save(this.currentUser);
            
            this.jwt = this.createJwt(this.currentUser.getMail());
            
            // just to be sure, you can validate the start settings, defined in setUp()
            assertThat(this.currentUser, notNullValue());
            assertThat(this.currentUser.getLastname(), is("Test"));
    }

    @After
    public void tearDown() throws Exception {
            this.repository.deleteAll();
    }

    @Test
    public void updateUser() throws Exception {
        
        this.mockMvc
            .perform(put("/users/" + this.currentUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.jwt)
                            .content("{\"lastname\":\"Test-Updated\"}"))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.lastname").value("Test-Updated"))
            .andExpect(jsonPath("$.forename").value("Test"))
            .andExpect(jsonPath("$.mail").value("test@test.de"))
            .andReturn();

        // of course you can validate the state in the backend too
        ApplicationUser updatedUser = this.repository.findOne(this.currentUser.getId());
        assertThat(updatedUser, notNullValue());
        assertThat(updatedUser.getLastname(), is("Test-Updated"));
        assertThat(updatedUser.getForename(), is("Test"));
    }
    
    @Test
    public void updateUserForbidden() throws Exception {
        String wrongJwt = this.createForbiddenJwt();
        
        this.mockMvc
            .perform(put("/users/" + this.currentUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", wrongJwt)
                            .content("{\"lastname\":\"Test-Updated\"}"))
            .andExpect(status().isForbidden())
            .andReturn();
    }
    
    @Test
    public void addUserJob() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Job jobToAdd = new Job("CEO", 1, 2017, 1, 2018, this.currentUser, "Scienceroot", this.getIndustry());
        
        this.mockMvc
            .perform(post("/users/" + this.currentUser.getId() + "/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(jobToAdd))
                            .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(1))
            .andExpect(jsonPath("$.jobs[0].title").value(jobToAdd.getTitle()))
            .andExpect(jsonPath("$.jobs[0].employer").value(jobToAdd.employer))
            .andExpect(jsonPath("$.jobs[0].startMonth").value(jobToAdd.getStartMonth()))
            .andExpect(jsonPath("$.jobs[0].startYear").value(jobToAdd.getStartYear()))
            .andExpect(jsonPath("$.jobs[0].endMonth").value(jobToAdd.getEndMonth()))
            .andExpect(jsonPath("$.jobs[0].endYear").value(jobToAdd.getEndYear()));
    }
    
    @Test
    public void addUserJobForbidden() throws Exception {
        String wrongJwt = this.createForbiddenJwt();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Job jobToAdd = new Job("CEO", 1, 2017, 1, 2018, this.currentUser, "Scienceroot", this.getIndustry());
        
        this.mockMvc
            .perform(post("/users/" + this.currentUser.getId() + "/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", wrongJwt)
                            .content(ow.writeValueAsString(jobToAdd))
            )
            .andExpect(status().isForbidden());
    }
    
    
    @Test
    public void followUser() throws Exception {
        ApplicationUser toFollow = new ApplicationUser();
        toFollow.setLastname("Test2");
        toFollow.setForename("Test2");
        toFollow = this.service.save(toFollow);
        
        this.mockMvc
            .perform(post("/users/" + this.currentUser.getId() + "/follow/" + toFollow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.follows").isArray())
            .andExpect(jsonPath("$.follows.length()").value(1))
            .andExpect(jsonPath("$.follows[0].lastname").value(toFollow.getLastname()))
            .andExpect(jsonPath("$.follows[0].forename").value(toFollow.getForename()));
    }
    
    @Test
    public void followUserForbidden() throws Exception {
        String wrongJwt = this.createForbiddenJwt();
        ApplicationUser toFollow = new ApplicationUser();
        toFollow.setLastname("Test2");
        toFollow.setForename("Test2");
        toFollow = this.service.save(toFollow);
        
        this.mockMvc
            .perform(post("/users/" + this.currentUser.getId() + "/follow/" + toFollow.getId())
                .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", wrongJwt)
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void unfollowUserForbidden() throws Exception {
        ApplicationUser following = new ApplicationUser();
        List<ApplicationUser> follows = new LinkedList<>();
        String wrongJwt = this.createForbiddenJwt();
        
        following.setLastname("Test2");
        following.setForename("Test2");
        following = this.service.save(following);
        
        
        follows.add(following);
        this.currentUser.setFollows(follows);
        this.service.save(this.currentUser);
        
        this.mockMvc
            .perform(delete("/users/" + this.currentUser.getId() + "/unfollow/" + following.getId())
                .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", wrongJwt)
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void getEmptyFollowedBy () throws Exception { 
        this.mockMvc
            .perform(get("/users/" + this.currentUser.getId() + "/followedBy")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(200))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void getFollowedBy () throws Exception {
        ApplicationUser follower = new ApplicationUser();
        List<ApplicationUser> follows = new LinkedList<>();
        
        follows.add(this.currentUser);
        
        follower.setLastname("Test2");
        follower.setForename("Test2");
        follower.setFollows(follows);
        
        follower = this.service.save(follower);
        
        this.mockMvc
            .perform(get("/users/" + this.currentUser.getId() + "/followedBy")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(200))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].lastname").value(follower.getLastname()))
            .andExpect(jsonPath("$[0].forename").value(follower.getForename()));
    }
    
    @Test
    public void addActiveUserJob() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Job jobToAdd = new Job("CEO", 1, 2017, null, null, this.currentUser, "Scienceroot", this.getIndustry());
        
        this.mockMvc
            .perform(post("/users/" + this.currentUser.getId() + "/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(jobToAdd))
                            .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(1))
            .andExpect(jsonPath("$.jobs[0].title").value(jobToAdd.getTitle()))
            .andExpect(jsonPath("$.jobs[0].employer").value(jobToAdd.employer))
            .andExpect(jsonPath("$.jobs[0].startMonth").value(jobToAdd.getStartMonth()))
            .andExpect(jsonPath("$.jobs[0].startYear").value(jobToAdd.getStartYear()))
            .andExpect(jsonPath("$.jobs[0].endMonth").isEmpty())
            .andExpect(jsonPath("$.jobs[0].endYear").isEmpty());
    }
    
    @Test
    public void removeUserJob() throws Exception {
        Job jobToAdd = new Job("CEO", 1, 2017, null, null, this.currentUser, "Scienceroot", this.getIndustry());
        
        this.currentUser.addJob(jobToAdd);
        this.jobRepository.save(jobToAdd);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            .perform(delete("/users/" + this.currentUser.getId() + "/jobs/" + jobToAdd.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(0));
    }
    
    @Test
    public void removeUserJobForbidden() throws Exception {
        Job jobToAdd = new Job("CEO", 1, 2017, null, null, this.currentUser, "Scienceroot", this.getIndustry());
        String wrongJwt = this.createForbiddenJwt();
        
        this.currentUser.addJob(jobToAdd);
        this.jobRepository.save(jobToAdd);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            .perform(delete("/users/" + this.currentUser.getId() + "/jobs/" + jobToAdd.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", wrongJwt)  
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void addUserInterest() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Interest interestToAdd = this.getInterest();
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/interests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(interestToAdd))
                            .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.interests").isArray())
            .andExpect(jsonPath("$.interests[0].name").value(interestToAdd.getName()));
    }
    
    @Test
    public void addUserInterestForbidden() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Interest interestToAdd = this.getInterest();
        String wrongJwt = this.createForbiddenJwt();
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/interests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(interestToAdd))
                            .header("Authorization", wrongJwt)
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void addUserContact() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserContact contact = new UserContact();
        contact.setPhone("1234");
        contact.setSkype("testskype");
               
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/contact")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(contact))
                            .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.contact.phone").value(contact.getPhone()))
            .andExpect(jsonPath("$.contact.skype").value(contact.getSkype()));
    }
    
    @Test
    public void addUserContactForbidden() throws Exception {
        String wrongJwt = this.createForbiddenJwt();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserContact contact = new UserContact();
        contact.setPhone("1234");
        contact.setSkype("testskype");
               
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/contact")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(contact))
                            .header("Authorization", wrongJwt)
            )
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void removeUserInterest() throws Exception {
        Interest userInterest = this.getInterest();
        
        this.currentUser.addInterest(userInterest);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(delete("/users/" + this.currentUser.getId() + "/interests/" + userInterest.getId())
                            .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.interests").isArray())
            .andExpect(jsonPath("$.interests.length()").value(0));
    }
    
    @Test
    public void addUserLanguage() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Language languageToAdd = this.getLanguage();
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/languages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ow.writeValueAsString(languageToAdd))
                    .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.languages").isArray())
            .andExpect(jsonPath("$.languages.length()").value(1))
            .andExpect(jsonPath("$.languages[0].name").value(languageToAdd.getName()));
    }
    
    @Test
    public void removeUserLanguage() throws Exception {
        Language userLanguage = this.getLanguage();
        List<Language> userLanguages = new ArrayList();
               
        userLanguages.add(userLanguage);
        
        this.currentUser.setLanguages(userLanguages);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(delete("/users/" + this.currentUser.getId() + "/languages/" + userLanguage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", this.jwt)
            )
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.languages").isArray())
            .andExpect(jsonPath("$.languages.length()").value(0));
    }
    
    @Test
    public void addUserLanguageForbidden() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Language language = this.getLanguage();
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/languages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(language))
                            .header("Authorization", this.createForbiddenJwt())
            )
            .andExpect(status().is(403));
    }
    
    @Test
    public void removeUserLanguageForbidden() throws Exception {
        Language userLanguage = this.getLanguage();
        List<Language> userLanguages = new ArrayList();
               
        userLanguages.add(userLanguage);
        
        this.currentUser.setLanguages(userLanguages);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(delete("/users/" + this.currentUser.getId() + "/languages/" + userLanguage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", this.createForbiddenJwt())
                           
            )
            .andExpect(status().is(403));
    }
    
    private Interest getInterest() {
        Interest interestToAdd = this.interestRepository
                .findAll()
                .iterator()
                .next();
        
        return interestToAdd;
    }
    
    
    private Industry getIndustry() {
        Industry industry = this.industryRepository
                .findAll()
                .iterator()
                .next();
        
        return industry;
    }
    
    private Language getLanguage() {
        Language language = this.languageRepository
                .findAll()
                .iterator()
                .next();
        
        return language;
    }
    
    private String createForbiddenJwt() {
        return this.createJwt("forbidden@forbidden.com");
    }
    
    private String createJwt(String mail) {
        return Jwts.builder()
                .setSubject(mail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }
}