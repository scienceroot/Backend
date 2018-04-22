package com.scienceroot.user;

import com.scienceroot.user.language.Language;
import com.scienceroot.user.language.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.scienceroot.industry.Industry;
import com.scienceroot.industry.IndustryRepository;
import com.scienceroot.interest.Interest;
import com.scienceroot.interest.InterestRepository;
import com.scienceroot.user.job.Job;
import com.scienceroot.user.job.JobRepository;
import java.util.ArrayList;
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

    @Before
    public void setUp() throws Exception {
            this.currentUser = new ApplicationUser();
            this.currentUser.setLastname("Test");
            this.currentUser.setForename("Test");
            this.currentUser = this.service.save(this.currentUser);
            
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
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(put("/users/" + this.currentUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{	" +
                                            "	\"lastname\":\"Test-Lastname\"," +
                                            "	\"forename\":\"Test-Forename\"" +
                                            "}"))

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.lastname").value("Test-Lastname"))
            .andExpect(jsonPath("$.forename").value("Test-Forename"));

        // of course you can validate the state in the backend too
        this.currentUser = this.repository.findOne(this.currentUser.getId());
        assertThat(this.currentUser, notNullValue());
        assertThat(this.currentUser.getLastname(), is("Test-Lastname"));
    }
    
    @Test
    public void addUserJob() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Job jobToAdd = new Job("CEO", 1, 2017, 1, 2018, this.currentUser, "Scienceroot", this.getIndustry());
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(jobToAdd))
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(1))
            .andExpect(jsonPath("$.jobs[0].title").value(jobToAdd.title))
            .andExpect(jsonPath("$.jobs[0].employer").value(jobToAdd.employer))
            .andExpect(jsonPath("$.jobs[0].startMonth").value(jobToAdd.startMonth))
            .andExpect(jsonPath("$.jobs[0].startYear").value(jobToAdd.startYear))
            .andExpect(jsonPath("$.jobs[0].endMonth").value(jobToAdd.endMonth))
            .andExpect(jsonPath("$.jobs[0].endYear").value(jobToAdd.endYear));
    }
    
    
    @Test
    public void followUser () throws Exception {
        ApplicationUser toFollow = new ApplicationUser();
        toFollow.setLastname("Test2");
        toFollow.setForename("Test2");
        toFollow = this.service.save(toFollow);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/follow/" + toFollow.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.follows").isArray())
            .andExpect(jsonPath("$.follows.length()").value(1))
            .andExpect(jsonPath("$.follows[0].lastname").value(toFollow.getLastname()))
            .andExpect(jsonPath("$.follows[0].forename").value(toFollow.getForename()));
    }
    
    @Test
    public void unfollowUser () throws Exception {
        ApplicationUser following = new ApplicationUser();
        following.setLastname("Test2");
        following.setForename("Test2");
        following = this.service.save(following);
        
        List<ApplicationUser> follows = new LinkedList<>();
        follows.add(following);
        this.currentUser.setFollows(follows);
        this.service.save(this.currentUser);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(delete("/users/" + this.currentUser.getId() + "/unfollow/" + following.getId())
                .contentType(MediaType.APPLICATION_JSON)
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.follows").isArray())
            .andExpect(jsonPath("$.follows.length()").value(0));
    }
    
    @Test
    public void getEmptyFollowedBy () throws Exception { 
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(get("/users/" + this.currentUser.getId() + "/followedBy")
                .contentType(MediaType.APPLICATION_JSON)
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
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
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(get("/users/" + this.currentUser.getId() + "/followedBy")
                .contentType(MediaType.APPLICATION_JSON)
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
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
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(post("/users/" + this.currentUser.getId() + "/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(jobToAdd))
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(1))
            .andExpect(jsonPath("$.jobs[0].title").value(jobToAdd.title))
            .andExpect(jsonPath("$.jobs[0].employer").value(jobToAdd.employer))
            .andExpect(jsonPath("$.jobs[0].startMonth").value(jobToAdd.startMonth))
            .andExpect(jsonPath("$.jobs[0].startYear").value(jobToAdd.startYear))
            .andExpect(jsonPath("$.jobs[0].endMonth").isEmpty())
            .andExpect(jsonPath("$.jobs[0].endYear").isEmpty());
    }
    
    @Test
    public void removeUserJob() throws Exception {
        Job jobToAdd = new Job("CEO", 1, 2017, null, null, this.currentUser, "Scienceroot", this.getIndustry());
        List<Job> userJobs = new ArrayList();
               
        userJobs.add(jobToAdd);
        
        this.currentUser.setJobs(userJobs);
        this.jobRepository.save(jobToAdd);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            .perform(delete("/users/" + this.currentUser.getId() + "/jobs/" + jobToAdd.getId())
                            .contentType(MediaType.APPLICATION_JSON)
            )

            .andDo(print())

            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jobs").isArray())
            .andExpect(jsonPath("$.jobs.length()").value(0));
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
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.interests").isArray())
            .andExpect(jsonPath("$.interests[0].name").value(interestToAdd.getName()));
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
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.contact.phone").value(contact.getPhone()))
            .andExpect(jsonPath("$.contact.skype").value(contact.getSkype()));
    }
    
    @Test
    public void removeUserInterest() throws Exception {
        Interest userInterest = this.getInterest();
        List<Interest> userInterests = new ArrayList();
               
        userInterests.add(userInterest);
        
        this.currentUser.setInterests(userInterests);
        this.repository.save(this.currentUser);
        
        this.mockMvc
            // define your request url (PUT of '/users/{uuid}'), content, ...
            .perform(delete("/users/" + this.currentUser.getId() + "/interests/" + userInterest.getId())
                            .contentType(MediaType.APPLICATION_JSON)
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
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
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
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
            )

            // debug, prints a shit of info (remove this line, when not needed)
            .andDo(print())

            // validate the response
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.languages").isArray())
            .andExpect(jsonPath("$.languages.length()").value(0));
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
}