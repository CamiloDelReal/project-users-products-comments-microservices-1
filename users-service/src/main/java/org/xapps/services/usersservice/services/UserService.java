package org.xapps.services.usersservice.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.xapps.services.usersservice.dtos.LoginRequest;
import org.xapps.services.usersservice.dtos.LoginResponse;
import org.xapps.services.usersservice.dtos.UserRequest;
import org.xapps.services.usersservice.dtos.UserResponse;
import org.xapps.services.usersservice.entities.User;


public interface UserService extends UserDetailsService {

    User getByEmail(String email);

    UserResponse getById(Long id);

    UserResponse  create(UserRequest userRequest);

    UserResponse edit(Long id, UserRequest userRequest);

    boolean delete(Long id);

    LoginResponse authenticate(LoginRequest loginRequest);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
