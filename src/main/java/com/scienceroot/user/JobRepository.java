package com.scienceroot.user;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Long>{


    List<Job> findByUserId(long userId);
}
