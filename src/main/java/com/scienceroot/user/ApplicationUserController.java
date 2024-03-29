package com.scienceroot.user;

import com.scienceroot.security.ActionForbiddenException;
import com.scienceroot.user.skill.Skill;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.fellowship.Fellowship;
import com.scienceroot.user.fellowship.FellowshipService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author husche
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    private final ApplicationUserService userService;
    private final FellowshipService fellowshipService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     *
     * @param applicationUserService
     * @param bCryptPasswordEncoder
     */
    @Autowired
    public ApplicationUserController(
            ApplicationUserService applicationUserService,
            FellowshipService fellowshipService,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userService = applicationUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fellowshipService = fellowshipService;
    }

    /**
     *
     * @param token
     * @return
     */
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

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApplicationUser getById(
            @PathVariable("id") UUID id
    ) {
        ApplicationUser user = this.userService.findOne(id);

        return Optional.ofNullable(user)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     *
     * @param token
     * @param id
     * @param user
     * @return
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApplicationUser updateUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID id,
            @RequestBody ApplicationUser user
    ) {
        String tokenUserMail = this.getJwtUserMail(token);
        ApplicationUser dbUser = this.getById(id);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }
        
        return Optional.ofNullable(dbUser)
                .map(oldUser -> oldUser.update(user))
                .map(tmpUser -> this.userService.save(tmpUser))
                .orElseThrow(UserNotFoundException::new);
    }
    
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/setPassword", method = RequestMethod.POST)
    public ApplicationUser updateUserPassword(
            @RequestHeader("Authorization") String token,
            @RequestBody String password
    ) {
        ApplicationUser dbUser = this.getMe(token);
        
        String passwordHashed = bCryptPasswordEncoder.encode(password);
        dbUser.setPassword(passwordHashed);
        
        return Optional.ofNullable(dbUser)
                .map(tmpUser -> this.userService.save(tmpUser))
                .orElseThrow(UserNotFoundException::new);
    }
    
    
    
    
    /**
     *
     * @param token
     * @param userId
     * @param toFollowId
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/follow/{toFollowId}", method = RequestMethod.POST)
    public Fellowship followUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @PathVariable("toFollowId") UUID toFollowId
    ) {
        ApplicationUser toFollowUser = getById(toFollowId);
        ApplicationUser dbUser = this.getById(userId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> fellowshipService.follow(toFollowUser, user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param token
     * @param userId
     * @param isFollowingId
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}/isFollowing/{isFollowingId}", method = RequestMethod.GET)
    public ApplicationUser isFollowingUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @PathVariable("isFollowingId") UUID isFollowingId
    ) {
        ApplicationUser followed = getById(isFollowingId);
        ApplicationUser follower = this.getById(userId);
        
        if(!this.fellowshipService.isFollowing(followed, follower)) {
            throw new ResourceNotFoundException();
        }

        return Optional.ofNullable(followed)
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param token
     * @param userId
     * @param toUnfollowId
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/unfollow/{toUnfollowId}", method = RequestMethod.DELETE)
    public ApplicationUser unfollowUser(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @PathVariable("toUnfollowId") UUID toUnfollowId
    ) {
        ApplicationUser follower = this.getById(userId);
        ApplicationUser followed = getById(toUnfollowId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(follower.getMail())) {
            throw new ActionForbiddenException();
        }

        this.fellowshipService.unfollow(followed, follower);

        return followed;
    }
    
    /**
     *
     * @param userId
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}/follows", method = RequestMethod.GET)
    public List<Fellowship> getUserFollows(
            @PathVariable("id") UUID userId
    ) {
        ApplicationUser dbUser = this.getById(userId);

        return this.fellowshipService.getFollows(dbUser);
    }
    
    /**
     *
     * @param userId
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}/followedBy", method = RequestMethod.GET)
    public List<Fellowship> getUserFollowedBy(
            @PathVariable("id") UUID userId
    ) {
        ApplicationUser dbUser = this.getById(userId);

        return this.fellowshipService.getFollowers(dbUser);
    }
    
    /**
     *
     * @param token
     * @param userId
     * @param job
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.POST)
    public ApplicationUser addUserJob(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @RequestBody Job job
    ) {
        String tokenUserMail = this.getJwtUserMail(token);
        ApplicationUser dbUser = this.getById(userId);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addJobToUser(user, job))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param token
     * @param userId
     * @param jobId
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/jobs/{jobId}", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserJob(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @PathVariable("jobId") UUID jobId
    ) {
        ApplicationUser dbUser = this.getById(userId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeJobFromUser(user, jobId))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     *
     * @param token
     * @param userId
     * @param interest
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/interests", method = RequestMethod.POST)
    public ApplicationUser addUserInterest(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @RequestBody Interest interest
    ) {
        ApplicationUser dbUser = this.getById(userId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addInterestToUser(user, interest))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param userId
     * @param skill
     * @return
     */
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
    
    /**
     *
     * @param token
     * @param userId
     * @param language
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/languages", method = RequestMethod.POST)
    public ApplicationUser addUserLanguage(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @RequestBody Language language
    ) {
        ApplicationUser dbUser = getById(userId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> userService.addLanguageToUser(user, language))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param token
     * @param userId
     * @param languageId
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/languages/{languageId}", method = RequestMethod.DELETE)
    public ApplicationUser deleteUserLanguage(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @PathVariable("languageId") UUID languageId
    ) {
        ApplicationUser dbUser = getById(userId);
        String tokenUserMail = this.getJwtUserMail(token);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }

        return Optional.ofNullable(dbUser)
                .map(user -> userService.removeLanguageFromUser(user, languageId))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }
    
    /**
     *
     * @param userId
     * @param interestId
     * @return
     */
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
    
    /**
     *
     * @param userId
     * @param skill
     * @return
     */
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
    
    /**
     *
     * @param token
     * @param userId
     * @param contact
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/contact", method = RequestMethod.POST)
    public ApplicationUser updateUserContact(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID userId,
            @RequestBody UserContact contact
    ){
        String tokenUserMail = this.getJwtUserMail(token);
        ApplicationUser dbUser = this.getById(userId);
        
        if(!tokenUserMail.equals(dbUser.getMail())) {
            throw new ActionForbiddenException();
        }
        
        return Optional.ofNullable(dbUser)
                .map(user -> userService.addContactToUser(user, contact))
                .map(user -> userService.save(user))
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     *
     * @param userId
     * @param publicKey
     * @return
     */
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
    
    /**
     *
     * @param mail
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetPassword(
            @RequestBody String mail){
        PasswordReset pr = new PasswordReset();
        pr.sendPasswordMail(mail);
        
        
    }
    
    private String getJwtUserMail(String token) {
        String mail = Jwts.parser()
            .setSigningKey(SECRET.getBytes())
            .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
            .getBody()
            .getSubject();
        
        return mail;
    }
    
}
