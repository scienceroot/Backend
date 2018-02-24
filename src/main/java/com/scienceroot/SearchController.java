/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scienceroot.search.Arxiv;
import com.scienceroot.search.Paper;
import com.scienceroot.user.ApplicationUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author husche
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Paper[] search(@RequestParam("q") String q){
        System.out.println("test");
        Arxiv arxiv = new Arxiv();
        String url = "http://export.arxiv.org/api/query?search_query=ti:" + q;
        return arxiv.runSearch(url);
        
    }
    
}
