package com.scienceroot.user;

import com.scienceroot.user.skill.Skill;
import com.scienceroot.user.skill.SkillRepository;
import com.scienceroot.user.language.LanguageRepository;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.job.Job;
import com.scienceroot.user.job.JobRepository;
import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.interest.Interest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author husche
 */
@Service
public class ApplicationUserService {

    private Blockchain blockchain;
    private ApplicationUserRepository userRepository;
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private LanguageRepository languageRepository;

    /**
     *
     * @param repository
     * @param jobRepository
     * @param blockchain
     * @param skillRepo
     * @param languageRepository
     */
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

    /**
     *
     * @param query
     * @return
     */
    @Query("SELECT user FROM ApplicationUser user WHERE lower(user.forename) or lower(user.lastname) like lower(concat('%', :query, '%')) ")
    public List<ApplicationUser> search(String query) {
        return userRepository.search(query);
    }
    
    /**
     *
     * @param query
     * @return
     */
    @Query("SELECT sk FROM Skill sk WHERE lower(sk.name) like lower(concat('%', :query, '%')) ")
    public List<Skill> searchSkill(String query){
        return skillRepository.search(query);
    }
    
    /**
     *
     * @param query
     * @return
     */
    
    public List<Language> searchLanguage(String query){
        return languageRepository.search(query);
    }

    /**
     *
     * @param <S>
     * @param s
     * @return
     */
    public <S extends ApplicationUser> S save(S s) {
        return userRepository.save(s);
    }
    
    public Iterable<ApplicationUser> saveAll(Iterable<ApplicationUser> s) {
        return userRepository.save(s);
    }

    /**
     *
     * @param uuid
     * @return
     */
    public ApplicationUser findOne(UUID uuid) {
        return userRepository.findOne(uuid);
    }

    /**
     *
     * @param mail
     * @return
     */
    public Optional<ApplicationUser> findByMail(String mail) {
        return userRepository.findByMail(mail);
    }


    /**
     *
     * @param user
     * @param job
     * @return
     */
    public ApplicationUser addJobToUser(ApplicationUser user, Job job) {
        job.setUser(user);
        jobRepository.save(job);
        
        user.addJob(job);
        
        return user;
    }

    /**
     *
     * @param user
     * @param interest
     * @return
     */
    public ApplicationUser addInterestToUser(ApplicationUser user, Interest interest) {
        user.getInterests().add(interest);
        return user;
    }
    
    /**
     *
     * @param user
     * @param skill
     * @return
     */
    public ApplicationUser addSkillToUser(ApplicationUser user, Skill skill){
        user.getSkills().add(skill);
        return user;
    }
    
    /**
     *
     * @param user
     * @param language
     * @return
     */
    public ApplicationUser addLanguageToUser(ApplicationUser user, Language language){
        user.getLanguages().add(language);
        return user;
    }
    
    /**
     *
     * @param user
     * @param skill
     * @return
     */
    public ApplicationUser removeSkillFromUser(ApplicationUser user, Skill skill){
        user.getSkills().remove(skill);
        return user;
    }
    
    /**
     *
     * @param user
     * @param languageId
     * @return
     */
    public ApplicationUser removeLanguageFromUser(ApplicationUser user, UUID languageId){
        user.getLanguages().removeIf(language -> language.getId().equals(languageId));
        return user;
    }
    
    /**
     *
     * @param user
     * @param jobId
     * @return
     */
    public ApplicationUser removeJobFromUser(ApplicationUser user, UUID jobId){
        user.getJobs().removeIf(job -> job.getId().equals(jobId));
        this.jobRepository.delete(jobId);
        return user;
    }
    
    /**
     *
     * @param user
     * @param interestId
     * @return
     */
    public ApplicationUser removeInterestFromUser(ApplicationUser user, UUID interestId){
        user.getInterests().removeIf(interest -> interest.getId().equals(interestId));
        
        return user;
    }

    /**
     *
     * @param user
     * @param publicKey
     * @return
     */
    public ApplicationUser addPublicKeyToUser(ApplicationUser user, String publicKey) {
        blockchain.sendInitialFunds(publicKey);
        user.setPublicKey(publicKey);

        return user;
    }
    
    /**
     *
     * @param user
     * @param contact
     * @return
     */
    public ApplicationUser addContactToUser(ApplicationUser user, UserContact contact){
        user.setContact(contact);
        return user;
    }
}
