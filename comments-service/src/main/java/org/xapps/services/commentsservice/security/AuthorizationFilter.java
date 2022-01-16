package org.xapps.services.commentsservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.xapps.services.commentsservice.dtos.UserAuthenticated;
import org.xapps.services.commentsservice.utils.ConfigProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    private final ConfigProvider configProvider;

    public AuthorizationFilter(AuthenticationManager authenticationManager, ConfigProvider configProvider) {
        super(authenticationManager);
        this.configProvider = configProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(configProvider.getTokenType())) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(request, response);
    }

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
                    UserAuthenticated user = new UserAuthenticated(Long.parseLong(subjectData[0]), subjectData[1]);
                    String rolesClaim = claims.get(configProvider.getHeaderAuthorities(), String.class);
                    user.setRoles(Stream.of(rolesClaim.split(configProvider.getAuthoritiesSeparator())).collect(Collectors.toList()));
                    Collection<GrantedAuthority> roles = user.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    roles.forEach(it -> System.out.println(it.getAuthority()));
                    auth = new UsernamePasswordAuthenticationToken(user, null, roles);
                }
            } catch (ExpiredJwtException ex) {
                logger.error("Token expired");
            }
        }

        return auth;
    }
}
