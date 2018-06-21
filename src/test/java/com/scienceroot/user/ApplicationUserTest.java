package com.scienceroot.user;

import com.scienceroot.user.job.Job;
import com.scienceroot.util.ApplicationUserHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
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
        this.currentUser = ApplicationUserHelper.getTestUser();
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
        
        ApplicationUser dbUser = this.userService.findOne(this.currentUser.getId());
        assertEquals(1, dbUser.getJobs().size());
    }
}