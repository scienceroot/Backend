/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author husche
 */
public class SearchFilter {

    public SearchFilter(){
        
    }
    
    public static List<Paper> filter(List<Paper> toFilter, int maxResults){
        
        HashMap<Integer, List<Paper>> importance = new HashMap();
        List<Paper> retList = new LinkedList<>();
        for (int i = 0; i < 20; i++){
            importance.put(i, new LinkedList<>());
        }
        
        for (int i = 0; i < toFilter.size(); i++){
            int imp = 0;
            Paper currPaper = toFilter.get(i);
            if (null == currPaper || null == currPaper.getLink())
                continue;
            if (currPaper.getLink().length > 0)
                imp += 5;
            if (!"".equals(currPaper.getDOI()))
                imp++;
            if (!"".equals(currPaper.getSummary()))
                imp++;
            if (!"".equals(currPaper.getTitle()))
                imp++;
            if (null != currPaper.getPublished())
                imp++;
            List ll = importance.get(imp);
            ll.add(currPaper);
            
            importance.put(imp, ll);
        }
        
        for (int n = importance.size(); n > 0 ; n--){
            List<Paper> currList = importance.get(n);
            if (null == currList)
                continue;
            for (Paper currPape : currList){
                if (retList.size() < maxResults){
                    retList.add(currPape);
                }
            }
            
        }
        return retList;
    }
    
}
