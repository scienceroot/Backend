package com.scienceroot.post;

import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenseemann
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    
    private final ApplicationUserService userService;
    
    /**
     *
     * @param postRepository
     * @param userService
     */
    @Autowired
    public PostService(
            PostRepository postRepository,
            ApplicationUserService userService
    ) {
        this.postRepository = postRepository;
        this.userService = userService;
    }
    
    /**
     *
     * @param <S>
     * @param s
     * @return
     */
    public <S extends Post> S save(S s) {
        return this.postRepository.save(s);
    }
    
    /**
     *
     * @param creatorId
     * @return
     */
    public List<Post> getByUser(UUID creatorId) {
        ApplicationUser dbUser = this.userService.findOne(creatorId);
        
        return this.postRepository.findByCreator(dbUser);
    }
}
