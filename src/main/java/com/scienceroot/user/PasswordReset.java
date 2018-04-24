/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.user;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;

/**
 *
 * @author husche
 */
public class PasswordReset {

    private final Configuration config;

    /**
     *
     */
    public PasswordReset() {
        this.config = new Configuration()
                .apiKey("key-3c14d95e94ff3515c69e2a9199d1a127")
                .domain("sandbox6fcc129c89bf4b5390715241f60d9c33.mailgun.org")
                .from("Scienceroot", "frederik.huschebeck@scienceroot.com");

    }

    /**
     *
     * @param mail
     * @return
     */
    public Boolean sendPasswordMail(String mail) {
        Response resp = Mail.using(config)
                .to(mail)
                .subject("Password reset")
                .text("forgotten password")
                .build()
                .send();
        return resp.isOk();
    }

}
