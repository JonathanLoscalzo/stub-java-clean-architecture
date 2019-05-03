package com.atlantis.supermarket.core.user.exceptions;

public class UsernameNotFoundException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String username;

    public UsernameNotFoundException(String username) {
	this.username = username;
    }

    public String getUsername() {
	return this.username;
    }

}
