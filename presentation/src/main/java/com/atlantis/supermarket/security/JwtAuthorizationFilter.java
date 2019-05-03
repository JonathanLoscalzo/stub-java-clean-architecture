package com.atlantis.supermarket.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;

/**
 * The second filter handles all HTTP requests and checks if there is an Authorization header with correct token. 
 * For example, if token is not expired or if signature key is correct.
 * If the token is valid then filter will add authentication data into Spring’s security context.
 * @author jloscalzo
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	// private static final Logger log =
	// LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, java.io.IOException {
	    	// if header starts with "Bearer"
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		String header = request.getHeader(SecurityConstants.TOKEN_HEADER);

		if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	/**
	 * The most important part of the filter that we've implemented is the private getAuthentication method. 
	 * This method reads the JWT from the Authorization header, and then uses JWT to validate the token. 
	 * If everything is in place, we set the user in the SecurityContext and allow the request to move on.
	 * @param request
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
		if (!StringUtils.isEmpty(token)) {
			try {
				byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey)
						.parseClaimsJws(token.replace("Bearer ", ""));

				String username = parsedToken.getBody().getSubject();

				List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody().get("rol")).stream()
						.map(authority -> new SimpleGrantedAuthority((String) authority)).collect(Collectors.toList());

				if (!StringUtils.isEmpty(username)) {
					return new UsernamePasswordAuthenticationToken(username, null, authorities);
				}
			} catch (ExpiredJwtException exception) {
				// log.warn("Request to parse expired JWT : {} failed : {}", token,
				// exception.getMessage());
			} catch (UnsupportedJwtException exception) {
				// log.warn("Request to parse unsupported JWT : {} failed : {}", token,
				// exception.getMessage());
			} catch (MalformedJwtException exception) {
				// log.warn("Request to parse invalid JWT : {} failed : {}", token,
				// exception.getMessage());
			} catch (SignatureException exception) {
				// log.warn("Request to parse JWT with invalid signature : {} failed : {}",
				// token, exception.getMessage());
			} catch (IllegalArgumentException exception) {
				// log.warn("Request to parse empty or null JWT : {} failed : {}", token,
				// exception.getMessage());
			}
		}

		return null;
	}
}
