package com.scienceroot.industry;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.scienceroot.industry.Industry;

public interface IndustryRepository extends CrudRepository<Industry, Long>{

	public Optional<Industry> findByName(String name);
}
