package org.xapps.services.gatewayservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.xapps.services.gatewayservice.utils.ConfigProvider;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    private final ConfigProvider configProvider;

    @Autowired
    public CorsConfig(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of(configProvider.getOriginsUrl()));
        corsConfig.setMaxAge(configProvider.getOriginsMaxAge());
        corsConfig.setAllowedMethods(Arrays.asList(configProvider.getOriginsMethods()));
        corsConfig.setAllowedHeaders(List.of(configProvider.getOriginsHeaders()));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }
}