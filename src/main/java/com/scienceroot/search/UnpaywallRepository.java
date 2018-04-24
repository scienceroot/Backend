/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author husche
 */
public interface UnpaywallRepository extends CrudRepository<Unpaywall, String>{
    
    /**
     *
     * @param doi
     * @return
     */
    @Query("select url from Unpaywall t where doi = :doi")
    String findOpenAccessLink(@Param("doi") String doi);
    
}
