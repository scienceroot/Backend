
package com.scienceroot.repository;

import java.util.List;
import java.util.UUID;

import com.scienceroot.user.ApplicationUser;

import org.springframework.data.repository.CrudRepository;

public interface RepositoryRepository extends CrudRepository<Repository, UUID>{

    public List<Repository> findByCreator(ApplicationUser creator);    
}
