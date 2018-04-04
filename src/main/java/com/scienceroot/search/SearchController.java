package com.scienceroot.search;

import com.scienceroot.search.preprint.Preprint;
import com.scienceroot.search.preprint.PreprintService;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.language.Language;
import com.scienceroot.user.skill.Skill;
import java.io.IOException;
import java.util.LinkedList;
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
    private PreprintService preprintService;
    private ApplicationUserService applicationUserService;

    @Autowired
    public SearchController(
            SearchService searchService, 
            ApplicationUserService applicationUserService,
            PreprintService preprintService
    ) {
        this.searchService = searchService;
        this.applicationUserService = applicationUserService;
        this.preprintService = preprintService;
    }

	@RequestMapping(value = "", method = RequestMethod.GET)
    public List<SearchResult> search(
            @RequestParam("q") String q,
            @RequestParam(value = "type", defaultValue = "papers") String type
    ) throws IOException {
		List<? extends Searchable> result;
        switch (type) {
			case "users":
				result = searchUsers(q);
				break;
                        case "perprints":
                                result = this.searchPreprints(q);
                                break;
			default:
				result = searchPapers(q);
				break;
        }

		return result.stream()
				.map(Searchable::toSearchResult)
				.collect(toList());
    }

    @RequestMapping(value = "/papers", method = RequestMethod.GET)
    public List<Paper> searchPapers(
            @RequestParam("q") String q
    ) {
		return searchService.search(q);
    }
    
    @RequestMapping(value = "/preprints", method = RequestMethod.GET)
    public List<Preprint> searchPreprints(
            @RequestParam("q") String q
    ) throws IOException {
		return this.preprintService.search(q);
    }
        
        @RequestMapping(value = "papers_advanced", method= RequestMethod.GET)
        public List<Paper> searchPapersAdvanced(
        @RequestParam("ti") String title,
        @RequestParam("au") String author,
        @RequestParam("abs") String abstractt){
            
            SearchParameters params = new SearchParameters();
            if (!"".equals(title))
                params.setTitle(title);
            if (!"".equals(author))
                params.setAuthor(author);
            if (!"".equals(abstractt))
                params.setAbstract(abstractt);
    
            return searchService.searchAdvanced(params);
            
        }

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<ApplicationUser> searchUsers(
            @RequestParam("q") String q
    ) {
		return applicationUserService.search(q);
    }
        
        @RequestMapping(value = "/skills", method = RequestMethod.GET)
        public List<Skill> searchSkills(
        @RequestParam("q") String q){
            return applicationUserService.searchSkill(q);
        }
        
        @RequestMapping(value = "/languages", method = RequestMethod.GET)
        public List<Language> searchLanguages(
        @RequestParam("q") String q){
            return applicationUserService.searchLanguage(q);
        }
        
        
    
}
