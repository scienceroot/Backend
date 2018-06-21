package com.scienceroot.user.fellowship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.scienceroot.user.ApplicationUser;

import org.springframework.data.repository.CrudRepository;

public interface FellowshipRepository extends CrudRepository<Fellowship, UUID> {

    List<Fellowship> findByFollowedId(UUID followed);
    
    List<Fellowship> findByFollowerId(UUID follows);
    
    List<Fellowship> findByFollowedIdAndFollowerId(UUID followed, UUID follows);
}