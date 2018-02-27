package com.scienceroot.user;

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

@DataJpaTest
@RunWith(SpringRunner.class)
@PropertySource("test-database.properties")
public class ApplicationUserTest {

    @Autowired
    private TestEntityManager entityManager;
    private ApplicationUser currentUser;

    @Before
    public void setUp() throws Exception {
        this.currentUser = new ApplicationUser();
        this.currentUser.setLastname("Test");
        this.currentUser.setForename("Test");
        this.currentUser = this.entityManager.persistAndFlush(this.currentUser);
    }

    @Test
    public void saveJob() {
        assertNull(this.currentUser.getJobs());

        Job job = new Job();
        job.employer = "Scienceroot";
        job.title = "Tester";
        job.user = this.currentUser;
        job = this.entityManager.persistAndFlush(job);

        List<Job> jobs = new ArrayList<>(1);
        jobs.add(job);

        this.currentUser.setJobs(jobs);
        this.currentUser = this.entityManager.persistAndFlush(this.currentUser);

        assertEquals(1, this.currentUser.getJobs().size());
    }
}