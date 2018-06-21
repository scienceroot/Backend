package com.scienceroot.user.fellowship;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.user.ApplicationUser;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Fellowship implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid_fellowships")
    @GenericGenerator(
        name = "uuid_fellowships", 
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
        name = "id", 
        unique = true, 
        nullable = false
    )
    @JsonProperty("id")
    private UUID id;

    @OneToOne()
    @JoinColumn(
        name = "followed_id"
    )
    private ApplicationUser followed;

    @OneToOne()
    @JoinColumn(
        name = "follower_id"
    )
    private ApplicationUser follower;

    public Fellowship() {

    }   
    
    public Fellowship(ApplicationUser followed, ApplicationUser follower) {
        this.followed = followed;
        this.follower = follower;
    }   

    /**
     * @return the followed
     */
    public ApplicationUser getFollowed() {
        return followed;
    }

    /**
     * @return the follower
     */
    public ApplicationUser getFollower() {
        return follower;
    }
}