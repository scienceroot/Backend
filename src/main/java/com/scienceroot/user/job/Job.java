package com.scienceroot.user.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.industry.Industry;
import com.scienceroot.user.ApplicationUser;
import java.util.UUID;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(generator = "uuid_users")
    @GenericGenerator(name = "uuid_users", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column
    public String title;
    
    @Column
    public Integer startMonth;
    @Column
    public Integer startYear;
    
    @Column
    public Integer endMonth;
    @Column
    public Integer endYear;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public ApplicationUser user;

    @Column
    public String employer;
    
    @JoinColumn
    @OneToOne()
    public Industry industry;

    public Job() {
    }

    public Job(String title, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear, ApplicationUser user, String employer, Industry industry) {
        this.title = title;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.user = user;
        this.employer = employer;
        this.industry = industry;
    }
    
    

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }
}
