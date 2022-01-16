package org.xapps.services.gatewayservice.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConfigProvider {
    @Value("${security.origins.url}")
    private String originsUrl;

    @Value("${security.origins.max-age}")
    private Long originsMaxAge;

    @Value("${security.origins.methods}")
    private String[] originsMethods;

    @Value("${security.origins.headers}")
    private String[] originsHeaders;
}
