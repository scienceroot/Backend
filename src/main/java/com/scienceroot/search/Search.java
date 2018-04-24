/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.util.List;

/**
 *
 * @author husche
 */
public abstract class Search {
    
    protected String url;
    protected SearchParameters fieldNames;

    public void createAdvancedUrl(SearchParameters params) {
    }
    
    public void createSimpleUrl(String query){
    }

    public List<Paper> runSearch() {
        return null;
    }
    
    public void setUrl(String url){
        this.url = url;
    }

}
