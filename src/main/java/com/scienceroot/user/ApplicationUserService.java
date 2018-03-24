package com.scienceroot.user;

import com.scienceroot.user.skill.Skill;
import com.scienceroot.user.skill.SkillRepository;
import com.scienceroot.user.language.LanguageRepository;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.job.Job;
import com.scienceroot.user.job.JobRepository;
import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.interest.Interest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserService {

    private Blockchain blockchain;
    private ApplicationUserRepository userRepository;
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private LanguageRepository languageRepository;

    @Autowired
    public ApplicationUserService(
            ApplicationUserRepository repository, 
            JobRepository jobRepository, 
            Blockchain blockchain,
            SkillRepository skillRepo, 
            LanguageRepository languageRepository
    ) {
        this.userRepository = repository;
        this.jobRepository = jobRepository;
        this.blockchain = blockchain;
        this.skillRepository = skillRepo;
        this.languageRepository = languageRepository;
    }

    @Query("SELECT user FROM ApplicationUser user WHERE user.forename or user.lastname like concat('%', :query, '%') ")
    public List<ApplicationUser> search(String query) {
        return userRepository.search(query);
    }
    
    @Query("SELECT sk FROM Skill sk WHERE sk.name like concat('%', :query, '%') ")
    public List<Skill> searchSkill(String query){
        return skillRepository.search(query);
    }
    
    @Query("SELECT lang FROM Language lang WHERE lang.name like concat('%', :query, '%') ")
    public List<Language> searchLanguage(String query){
        return languageRepository.search(query);
    }

    public <S extends ApplicationUser> S save(S s) {
        return userRepository.save(s);
    }

    public ApplicationUser findOne(UUID uuid) {
        return userRepository.findOne(uuid);
    }

    public Optional<ApplicationUser> findByMail(String mail) {
        return userRepository.findByMail(mail);
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
    
    public ApplicationUser addSkillToUser(ApplicationUser user, Skill skill){
        user.getSkills().add(skill);
        return user;
    }
    
    public ApplicationUser addLanguageToUser(ApplicationUser user, Language language){
        user.getLanguages().add(language);
        return user;
    }
    
    public ApplicationUser removeSkillFromUser(ApplicationUser user, Skill skill){
        user.getSkills().remove(skill);
        return user;
    }
    
    public ApplicationUser removeLanguageFromUser(ApplicationUser user, UUID languageId){
        user.getLanguages().removeIf(language -> language.getId().equals(languageId));
        return user;
    }
    
    public ApplicationUser removeJobFromUser(ApplicationUser user, UUID jobId){
        user.getJobs().removeIf(job -> job.getId().equals(jobId));
        return user;
    }
    
    public ApplicationUser removeInterestFromUser(ApplicationUser user, UUID interestId){
        user.getInterests().removeIf(interest -> interest.getId().equals(interestId));
        return user;
    }

    public ApplicationUser addPublicKeyToUser(ApplicationUser user, String publicKey) {
        if (null == user) {
            return user;
        }
        if ("".equals(user.getPublicKey())) {
            blockchain.sendInitialFunds(publicKey);
        }
        user.setPublicKey(publicKey);
        return user;
    }
}
