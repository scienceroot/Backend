package com.scienceroot.industry;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/industries")
@CrossOrigin
public class IndustryController {

	private IndustryRepository industryRepository;
	
	public IndustryController(@Autowired IndustryRepository industryRepository) {
		this.industryRepository = industryRepository;
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Industry getByName(@RequestParam(name = "q") String query) {
		return null;
		
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity getByName(@RequestBody Industry industry) throws JsonProcessingException {
		if(industry.getName() != null) {
			Optional<Industry> dbIndustry = this.industryRepository.findByName(industry.getName());
			
			if(!dbIndustry.isPresent()) {
				this.industryRepository.save(industry);
				
				String userStr = new ObjectMapper().writeValueAsString(industry);
				
				return ResponseEntity.status(HttpStatus.CREATED).body(userStr);
				
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Industry already exists");
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing name property");
		}
	}
}
