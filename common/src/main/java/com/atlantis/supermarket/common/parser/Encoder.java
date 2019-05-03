package com.atlantis.supermarket.common.parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Encoder {
    /**
     * Password encoder – in our case bcrypt
     * 
     * @return
     */
    public static PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }
}
