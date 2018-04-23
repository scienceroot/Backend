/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author husche
 */

@Embeddable
public class UserContact implements Serializable{
    
    
    @Column(name = "skype")
    private String skype;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "twitter")
    private String twitter;
    
    /**
     *
     */
    public UserContact(){
    }

    /**
     *
     * @return
     */
    public String getSkype() {
        return skype;
    }

    /**
     *
     * @param skype
     */
    public void setSkype(String skype) {
        this.skype = skype;
    }

    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     *
     * @param twitter
     */
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
    
    
    
}
