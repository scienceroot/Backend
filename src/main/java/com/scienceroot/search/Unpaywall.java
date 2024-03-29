/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author husche
 */
@Entity
@Table(name = "importtable")
public class Unpaywall implements Serializable{

    @Id
    private String doi;

    private String url;

    /**
     *
     */
    public Unpaywall() {
    }

    /**
     *
     * @return
     */
    public String getDoi() {
        return doi;
    }

    /**
     *
     * @param doi
     */
    public void setDoi(String doi) {
        this.doi = doi;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
