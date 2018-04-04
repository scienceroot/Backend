/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author husche
 */
public class SearchParameters {

    private final Map searchProperties;

    public SearchParameters() {
        this.searchProperties = new HashMap();
    }

    public void setTitle(String title) {
        searchProperties.put("title", title);
    }

    public void setAuthor(String author) {
        searchProperties.put("author", author);
    }

    public void setAbstract(String abst) {
        searchProperties.put("abstract", abst);
    }
    
    public String getTitle() {
        return searchProperties.containsKey("title") ? (String)searchProperties.get("title") : "";
    }

    public String getAuthor() {
        return searchProperties.containsKey("author") ? (String)searchProperties.get("author") : "";
    }
    
    public String getAbstract(){
        return searchProperties.containsKey("abstract") ? (String)searchProperties.get("abstract") : "";
    }
    
}
