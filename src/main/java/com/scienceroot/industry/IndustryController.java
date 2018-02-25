package com.scienceroot.industry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/industries")
public class IndustryController {

    private IndustryRepository industryRepository;

    public IndustryController(@Autowired IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Industry> getByName(@RequestParam(name = "q") String query) {
        return this.industryRepository.findByContainsName(query);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Industry industry) throws JsonProcessingException {
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
