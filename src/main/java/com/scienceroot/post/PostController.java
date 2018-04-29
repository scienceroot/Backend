package com.scienceroot.post;

import com.scienceroot.security.ActionForbiddenException;
import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.ResourceNotFoundException;
import com.scienceroot.user.UserNotFoundException;
import io.jsonwebtoken.Jwts;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenseemann
 */
@CrossOrigin
@RestController
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;
    
    private final ApplicationUserService userService;
    
    /**
     *
     * @param postService
     */
    @Autowired
    public PostController(
            PostService postService,
            ApplicationUserService userService
    ) {
        this.postService = postService;
        this.userService = userService;
    }
    
    /**
     *
     * @param token
     * @param toCreate
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Post create(
            @RequestHeader("Authorization") String token,
            @RequestBody Post toCreate
    ) {
        String mail = this.getJwtUserMail(token);
        
        if(!toCreate.getCreator().getMail().equals(mail)) {
            throw new ActionForbiddenException();
        }
        
        return this.postService.save(toCreate);
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Post delete(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID postId
    ) {
        String mail = this.getJwtUserMail(token);
        Optional<Post> toDelete = this.postService.findById(postId);
        
        if(!toDelete.isPresent()) {
            throw new ResourceNotFoundException();
        }
        
        if(!toDelete.get().getCreator().getMail().equals(mail)) {
            throw new ActionForbiddenException();
        } else {
            this.postService.delete(toDelete.get());
        }
        
        return toDelete.get();
    }

    /**
     *
     * @param userId
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<Post> byUser(
          @PathVariable(name = "userId") UUID userId
    ) {
        List<Post> posts = this.postService.getByUser(userId);
        
        return posts;
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
