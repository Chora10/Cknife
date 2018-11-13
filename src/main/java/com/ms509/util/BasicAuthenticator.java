package com.ms509.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

class BasicAuthenticator extends Authenticator {
    String username;
    String password;

    public BasicAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}