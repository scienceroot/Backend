package com.scienceroot.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    private RepositoryRepository repositoryRepository;

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }
    
    public Repository save(Repository s) {
        return this.repositoryRepository.save(s);
    }
    
    
    public Optional<Repository> findOne(UUID id) {
        return Optional.ofNullable(this.repositoryRepository.findOne(id));
    }
}
