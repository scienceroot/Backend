/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import com.scienceroot.user.skill.Skill;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.job.Job;
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
    @Column(name = "id", unique = true, nullable = false)
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
            name = "scr_user_to_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Skill> skills;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
            name = "scr_user_to_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Language> languages;
    
    @ManyToMany()
    @JoinTable(name = "follows",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "follows_id"))
    private List<ApplicationUser> follows;
    
    @ManyToMany()
    @JoinTable(name = "follows",
                joinColumns = @JoinColumn(name = "follows_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<ApplicationUser> followedBy;

    @Column
    @JsonProperty("publicKey")
    private String publicKey = "";

    @Embedded
    private UserContact contact;

    public ApplicationUser() {
    }

    public ApplicationUser(String mail, String password) {
        this.mail = mail;
        this.password = password;
        this.location = new Location();
        this.publicKey = "";
        this.contact = new UserContact();
    }

    @Override
    public SearchResult toSearchResult() {
        SearchResult result = new SearchResult();
        result.setText(lastname + " " + forename);
        return result;
    }

    public ApplicationUser update(ApplicationUser updatedUser) {
        this.setForename(updatedUser.forename);
        this.setLastname(updatedUser.lastname);
        this.setLocation(updatedUser.location);
        this.setMail(updatedUser.mail);
        return this;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public UserContact getContact() {
        return contact;
    }

    public void setContact(UserContact contact) {
        this.contact = contact;
    }

    /**
     * @return the follows
     */
    public List<ApplicationUser> getFollows() {
        return follows;
    }

    /**
     * @param follows the follows to set
     */
    public void setFollows(List<ApplicationUser> follows) {
        this.follows = follows;
    }

    /**
     * @return the followedBy
     */
    @JsonIgnore
    public List<ApplicationUser> getFollowedBy() {
        return followedBy;
    }
}
