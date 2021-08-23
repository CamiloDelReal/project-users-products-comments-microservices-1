package org.xapps.services.usersservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.xapps.services.usersservice.dtos.LoginRequest;
import org.xapps.services.usersservice.entities.User;
import org.xapps.services.usersservice.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment env;
    private ObjectMapper objectMapper;
    private UserService userService;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env, ObjectMapper objectMapper, UserService userService) {
        super(authenticationManager);
        this.env = env;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            User user = userService.getByEmail(loginRequest.getEmail());
            List<GrantedAuthority> authorities;
            if(user != null) {
                authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
            } else {
                authorities = new ArrayList<>();
            }
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), authorities)
            );
        } catch(IOException ex) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
        String email = user.getUsername();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(env.getProperty("security.claims.header-autorities"), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        String key = env.getProperty("security.token.value");
        long issueAt = System.currentTimeMillis();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("Issued by Camilo del Real")
                .setSubject(email)
                .setIssuedAt(new Date(issueAt))
                .setExpiration(new Date(issueAt + Long.parseLong(env.getProperty("security.token.validity"))))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        response.setHeader(env.getProperty("security.token.header-name"), token);
    }
}
