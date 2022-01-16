package org.xapps.services.productsservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.xapps.services.productsservice.utils.ConfigProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final ConfigProvider configProvider;

    @Autowired
    public WebSecurity(ConfigProvider configProvider) {
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
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(providesAuthorizationFilter());
        http.headers().frameOptions().disable();
    }

    private AuthorizationFilter providesAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), configProvider);
    }

    @Bean
    public AuthenticationManager providesAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}
