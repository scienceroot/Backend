package com.scienceroot.industry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.scienceroot.industry.Industry;

public interface IndustryRepository extends CrudRepository<Industry, Long>{

	public Optional<Industry> findByName(String name);
	
	@Query("SELECT i FROM Industry i WHERE i.name LIKE %:name%")
	public Optional<List<Industry>> findByContainsName(@Param("name") String name);
}
