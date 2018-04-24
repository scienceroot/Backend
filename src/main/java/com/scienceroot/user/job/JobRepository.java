package com.scienceroot.user.job;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author husche
 */
public interface JobRepository extends CrudRepository<Job, UUID>{
}
