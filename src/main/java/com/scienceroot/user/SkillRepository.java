package com.scienceroot.user;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SkillRepository extends CrudRepository<Job, Long>{
    
    @Query("SELECT sk " +
            "FROM Skill sk " +
            "WHERE sk.name like concat('%', :query, '%')")
    List<Skill> search(@Param("query") String query);
}
