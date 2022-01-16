package org.xapps.services.usersservice.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.xapps.services.usersservice.entities.User;
import org.xapps.services.usersservice.services.UserService;
import org.xapps.services.usersservice.utils.ConfigProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;
    private final ConfigProvider configProvider;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, ConfigProvider configProvider) {
        super(authenticationManager);
        this.userService = userService;
        this.configProvider = configProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(configProvider.getTokenType())) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);
            if(auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = null;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null) {
            String token = authHeader.replace(configProvider.getTokenType(), "");
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(configProvider.getTokenKey())
                        .parseClaimsJws(token)
                        .getBody();

                String[] subjectData = claims.getSubject().split(configProvider.getAuthoritiesSeparator());

                if(subjectData.length == 2 && !subjectData[0].isEmpty() && !subjectData[1].isEmpty()) {
                    User user = userService.getByEmail(subjectData[1]);
                    if(user != null) {
                        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
                        auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    }
                }
            } catch (ExpiredJwtException ex) {
                logger.error("Token expired", ex);
            }
        }

        System.out.println("Returning " + auth);
        return auth;
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(configProvider.getTokenKey())
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
