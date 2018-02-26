/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import com.scienceroot.search.Paper;
import com.scienceroot.user.ApplicationUser;

/**
 *
 * @author husche
 */
public class SearchResult {

    public SearchResult(Paper[] papers, ApplicationUser[] users){
        this.papers = papers;
        this.users = users;
    }
    
    public Paper[] getPapers() {
        return papers;
    }

    public void setPapers(Paper[] papers) {
        this.papers = papers;
    }

    public ApplicationUser[] getUsers() {
        return users;
    }

    public void setUsers(ApplicationUser[] users) {
        this.users = users;
    }
    
    private Paper[] papers;
    private ApplicationUser[] users;
    
}
