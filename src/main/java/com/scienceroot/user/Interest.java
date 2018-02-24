/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author husche
 */
@Entity
@Table(name="interests")
public class Interest implements Serializable{
    
    @Id
    @SequenceGenerator(name="interests_sequence",sequenceName="interests_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="interests_sequence")
    @Column(name="id", unique=true, nullable=false)
    @JsonProperty("id")
    private long id;
    
    @Column
    @JsonProperty("name")
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public ApplicationUser user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
