package com.scienceroot.search;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    protected static final String URL_ARXIV = "http://export.arxiv.org/api/query";

    public List<Paper> search(String query) {
        Arxiv arxiv = new Arxiv();
        String url = URL_ARXIV + "?search_query=ti:" + query;
        return Arrays.asList(arxiv.runSearch(url));
    }
    
    public List<Paper> searchAdvanced(String query){
        Arxiv arxiv = new Arxiv();
        String url = URL_ARXIV + "?search_query=" + query;
        return Arrays.asList(arxiv.runSearch(url));
    }
}