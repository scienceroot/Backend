package com.scienceroot.user.job;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, UUID>{
}
