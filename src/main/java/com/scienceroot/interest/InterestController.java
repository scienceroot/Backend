package com.scienceroot.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author husche
 */
@CrossOrigin
@RestController
@RequestMapping("/interests")
public class InterestController {

    private InterestRepository interestRepository;

    /**
     *
     * @param interestRepository
     */
    public InterestController(@Autowired InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    /**
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Interest> getByName(@RequestParam(name = "q") String query) {
        return this.interestRepository.findByNameContaining(query);
    }
}
