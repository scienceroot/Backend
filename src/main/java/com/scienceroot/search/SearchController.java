package com.scienceroot.search;

import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author husche
 */
@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;
    private ApplicationUserService applicationUserService;

    @Autowired
    public SearchController(SearchService searchService, ApplicationUserService applicationUserService) {
        this.searchService = searchService;
        this.applicationUserService = applicationUserService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<SearchResult> search(
            @RequestParam("q") String q,
            @RequestParam(value = "type", defaultValue = "papers") String type
    ) {
        switch (type) {
            case "users":
                return searchUsers(q);
            default:
                return searchPapers(q);
        }
    }

    @RequestMapping(value = "/search/papers", method = RequestMethod.GET)
    public List<SearchResult> searchPapers(
            @RequestParam("q") String q
    ) {
        return searchService.search(q)
                .stream()
                .map(Paper::toSearchResult)
                .collect(toList());
    }

    @RequestMapping(value = "/search/users", method = RequestMethod.GET)
    public List<SearchResult> searchUsers(
            @RequestParam("q") String q
    ) {
        return applicationUserService.search(q)
                .stream()
                .map(ApplicationUser::toSearchResult)
                .collect(toList());
    }
    
}
