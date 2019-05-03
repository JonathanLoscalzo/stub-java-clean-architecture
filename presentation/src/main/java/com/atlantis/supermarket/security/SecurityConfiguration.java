package com.atlantis.supermarket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.atlantis.supermarket.common.parser.Encoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserSecurityService userService;
    
    public SecurityConfiguration(UserSecurityService userService) {
	this.userService = userService;
    }

    /**
     * 
     * - CORS configuration - Set which endpoints are secure and which are publicly
     * available Add our 2 filters into security context Disable session management
     * we don’t need sessions so this will prevent creation of session cookies
     * 
     * configure(HttpSecurity http): a method where we can define which resources
     * are public and which are secured
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.cors().and().csrf().disable();
	http.headers().frameOptions().disable();
	// http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
	// http.authorizeRequests().antMatchers("/api/public/**").permitAll();
	
	http.addFilter(new JwtAuthenticationFilter(authenticationManager()))
		.addFilter(new JwtAuthorizationFilter(authenticationManager()));
	http.authorizeRequests()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/api/public/**").permitAll()
		.antMatchers("/api/private/admin").hasAnyRole("ADMIN")
		.anyRequest().authenticated();
        	
	
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Authentication manager – in our case simple in memory authentication but in
     * real life you’ll need something like UserDetailsService
     * 
     * configure(AuthenticationManagerBuilder auth): a method where we defined a
     * custom implementation of UserDetailsService to load user-specific data in the
     * security framework.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	/*
	 * auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().
	 * encode("password")) .authorities("ROLE_USER");
	 */
	auth.userDetailsService(this.userService).passwordEncoder(Encoder.passwordEncoder());
    }

   

    /**
     * - CORS configuration corsConfigurationSource(): a method where we can
     * allow/restrict our CORS support. In our case we left it wide open by
     * permitting requests from any source (/**).
     * 
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

	return source;
    }
}
