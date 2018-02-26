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
import com.scienceroot.interest.Interest;
import com.scienceroot.search.SearchResult;
import com.scienceroot.search.Searchable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * https://stackoverflow.com/questions/22256124/cannot-create-a-database-table-named-user-in-postgresql
 */
@Entity
@Table(name = "scr_user")
public class ApplicationUser implements Serializable, Searchable {

    private static final long serialVersionUID = 1L;
   
    @Id
    @GeneratedValue(generator = "uuid_users")
    @GenericGenerator(name = "uuid_users", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id", unique=true, nullable=false)
    @JsonProperty("uid")
    private UUID id;
    
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

    private String[] roles;

    @Column
    private Location location;

    @JsonProperty("jobs")
    @OneToMany(mappedBy = "user")
    private List<Job> jobs;

    @JsonProperty("interests")
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "scr_user_to_interest",
            joinColumns = @JoinColumn(name = "interest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Interest> interests;

    @Column
    @JsonProperty("skills")
    private String[] skills;

    public ApplicationUser() {
    }

    public ApplicationUser(String mail, String password) {
        this.mail = mail;
        this.password = password;
        this.location = new Location();
    }

    @Override
    public SearchResult toSearchResult() {
        SearchResult result = new SearchResult();
        result.setText(lastname + " " + forename);
        return result;
    }

    @JsonGetter("uid")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
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

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }
}
