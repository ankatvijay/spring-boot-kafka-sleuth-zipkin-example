package com.ankat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("topic")
public class TopicProperties {

    @Getter
    @Setter
    private String source;

    @Getter
    @Setter
    private String destination;
}
