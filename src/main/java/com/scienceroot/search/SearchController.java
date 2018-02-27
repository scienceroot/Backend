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

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public List<SearchResult> search(
            @RequestParam("q") String q,
            @RequestParam(value = "type", defaultValue = "papers") String type
    ) {
		List<? extends Searchable> result;
        switch (type) {
			case "users":
				result = searchUsers(q);
				break;
			default:
				result = searchPapers(q);
				break;
        }

		return result.stream()
				.map(Searchable::toSearchResult)
				.collect(toList());
    }

    @RequestMapping(value = "/search/papers", method = RequestMethod.GET)
	public List<Paper> searchPapers(
            @RequestParam("q") String q
    ) {
		return searchService.search(q);
    }

    @RequestMapping(value = "/search/users", method = RequestMethod.GET)
	public List<ApplicationUser> searchUsers(
            @RequestParam("q") String q
    ) {
		return applicationUserService.search(q);
    }
    
}
