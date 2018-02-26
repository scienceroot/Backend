package com.scienceroot.user;

import com.scienceroot.interest.Interest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserService {

    private ApplicationUserRepository repository;
    private JobRepository jobRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository repository) {
        this.repository = repository;
    }

    @Query("SELECT user FROM ApplicationUser user WHERE user.forename or user.lastname like concat('%', :query, '%') ")
    public List<ApplicationUser> search(String query) {
        return repository.search(query);
    }

    public <S extends ApplicationUser> S save(S s) {
        return repository.save(s);
    }

    public ApplicationUser findOne(UUID uuid) {
        return repository.findOne(uuid);
    }

    public Optional<ApplicationUser> findByMail(String mail) {
        return repository.findByMail(mail);
    }

    public ApplicationUser addJobToUser(ApplicationUser user, Job job) {
        job.user = user;
        jobRepository.save(job);
        return user;
    }

    public ApplicationUser addInterestToUser(ApplicationUser user, Interest interest) {
        user.getInterests().add(interest);
        return user;
    }
}
