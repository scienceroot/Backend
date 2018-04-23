package com.scienceroot.user.skill;

import com.scienceroot.user.job.Job;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author husche
 */
public interface SkillRepository extends CrudRepository<Job, Long>{
    
    /**
     *
     * @param query
     * @return
     */
    @Query("SELECT sk " +
            "FROM Skill sk " +
            "WHERE sk.name like concat('%', :query, '%')")
    List<Skill> search(@Param("query") String query);
}
