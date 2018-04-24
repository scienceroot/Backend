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

/**
 *
 * @author husche
 */
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(generator = "uuid_users")
    @GenericGenerator(name = "uuid_users", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column
    private String title;
    
    @Column
    private Integer startMonth;

    @Column
    private Integer startYear;
    
    @Column
    private Integer endMonth;

    @Column
    private Integer endYear;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private ApplicationUser user;

    /**
     *
     */
    @Column
    public String employer;
    
    /**
     *
     */
    @JoinColumn
    @OneToOne()
    public Industry industry;

    /**
     *
     */
    public Job() {
    }

    /**
     *
     * @param title
     * @param startMonth
     * @param startYear
     * @param endMonth
     * @param endYear
     * @param user
     * @param employer
     * @param industry
     */
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
    
    /**
     *
     * @return
     */
    public UUID getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getEmployer() {
        return employer;
    }

    /**
     *
     * @param employer
     */
    public void setEmployer(String employer) {
        this.employer = employer;
    }

    /**
     *
     * @return
     */
    public Industry getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     */
    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }
}
