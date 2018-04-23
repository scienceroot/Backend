package com.scienceroot.search;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

    protected static final String URL_ARXIV = "http://export.arxiv.org/api/query";
    protected static final String URL_PLOS = "http://api.plos.org/search";
    protected static final String URL_CROSSREF = "https://api.crossref.org/works";

    public List<Paper> search(String query) {
        Arxiv arxiv = new Arxiv();
        Plos plos = new Plos();
        CrossRef cr = new CrossRef();
        String url = URL_ARXIV + "?search_query=ti:" + query;
        String url2 = URL_PLOS + "?q=everything:" + query + "&wt=json";
        String urlxr = URL_CROSSREF + "?query=" + query;
        
        List<Paper> retList = new ArrayList<>();
        retList.addAll(arxiv.runSearch(url));
        retList.addAll(plos.runSearch(url2));
        retList.addAll(cr.runSearch(urlxr));
        return retList;
    }
    
    public List<Paper> searchAdvanced(SearchParameters searchParams){
        Arxiv arxiv = new Arxiv();
        Plos plos = new Plos();
        CrossRef cr = new CrossRef();
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