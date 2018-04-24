package com.scienceroot.search;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author husche
 */
@Service
public class SearchService {

    ;
    

    /**
     *
     * @param query
     * @return
     */
    public List<Paper> search(String query) {
        Arxiv arxiv = new Arxiv();
        Plos plos = new Plos();
        CrossRef cr = new CrossRef();
        //TODO: dirty hack for now
        query = query.replaceAll(" ", "%20");
        arxiv.createSimpleUrl(query);
        plos.createSimpleUrl(query);
        cr.createSimpleUrl(query);

        List<Search> searchList = Arrays.asList(arxiv, plos, cr);
        List<Paper> retlist = new ArrayList();
        searchList.parallelStream()
                .map(Search::runSearch)
                .forEachOrdered(v -> retlist.addAll(v));
        return retlist;
    }

    /**
     *
     * @param searchParams
     * @return
     */
    public List<Paper> searchAdvanced(SearchParameters searchParams) {
        Arxiv arxiv = new Arxiv();
        Plos plos = new Plos();
        CrossRef cr = new CrossRef();
        //TODO: dirty hack for now
        searchParams.correctParameters();
        arxiv.createAdvancedUrl(searchParams);
        plos.createAdvancedUrl(searchParams);
        cr.createAdvancedUrl(searchParams);

        List<Search> searchList = Arrays.asList(arxiv, plos, cr);
        List<Paper> retlist = new ArrayList();
        searchList.parallelStream()
                .map(Search::runSearch)
                .forEachOrdered(v -> retlist.addAll(v));

        return retlist;
    }

}
