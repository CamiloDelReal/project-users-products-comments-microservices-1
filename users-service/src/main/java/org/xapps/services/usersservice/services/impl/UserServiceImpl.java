package org.xapps.services.usersservice.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xapps.services.usersservice.dtos.LoginRequest;
import org.xapps.services.usersservice.dtos.LoginResponse;
import org.xapps.services.usersservice.dtos.UserRequest;
import org.xapps.services.usersservice.dtos.UserResponse;
import org.xapps.services.usersservice.entities.Role;
import org.xapps.services.usersservice.entities.User;
import org.xapps.services.usersservice.repositories.UserRepository;
import org.xapps.services.usersservice.services.RoleService;
import org.xapps.services.usersservice.services.UserService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private AuthenticationManager authenticationManager;
    private Environment env;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public UserServiceImpl(@Lazy AuthenticationManager authenticationManager, Environment env, UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        UserResponse response = null;
        if(user != null) {
            response = modelMapper.map(user, UserResponse.class);
        }
        return response;
    }

    @Override
    public UserResponse create(UserRequest userRequest) {
        // This method should return a custom object with error code + response
        User entity = modelMapper.map(userRequest, User.class);
        entity.setEncryptedPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role guest = roleService.getByName("Guest");
        entity.setRoles(List.of(guest));
        UserResponse response = null;
        try {
            userRepository.save(entity);
            response = modelMapper.map(entity, UserResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public UserResponse edit(Long id, UserRequest userRequest) {
        // This method should return a custom object with error code + response
        User entity = userRepository.findById(id).orElse(null);
        UserResponse response = null;
        if(entity != null) {
            try {
                entity.setFirstName(userRequest.getFirstName());
                entity.setLastName(userRequest.getLastName());
                entity.setEmail(userRequest.getEmail());
                entity.setEncryptedPassword(passwordEncoder.encode(userRequest.getPassword()));
                userRepository.save(entity);
                response = modelMapper.map(entity, UserResponse.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        LoginResponse response = null;
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
            System.out.println(user);
            Collection<GrantedAuthority> authorities;
            if(user != null) {
                authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
            } else {
                authorities = new ArrayList<>();
            }
            SecurityContextHolder.getContext().setAuthentication(auth);
            String rolesClaims = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(env.getProperty("security.claims.separator")));
            logger.info("Roles inside token  " + rolesClaims);
            Claims claims = Jwts.claims().setSubject(loginRequest.getEmail());
            claims.put(env.getProperty("security.claims.header-authorities"), rolesClaims);
            String key = env.getProperty("security.token.value");
            long issueAt = System.currentTimeMillis();
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer("Issued by Camilo del Real")
                    .setSubject(loginRequest.getEmail())
                    .setIssuedAt(new Date(issueAt))
                    .setExpiration(new Date(issueAt + Long.parseLong(env.getProperty("security.token.validity"))))
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();
            response = new LoginResponse(loginRequest.getEmail(), token, env.getProperty("security.token.type"), AuthorityUtils.authorityListToSet(authorities));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) throw new UsernameNotFoundException(email);
        Collection<GrantedAuthority> authorities;
        if(user != null) {
            authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        } else {
            authorities = new ArrayList<>();
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, authorities);
    }
}
