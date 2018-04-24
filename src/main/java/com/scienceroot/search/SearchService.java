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
        String arxivUrl = URL_ARXIV + "?search_query=all:" + query;
        String plosUrl = URL_PLOS + "?q=everything:" + query + "&wt=json";
        String crossrefUrl = URL_CROSSREF + "?query=" + query;
        
        arxiv.setUrl(arxivUrl);
        plos.setUrl(plosUrl);
        cr.setUrl(crossrefUrl);
        
        List<Paper> retList = new ArrayList<>();
        retList.addAll(arxiv.runSearch());
        retList.addAll(plos.runSearch());
        retList.addAll(cr.runSearch());
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
        String arxivUrl = URL_ARXIV + "?search_query=" + arxiv.createQueryString(searchParams);
        String plosUrl = URL_PLOS + "?q=" + plos.createQueryString(searchParams);
        String crossrefUrl = URL_CROSSREF + "?" + cr.createQueryString(searchParams);
        
        arxiv.setUrl(arxivUrl);
        plos.setUrl(plosUrl);
        cr.setUrl(crossrefUrl);
        List<Search> searchList = Arrays.asList(arxiv, plos, cr);
        List<Paper> retlist = new ArrayList();
        searchList.parallelStream().map(Search::runSearch).forEachOrdered(v -> retlist.addAll(v));
        
        return retlist;
    }
    
}