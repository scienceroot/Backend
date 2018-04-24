/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author husche
 */
public class CrossRef extends Search {

    /**
     *
     */
    public CrossRef() {
        fieldNames = new SearchParameters();
        fieldNames.setTitle("query.title=");
        fieldNames.setAuthor("query.author=");
    }


    /**
     * @param params
     */
    @Override
    public String createQueryString(SearchParameters params) {
        //I'm 100% certain there's a better way to do this
        List<String> searchVars = new LinkedList<>();
        if (!"".equals(params.getTitle()) && !"".equals(fieldNames.getTitle())) {
            searchVars.add(fieldNames.getTitle() + params.getTitle().replace(' ', '+'));
        }
        if (!"".equals(params.getAuthor()) && !"".equals(fieldNames.getAuthor())) {
            searchVars.add(fieldNames.getAuthor() + params.getAuthor().replace(' ', '+'));
        }
        return String.join("&", searchVars);
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
            JsonObject respObj = mainobj.get("message").getAsJsonObject();
            JsonArray docsObj = respObj.get("items").getAsJsonArray();

            for (int j = 0; j < docsObj.size(); j++) {
                Paper curPaper = new Paper();
                JsonObject currObj = docsObj.get(j).getAsJsonObject();

                curPaper.setTitle(currObj.get("title").getAsString());
                curPaper.setDOI(currObj.get("DOI").getAsString());
                papers.add(curPaper);
            }
        } catch (JsonSyntaxException | IOException e) {
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
        return papers;
    }

}
