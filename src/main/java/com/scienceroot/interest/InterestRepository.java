package com.scienceroot.interest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface InterestRepository extends CrudRepository<Interest, UUID> {

    List<Interest> findByNameContaining(String name);
}
