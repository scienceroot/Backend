package com.scienceroot.search;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author husche
 */
@Service
public class SearchService {

    protected static final String URL_ARXIV = "http://export.arxiv.org/api/query";
    protected static final String URL_PLOS = "http://api.plos.org/search";
    protected static final String URL_CROSSREF = "https://api.crossref.org/works";

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
        String arxivUrl = URL_ARXIV + "?search_query=ti:" + query;
        String plosUrl = URL_PLOS + "?q=everything:" + query + "&wt=json";
        String crossrefUrl = URL_CROSSREF + "?query=" + query;
        
        List<Paper> retList = new ArrayList<>();
        retList.addAll(arxiv.runSearch(arxivUrl));
        retList.addAll(plos.runSearch(plosUrl));
        retList.addAll(cr.runSearch(crossrefUrl));
        return retList;
    }
    
    /**
     *
     * @param searchParams
     * @return
     */
    public List<Paper> searchAdvanced(SearchParameters searchParams){
        Arxiv arxiv = new Arxiv();
        Plos plos = new Plos();
        CrossRef cr = new CrossRef();
        //TODO: dirty hack for now
        searchParams.correctParameters();
        String arxivurl = URL_ARXIV + "?search_query=" + arxiv.createQueryString(searchParams);
        String plosurl = URL_PLOS + "?q=" + plos.createQueryString(searchParams);
        String crossurl = URL_CROSSREF + "?" + cr.createQueryString(searchParams);
        List<Paper> retList = new ArrayList<>();
        retList.addAll(arxiv.runSearch(arxivurl));
        retList.addAll(plos.runSearch(plosurl));
        retList.addAll(cr.runSearch(crossurl));

        return retList;
    }
    
}