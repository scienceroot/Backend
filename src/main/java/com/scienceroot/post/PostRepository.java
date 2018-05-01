package com.scienceroot.post;

import com.scienceroot.user.ApplicationUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, UUID> {

    /**
     *
     * @param creator
     * @return
     */
    public List<Post> findByCreator(ApplicationUser creator);    
    
    public List<Post> findByCreatorIn(List<ApplicationUser> creator);    
}
