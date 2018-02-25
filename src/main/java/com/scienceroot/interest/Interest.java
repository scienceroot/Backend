/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.interest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.user.ApplicationUser;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author husche
 */
@Entity
@Table(name="interests")
public class Interest implements Serializable{

    @ManyToMany(mappedBy = "interests")
    @JsonIgnore
    public List<ApplicationUser> user;
    
    @Column
    @JsonProperty("name")
    private String name;
    @Id
    @GeneratedValue(generator = "uuid_interests")
    @GenericGenerator(name = "uuid_interests", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
