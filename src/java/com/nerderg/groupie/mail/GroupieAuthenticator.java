/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nerderg.groupie.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author pmcneil
 */
public class GroupieAuthenticator extends Authenticator{

    private String password;
    private String username;

    public GroupieAuthenticator(String username, String password) {
        this.password = password;
        this.username = username;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }


}
