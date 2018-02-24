/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.scienceroot.Location;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * https://stackoverflow.com/questions/22256124/cannot-create-a-database-table-named-user-in-postgresql
 */

@Entity
@Table(name = "scr_user")
public class ApplicationUser implements Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
   
    @Id
    @SequenceGenerator(name="scr_user_sequence",sequenceName="scr_user_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="scr_user_sequence")
    @Column(name="id", unique=true, nullable=false)
    @JsonProperty("uid")
    private long id;
    
    @Column
    @JsonProperty("username")
    private String username;
    
    @Column
    @JsonProperty("forename")
    private String forename;
    
    @Column
    @JsonProperty("lastname")
    private String lastname;
    
    @Column
    private String password;

    @Column
    @JsonProperty("mail")
    private String mail;

    //investor/journal
    //@Column(columnDefinition = "text[]")
    //@JsonProperty("roles")
    private String[] roles;

    @Column
    @PrimaryKeyJoinColumn
    private Location location;

    @JsonProperty("jobs")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Job> jobs;

    @Column
    @JsonProperty("skills")
    private String[] skills;

    public void setId(long id) {
        this.id = id;
    }

    @JsonGetter("mail")
    public String getMail() {
        return mail;
    }

    @JsonSetter("mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @JsonGetter("roles")
    public String[] getRoles() {
        return roles;
    }

    @JsonSetter("roles")
    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @JsonGetter("location")
    public Location getLocation() {
        return location;
    }

    @JsonSetter("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    /**public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }**/

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public ApplicationUser() {
    }

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.location = new Location();
    }

    @JsonGetter("uid")
    public long getId() {
        return id;
    }

    @JsonGetter("username")
    public String getUsername() {
        return username;
    }

    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Job> getJobs() {
		return jobs;
	}
    
    public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

}
