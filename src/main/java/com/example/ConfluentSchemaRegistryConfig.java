package com.example;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.schema.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.cloud.stream.schema.client.config.SchemaRegistryClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Created by b on 12/3/17.
 */
@Configuration
@EnableConfigurationProperties({SchemaRegistryClientProperties.class})
public class ConfluentSchemaRegistryConfig {
    @Bean
    public SchemaRegistryClient schemaRegistryClient(SchemaRegistryClientProperties schemaRegistryClientProperties) {
        ConfluentSchemaRegistryClient confluentSchemaRegistryClient = new ConfluentSchemaRegistryClient();
        if(StringUtils.hasText(schemaRegistryClientProperties.getEndpoint())) {
            confluentSchemaRegistryClient.setEndpoint(schemaRegistryClientProperties.getEndpoint());
        }

        return confluentSchemaRegistryClient;
    }
}
