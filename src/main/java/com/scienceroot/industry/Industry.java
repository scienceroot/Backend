package com.scienceroot.industry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.scienceroot.user.Job;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "industries")
public class Industry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
    @JsonProperty("id")
    private long id;
	
	@Column
    @JsonProperty("name")
    private String name;
	
	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
}
