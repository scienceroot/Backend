package com.scienceroot.user;

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

@Entity
@Table(name = "jobs")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
    @JsonProperty("id")
	public Long id;
	
	@Column
	public String title;
	
	@JoinColumn
	@OneToOne()
	public ApplicationUser user;
	
	@JoinColumn
	@OneToOne()
	public Industry industry;

}
