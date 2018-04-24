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
    private Boolean isOa;
    private String oaLink;

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getPublished() {
        return new Date(published.getTime());
    }

    /**
     *
     * @param published
     */
    public void setPublished(Date published) {
        this.published = new Date(published.getTime());
    }

    /**
     *
     * @return
     */
    public Date getUpdated() {
        return new Date(updated.getTime());
    }

    /**
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
        this.updated = new Date(updated.getTime());
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     */
    public String[] getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String[] author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String[] getLink() {
        return link;
    }

    /**
     *
     * @param link
     */
    public void setLink(String[] link) {
        this.link = link;
    }

    /**
     *
     * @return
     */
    @Override
    public SearchResult toSearchResult() {
        SearchResult result = new SearchResult();
        result.setText(title);
        result.setLink(link[0]);
        return result;
    }

    /**
     *
     * @return
     */
    public String getDOI() {
        return DOI;
    }

    /**
     *
     * @param DOI
     */
    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    /**
     *
     * @return
     */
    public String getJournalName() {
        return journalName;
    }

    /**
     *
     * @param journalName
     */
    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    /**
     *
     * @return
     */
    public int getCitationCount() {
        return citationCount;
    }

    /**
     *
     * @param citationCount
     */
    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    /**
     *
     * @return
     */
    public int getCitationCountEstimate() {
        return citationCountEstimate;
    }

    /**
     *
     * @param citationCountEstimate
     */
    public void setCitationCountEstimate(int citationCountEstimate) {
        this.citationCountEstimate = citationCountEstimate;
    }

    /**
     *
     * @return
     */
    public Boolean getIsOa() {
        return isOa;
    }

    /**
     *
     * @param isOa
     */
    public void setIsOa(Boolean isOa) {
        this.isOa = isOa;
    }

    /**
     *
     * @return
     */
    public String getOaLink() {
        return oaLink;
    }

    /**
     *
     * @param OaLink
     */
    public void setOaLink(String OaLink) {
        this.oaLink = OaLink;
    }
}
