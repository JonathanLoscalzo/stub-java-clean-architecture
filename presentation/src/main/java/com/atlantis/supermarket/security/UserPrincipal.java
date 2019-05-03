package com.atlantis.supermarket.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.atlantis.supermarket.core.user.User;


public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
	this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return this.user.getRoles().stream().map(x -> new SimpleGrantedAuthority(x.toString()))
		.collect(Collectors.toList());
    }
    
    public User getUser() {
	return this.user;
    }

    @Override
    public String getPassword() {
	return this.user.getPassword();
    }

    @Override
    public String getUsername() {
	return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public boolean isEnabled() {
	// TODO Auto-generated method stub
	return true;
    }
}