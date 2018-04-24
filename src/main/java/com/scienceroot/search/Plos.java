/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import com.google.gson.JsonArray;
import java.net.URL;
import java.io.InputStreamReader;
import java.util.LinkedList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

/**
 *
 * @author husche
 */
public class Plos extends Search {

    /**
     *
     */
    public Plos() {
        fieldNames = new SearchParameters();
        fieldNames.setTitle("title:");
        fieldNames.setAuthor("author:");
        fieldNames.setAbstract("abstract:");
    }

    /**
     *
     * @param params
     */
    @Override
    public String createQueryString(SearchParameters params) {
        //I'm 100% certain there's a better way to do this
        List<String> searchVars = new LinkedList<>();
        if (!"".equals(params.getTitle()) && !"".equals(fieldNames.getTitle())) {
            searchVars.add(fieldNames.getTitle() + "\"" + params.getTitle() + "\"");
        }
        if (!"".equals(params.getAuthor()) && !"".equals(fieldNames.getAuthor())) {
            searchVars.add(fieldNames.getAuthor() + "\"" + params.getAuthor() + "\"");
        }
        if (!"".equals(params.getAbstract()) && !"".equals(fieldNames.getAbstract())) {
            searchVars.add(fieldNames.getAbstract() + "\"" + params.getAbstract() + "\"");
        }
        return String.join("%20AND%20", searchVars).concat("&wt=json");

    }

    /**
     *
     * @return
     */
    @Override
    public List<Paper> runSearch() {
        LinkedList<Paper> papers = new LinkedList<>();
        try {
            StringBuilder content = new StringBuilder();
            URL feedUrl = new URL(this.url);
            URLConnection conn = feedUrl.openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            }
            JsonObject mainobj = new JsonParser().parse(content.toString()).getAsJsonObject();
            JsonObject respObj = mainobj.get("response").getAsJsonObject();
            JsonArray docsObj = respObj.get("docs").getAsJsonArray();

            for (int j = 0; j < docsObj.size(); j++) {
                Paper curPaper = new Paper();
                JsonObject currObj = docsObj.get(j).getAsJsonObject();
                JsonArray authors = currObj.get("author_display").getAsJsonArray();

                String[] authorsArray = new String[authors.size()];
                for (int i = 0; i < authors.size(); i++) {
                    authorsArray[i] = authors.get(i).getAsString();
                }

                curPaper.setAuthor(authorsArray);
                curPaper.setTitle(currObj.get("title_display").getAsString());
                curPaper.setDOI(currObj.get("id").getAsString());
                curPaper.setJournalName(currObj.get("journal").getAsString());
                curPaper.setSummary(currObj.get("abstract").getAsString());
                //curPaper.setPublished(currObj.get("publication_date").);
                //curPaper.setUpdated(curEntry.getUpdatedDate());
                //curPaper.setLink(linkArray);
                papers.add(curPaper);
            }
        } catch (JsonSyntaxException | IOException e) {
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
        return papers;
    }

}
