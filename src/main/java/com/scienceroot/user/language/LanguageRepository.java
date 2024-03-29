/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user.language;

/**
 *
 * @author husche
 */

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author husche
 */
public interface LanguageRepository extends CrudRepository<Language, Long>{
    
    /**
     *
     * @param query
     * @return
     */
    @Query("SELECT lang FROM Language lang WHERE lower(lang.name) like concat('%', lower(:query), '%') ")
    List<Language> search(@Param("query") String query);
    
    /**
     *
     * @param name
     * @return
     */
    List<Language> findByNameContaining(String name);
}
