/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

/**
 *
 * @author husche
 */

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LanguageRepository extends CrudRepository<Language, Long>{
    
    @Query("SELECT lang " +
            "FROM Language lang " +
            "WHERE lang.name like concat('%', :query, '%')")
    List<Language> search(@Param("query") String query);
}
