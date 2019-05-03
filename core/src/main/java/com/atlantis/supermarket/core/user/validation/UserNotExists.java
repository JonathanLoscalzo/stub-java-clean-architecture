package com.atlantis.supermarket.core.user.validation;

import com.atlantis.supermarket.core.user.User;
import com.atlantis.supermarket.core.user.exceptions.UsernameNotFoundException;

public class UserNotExists {

    public static void validate(User user, String username) throws UsernameNotFoundException {
	if (user == null) {
            throw new UsernameNotFoundException(username);
        }
    }
}
