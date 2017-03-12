package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by b on 12/3/17.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
@Import({ConfluentSchemaRegistryConfig.class})
public @interface EnableConfluentSchemaRegistryClient {
}
