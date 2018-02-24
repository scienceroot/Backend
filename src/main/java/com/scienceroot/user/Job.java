package com.scienceroot.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.industry.Industry;
import com.scienceroot.user.ApplicationUser;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @SequenceGenerator(name="scr_user_sequence",sequenceName="scr_user_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="scr_user_sequence")
    @Column(name="id", unique=true, nullable=false)
    @JsonProperty("id")
    public Long id;

    @Column
    public String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public ApplicationUser user;

    @Column
    public String employer;
    
    @JoinColumn
    @OneToOne()
    public Industry industry;

}
