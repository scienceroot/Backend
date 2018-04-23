/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import io.jsonwebtoken.lang.Strings;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author husche
 */
public class SearchParameters {

    private final HashMap<String, String> searchProperties;

    /**
     *
     */
    public SearchParameters() {
        this.searchProperties = new HashMap();
    }
    
    /**
     *
     */
    public void correctParameters(){
        searchProperties.replaceAll((k, v) -> v.replaceAll(" ", "%20"));
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        searchProperties.put("title", title);
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        searchProperties.put("author", author);
    }

    /**
     *
     * @param abst
     */
    public void setAbstract(String abst) {
        searchProperties.put("abstract", abst);
    }
    
    /**
     *
     * @return
     */
    public String getTitle() {
        return searchProperties.containsKey("title") ? (String)searchProperties.get("title") : "";
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return searchProperties.containsKey("author") ? (String)searchProperties.get("author") : "";
    }
    
    /**
     *
     * @return
     */
    public String getAbstract(){
        return searchProperties.containsKey("abstract") ? (String)searchProperties.get("abstract") : "";
    }
    
}
