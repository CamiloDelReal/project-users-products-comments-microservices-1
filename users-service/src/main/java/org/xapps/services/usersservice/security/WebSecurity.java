package org.xapps.services.usersservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.xapps.services.usersservice.services.UserService;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment env;
    private ObjectMapper objectMapper;
    private UserService userService;

    @Autowired
    public WebSecurity(Environment env, ObjectMapper objectMapper, @Lazy UserService userService) {
        this.env = env;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/users").permitAll()
                .anyRequest().permitAll()

                // Authentication with basic uri /login with token in response header and authorization filter with request header token
//                .and()
//                .addFilter(providesAuthenticationFilter())
//                .addFilter(providesAuthorizationFilter());
                // Authentication with custom uri /security/login with token in custom response object and authorization filter with request header token
                .and()
                .addFilter(providesAuthorizationFilter());

        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(providesBCryptPasswordEncoder());
        super.configure(auth);
    }

    private AuthenticationFilter providesAuthenticationFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter(authenticationManager(), env, objectMapper, userService);
        return authFilter;
    }

    private AuthorizationFilter providesAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), env, userService);
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
