/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;
import java.util.Date;
/**
 *
 * @author husche
 */
public class Paper implements Searchable {
    
    //roughly based on https://docs.microsoft.com/en-us/azure/cognitive-services/academic-knowledge/paperentityattributes
    private String id;
    private Date published;
    private Date updated;
    private String DOI;
    private String title;
    private String summary;
    private String journalName;
    private String[] author;
    private String[] link;
    private int citationCount;
    private int citationCountEstimate;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String[] getLink() {
        return link;
    }

    public void setLink(String[] link) {
        this.link = link;
    }


    @Override
    public SearchResult toSearchResult() {
        SearchResult result = new SearchResult();
        result.setText(title);
        result.setLink(link[0]);
        return result;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public int getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public int getCitationCountEstimate() {
        return citationCountEstimate;
    }

    public void setCitationCountEstimate(int citationCountEstimate) {
        this.citationCountEstimate = citationCountEstimate;
    }
}
