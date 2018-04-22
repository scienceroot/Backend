package com.scienceroot.user;

import com.scienceroot.user.skill.Skill;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.job.Job;
import com.scienceroot.interest.Interest;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private ApplicationUserService userService;

    @Autowired
    public ApplicationUserController(
            ApplicationUserService applicationUserService
    ) {
        this.userService = applicationUserService;
    }

    @GetMapping(value = "/me")
    public ApplicationUser getMe(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {

        String mail = Jwts.parser().setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        return this.userService
                .findByMail(mail)
                .orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApplicationUser getById(
            @PathVariable("id") UUID id
    ) {

        ApplicationUser user = this.userService.findOne(id);

        return Optional.ofNullable(user)
                .orElseThrow(UserNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApplicationUser updateUser(
            @PathVariable("id") UUID id,
            @RequestBody ApplicationUser user
    ) {

        ApplicationUser userToUpdate = getById(id);

        return Optional.ofNullable(userToUpdate)
                .map(tmpUser -> tmpUser = user)
                .map(userService::save)
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/follow/{toFollowId}", method = RequestMethod.POST)
    public ApplicationUser followUser(
            @PathVariable("id") UUID userId,
            @PathVariable("toFollowId") UUID toFollowId
    ) {

        ApplicationUser dbUser = getById(userId);
        ApplicationUser toFollowUser = getById(toFollowId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.followUser(user, toFollowUser))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}/followedBy", method = RequestMethod.GET)
    public List<ApplicationUser> getUserFollowedBy(
            @PathVariable("id") UUID userId
    ) {

        ApplicationUser dbUser = getById(userId);

        return dbUser.getFollowedBy();
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/jobs/{jobId}", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserJob(
            @PathVariable("id") UUID userId,
            @PathVariable("jobId") UUID jobId
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeJobFromUser(user, jobId))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/interests", method = RequestMethod.POST)
    public ApplicationUser addUserInterest(
            @PathVariable("id") UUID userId,
            @RequestBody Interest interest
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addInterestToUser(user, interest))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/skills", method = RequestMethod.POST)
    public ApplicationUser addUserSkill(
            @PathVariable("id") UUID userId,
            @RequestBody Skill skill
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addSkillToUser(user, skill))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/languages", method = RequestMethod.POST)
    public ApplicationUser addUserLanguage(
            @PathVariable("id") UUID userId,
            @RequestBody Language language
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addLanguageToUser(user, language))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/languages/{languageId}", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserLanguage(
            @PathVariable("id") UUID userId,
            @PathVariable("languageId") UUID languageId
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeLanguageFromUser(user, languageId))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/interests/{interestId}", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserInterest(
            @PathVariable("id") UUID userId,
            @PathVariable("interestId") UUID interestId
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeInterestFromUser(user, interestId))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/skills", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserSkill(
            @PathVariable("id") UUID userId,
            @RequestBody Skill skill
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeSkillFromUser(user, skill))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/contact", method = RequestMethod.POST)
    public ApplicationUser updateUserContact(@PathVariable("id") UUID userId,
            @RequestBody UserContact contact){
        
        ApplicationUser dbUser = getById(userId);
        return Optional.ofNullable(dbUser)
                .map(user -> userService.addContactToUser(user, contact))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/publickey", method = RequestMethod.POST)
    public ApplicationUser updateUserPublicKey(
            @PathVariable("id") UUID userId,
            @RequestBody String publicKey
    ) {

        ApplicationUser dbUser = getById(userId);

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addPublicKeyToUser(user, publicKey))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetPassword(
            @RequestBody String mail){
        
        
    }
    
}
