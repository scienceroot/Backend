/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.post;

import com.scienceroot.user.ApplicationUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author svenseemann
 */
public interface PostRepository extends CrudRepository<Post, UUID> {

    public List<Post> findByCreator(ApplicationUser creator);    
}
