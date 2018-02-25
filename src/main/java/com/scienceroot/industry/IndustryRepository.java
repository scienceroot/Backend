package com.scienceroot.industry;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends CrudRepository<Industry, Long>{

	Optional<Industry> findByName(String name);
	
	@Query("SELECT i FROM Industry i WHERE i.name LIKE %:name%")
	List<Industry> findByContainsName(@Param("name") String name);
}
