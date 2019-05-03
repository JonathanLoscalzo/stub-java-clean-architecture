package com.atlantis.supermarket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.atlantis.supermarket.core.user.UserService;


@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
	try {
	    return new UserPrincipal(userService.findUserByUsername(username));
	} catch (com.atlantis.supermarket.core.user.exceptions.UsernameNotFoundException e) {
	    throw new UsernameNotFoundException(e.getUsername());
	}
    }
}