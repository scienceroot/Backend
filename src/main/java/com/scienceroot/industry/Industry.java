package com.scienceroot.industry;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "industries")
public class Industry implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid_industries")
    @GenericGenerator(name = "uuid_industries", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
	
    private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

    public UUID getId() {
		return id;
	}
}
