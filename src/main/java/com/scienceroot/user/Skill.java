/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.search.SearchResult;
import com.scienceroot.search.Searchable;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author husche
 */
@Entity
@Table(name = "scr_skill")
public class Skill implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid_users")
    @GenericGenerator(name = "uuid_users", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;
    
    @Column(name = "name")
    private String name;
    
    public Skill(){
        
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    
    
    
}
