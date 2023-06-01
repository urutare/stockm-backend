package com.urutare.stockm.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stockm")
@Data
public class Properties {
    private String API_SECRET_KEY;
    private long TOKEN_VALIDITY;
    private String BASE_URL;
}
