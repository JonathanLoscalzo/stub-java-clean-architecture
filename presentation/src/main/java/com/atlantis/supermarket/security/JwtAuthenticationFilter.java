package com.atlantis.supermarket.security;

import java.io.BufferedReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.atlantis.supermarket.common.parser.JsonParser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * If username and password is correct, then filter will create a JWT token and
 * returns it in HTTP Authorization header.
 * https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 * 
 * @author jloscalzo
 *
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
	this.authenticationManager = authenticationManager;
	
	setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    private String getRequestBody(final HttpServletRequest request) {
	final StringBuilder builder = new StringBuilder();
	try (BufferedReader reader = request.getReader()) {
	    if (reader == null) {
		logger.debug("Request body could not be read because it's empty.");
		return null;
	    }
	    String line;
	    while ((line = reader.readLine()) != null) {
		builder.append(line);
	    }
	    return builder.toString();
	} catch (final Exception e) {
	    logger.trace("Could not obtain the saml request body from the http request", e);
	    return null;
	}
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	    throws AuthenticationException {
	String pwd = "";
	String username = request.getParameter("username");
	String password = request.getParameter("password");

	Map user = JsonParser.readValue(getRequestBody(request),
		Map.class);
	username = (String) user.get("username");
	password = (String) user.get("password");

	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
		password);

	return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	    FilterChain filterChain, Authentication authentication) {
	UserPrincipal user = ((UserPrincipal) authentication.getPrincipal());

	List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		.collect(Collectors.toList());

	byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

	String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
		.setHeaderParam("typ", SecurityConstants.TOKEN_TYPE).setIssuer(SecurityConstants.TOKEN_ISSUER)
		.setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(user.getUsername())
		.setExpiration(new Date(System.currentTimeMillis() + 864000000)).claim("rol", roles).compact();

	response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }
}
