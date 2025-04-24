package com.example.resource.config;

import com.example.resource.utils.MetadataExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MetadataExtractorConfig {

    @Bean
    public MetadataExtractor metadataExtractor() {
        return new MetadataExtractor();
    }
}
