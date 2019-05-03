package com.atlantis.supermarket.core.user.exceptions;

public class UserExistsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String username;
    
    public String getUsername() {
        return username;
    }

    public UserExistsException(String username) {
	this.username = username;
    }

}
