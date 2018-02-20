package com.scienceroot.industry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scienceroot.user.Job;

@Entity
@Table(name = "industries")
public class Industry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
    private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
}
