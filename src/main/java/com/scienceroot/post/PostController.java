package com.scienceroot.post;

import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    /**
     *
     * @param postService
     */
    @Autowired
    public PostController(
            PostService postService
    ) {
        this.postService = postService;
    }
    
    /**
     *
     * @param toCreate
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Post create(
            @RequestBody Post toCreate
    ) {
        return this.postService.save(toCreate);
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
}
