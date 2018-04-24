package com.scienceroot.user;

import com.scienceroot.user.job.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class ApplicationUserTest {

    
    @Autowired
    private ApplicationUserService userService;
    
    private ApplicationUser currentUser;

    
    public ApplicationUserTest() {
        
    }

    @Before
    public void setUp() throws Exception {
        this.currentUser = new ApplicationUser();
        this.currentUser.setLastname("Test");
        this.currentUser.setForename("Test");
        this.currentUser = this.userService.save(this.currentUser);
    }

    @Test
    public void saveJob() {
        assertEquals(0, this.currentUser.getJobs().size());
            
        Job job = new Job();
        job.employer = "Scienceroot";
        job.setTitle("Tester");
        job.setUser(this.currentUser);
        
        this.userService.addJobToUser(this.currentUser, job);
        this.currentUser = this.userService.save(this.currentUser);

        assertEquals(1, this.currentUser.getJobs().size());
    }
}