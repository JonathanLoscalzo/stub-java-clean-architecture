package com.atlantis.supermarket.core.user.validation;

import com.atlantis.supermarket.core.user.User;
import com.atlantis.supermarket.core.user.exceptions.UserExistsException;

public class UserExists {

    public static void validate(User user) throws UserExistsException {
	if (user != null) {
	    throw new UserExistsException(user.getUsername());
	}
    }
}
