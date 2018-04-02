/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import com.rometools.rome.feed.synd.SyndEntry;
import java.net.URL;
import java.io.InputStreamReader;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.feed.synd.SyndPerson;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author husche
 */
public class Arxiv {
    
    private SearchParameters fieldNames;

    public Arxiv() {
        fieldNames = new SearchParameters();
        fieldNames.setTitle("ti:");
        fieldNames.setAuthor("au:");
        fieldNames.setAbstract("abs:");
        
    }
    
    public String createQueryString(SearchParameters params){
        //I'm 100% certain there's a better way to do this
        List<String> searchVars = new LinkedList<>();
        if (!"".equals(params.getTitle()) && !"".equals(fieldNames.getTitle()))
            searchVars.add(fieldNames.getTitle() + params.getTitle());
        if (!"".equals(params.getAuthor()) && !"".equals(fieldNames.getAuthor()))
            searchVars.add(fieldNames.getAuthor() + params.getAuthor());
        if (!"".equals(params.getAbstract()) && !"".equals(fieldNames.getAbstract()))
            searchVars.add(fieldNames.getAbstract() + params.getAbstract());
        String query = String.join("+AND+", searchVars);
        return query;
    }

    public List<Paper> runSearch(String url) {
        LinkedList<Paper> papers = new LinkedList<>();
        try {
            URL feedUrl = new URL(url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            //System.out.println(feed);
            
            for (SyndEntry curEntry : feed.getEntries()){
                Paper curPaper = new Paper();
                List<SyndPerson> authors = curEntry.getAuthors();
                List<SyndLink> links = curEntry.getLinks();
                String[] authorsArray = new String[authors.size()];
                String[] linkArray = new String[links.size()];
                for (int i = 0; i < authors.size(); i++){
                    authorsArray[i] = authors.get(i).getName();
                }
                for (int i = 0; i < links.size(); i++){
                    linkArray[i] = links.get(i).getHref();
                }
                curPaper.setAuthor(authorsArray);
                curPaper.setTitle(curEntry.getTitle());
                curPaper.setPublished(curEntry.getPublishedDate());
                curPaper.setUpdated(curEntry.getUpdatedDate());
                curPaper.setLink(linkArray);
                papers.add(curPaper);                
            }
        } catch (Exception e) {
        }
        return papers;
    }

}
