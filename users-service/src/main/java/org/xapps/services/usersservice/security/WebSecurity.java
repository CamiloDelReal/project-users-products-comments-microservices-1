package org.xapps.services.usersservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.xapps.services.usersservice.services.UserService;
import org.xapps.services.usersservice.utils.ConfigProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ConfigProvider configProvider;

    @Autowired
    public WebSecurity(ObjectMapper objectMapper, @Lazy UserService userService, ConfigProvider configProvider) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.configProvider = configProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of(configProvider.getOriginsUrl()));
                corsConfiguration.setAllowedMethods(Arrays.asList(configProvider.getOriginsMethods()));
                corsConfiguration.setAllowedHeaders(Arrays.asList(configProvider.getOriginsHeaders()));
                corsConfiguration.setMaxAge(configProvider.getOriginsMaxAge());
                return corsConfiguration;
            }
        });
        http
                .authorizeRequests()
                .antMatchers("/users").permitAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Authentication with basic uri /login with token in response header and authorization filter with request header token
//                .and()
//                .addFilter(providesAuthenticationFilter())
//                .addFilter(providesAuthorizationFilter());
                // Authentication with custom uri /security/login with token in custom response object and authorization filter with request header token
                .and()
                .addFilter(providesAuthorizationFilter());
                // Same as previous
//                .addFilterBefore(providesAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(providesBCryptPasswordEncoder());
        super.configure(auth);
    }

    private AuthenticationFilter providesAuthenticationFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter(authenticationManager(), objectMapper, userService, configProvider);
        return authFilter;
    }

    private AuthorizationFilter providesAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), userService, configProvider);
    }

    @Bean
    public BCryptPasswordEncoder providesBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager providesAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}
