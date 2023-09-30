package com.ankat.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {TopicProperties.class, KafkaProperties.class})
public class TopicConfig {

    /*
    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }
    */

    @Bean
    public NewTopic createSourceTopic(TopicProperties topicProperties) {
        log.info("Create Source Topic : {}",topicProperties.getSource());
        return TopicBuilder.name(topicProperties.getSource()).partitions(4).replicas(4).build();
    }

    @Bean
    public NewTopic createDestinationTopic(TopicProperties topicProperties) {
        log.info("Create Source Topic : {}",topicProperties.getDestination());
        return TopicBuilder.name(topicProperties.getDestination()).partitions(4).replicas(4).build();
    }
}
