package com.scienceroot.interest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author husche
 */
public interface InterestRepository extends CrudRepository<Interest, UUID> {

    /**
     *
     * @param name
     * @return
     */
    List<Interest> findByNameContaining(String name);
}
