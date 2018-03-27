package com.scienceroot.search.preprint;

import com.fasterxml.jackson.databind.JsonNode;
import com.scienceroot.search.SearchResult;
import com.scienceroot.search.Searchable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class Preprint implements Searchable {
    
    public String id;
    
    public String title;
    public String description;
    public List<String> authors;
    
    public List<String> links;
    
    public LocalDateTime created;
    public LocalDateTime modified;
    public LocalDateTime published;

    public Preprint(JsonNode json) {
        this.title = json.get("title").asText();
        this.title = json.get("description").asText();
        
        this.authors = new LinkedList<>();
        for (final JsonNode author: json.get("contributors")) {
            this.authors.add(author.asText());
        }
        
        this.links = new LinkedList<>();
        for (final JsonNode link: json.get("identifiers")) {
            this.links.add(link.asText());
        }
        
        
        if(!json.get("date_modified").isNull()) {
            String modifiedStr = json.get("date_modified").asText();
            this.modified = LocalDateTime.parse(modifiedStr, DateTimeFormatter.ISO_DATE_TIME);
        }
        
        if(!json.get("date").isNull()) {
            String createdStr = json.get("date").asText();
            this.created = LocalDateTime.parse(createdStr, DateTimeFormatter.ISO_DATE_TIME);
        }
        
        if(!json.get("date_published").isNull()) {
            String publishedStr = json.get("date_published").asText();
            this.created = LocalDateTime.parse(publishedStr, DateTimeFormatter.ISO_DATE_TIME);
        }
       
    }

    @Override
    public SearchResult toSearchResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
