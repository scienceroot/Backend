/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.net.URL;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.feed.synd.SyndPerson;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author husche
 */
public class Arxiv extends Search {

    protected static final String URL_ARXIV = "http://export.arxiv.org/api/query";

    /**
     *
     */
    public Arxiv() {
        fieldNames = new SearchParameters();
        fieldNames.setTitle("ti:");
        fieldNames.setAuthor("au:");
        fieldNames.setAbstract("abs:");

    }

    /**
     *
     * @param params
     */
    @Override
    public void createAdvancedUrl(SearchParameters params) {
        //I'm 100% certain there's a better way to do this

        List<String> searchVars = new LinkedList<>();
        if (!"".equals(params.getTitle()) && !"".equals(fieldNames.getTitle())) {
            searchVars.add(fieldNames.getTitle() + params.getTitle());
        }
        if (!"".equals(params.getAuthor()) && !"".equals(fieldNames.getAuthor())) {
            searchVars.add(fieldNames.getAuthor() + params.getAuthor());
        }
        if (!"".equals(params.getAbstract()) && !"".equals(fieldNames.getAbstract())) {
            searchVars.add(fieldNames.getAbstract() + params.getAbstract());
        }
        this.url = URL_ARXIV + "?search_query=" + String.join("+AND+", searchVars) + "&max_results=" + this.maxResults;
    }

    @Override
    public void createSimpleUrl(String query) {
        this.url = URL_ARXIV + "?search_query=all:" + query + "&max_results=" + this.maxResults;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Paper> runSearch() {
        LinkedList<Paper> papers = new LinkedList<>();
        try {
            URL feedUrl = new URL(this.url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            feed.getEntries().stream().map((curEntry) -> {
                Paper curPaper = new Paper();
                List<SyndPerson> authors = curEntry.getAuthors();
                List<SyndLink> links = curEntry.getLinks();
                String[] authorsArray = new String[authors.size()];
                String[] linkArray = new String[links.size()];
                for (int i = 0; i < authors.size(); i++) {
                    authorsArray[i] = authors.get(i).getName();
                }
                for (int i = 0; i < links.size(); i++) {
                    linkArray[i] = links.get(i).getHref();
                    if ("doi".equals(links.get(i).getTitle()) && linkArray[i].contains("http://dx.doi.org/")) {
                        //arxiv also sends arxiv:doi with the doi, but SyndFeed doesn't
                        //seem to parse this
                        curPaper.setDOI(linkArray[i].substring("http://dx.doi.org/".length()));
                    }
                }
                curPaper.setAuthor(authorsArray);
                curPaper.setTitle(curEntry.getTitle());
                curPaper.setPublished(curEntry.getPublishedDate());
                curPaper.setUpdated(curEntry.getUpdatedDate());
                curPaper.setLink(linkArray);
                curPaper.setSummary(curEntry.getDescription().getValue());
                return curPaper;
            }).forEachOrdered((curPaper) -> {
                papers.add(curPaper);
            });
        } catch (FeedException | IOException | IllegalArgumentException e) {
        }
        return papers;
    }

}
