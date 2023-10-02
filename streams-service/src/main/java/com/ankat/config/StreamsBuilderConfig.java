package com.ankat.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamsBuilderConfig {

    @Value("${topic.scenarios[1].scenario.[0].consumer-group}")
    private String scenario1StreamApplicationId;
    @Value("${topic.scenarios[2].scenario.[0].consumer-group}")
    private String scenario2StreamApplicationId;

    @Bean("scenario1StreamsBuilderFactoryBean")
    public StreamsBuilderFactoryBean scenario1StreamsBuilderFactoryBean(KafkaProperties properties) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, scenario1StreamApplicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getStreams().getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }

    @Bean("scenario2StreamsBuilderFactoryBean")
    public StreamsBuilderFactoryBean scenario2StreamsBuilderFactoryBean(KafkaProperties properties) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, scenario2StreamApplicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getStreams().getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }
}
