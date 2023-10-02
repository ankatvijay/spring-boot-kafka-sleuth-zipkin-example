package com.ankat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {TopicProperties.class, KafkaProperties.class})
@Configuration
public class TopicConfig {

    /*
    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }
    */

    /*
    @Bean
    public NewTopic testTopic(TopicProperties topicProperties) {
        log.info("Create Topic : {}", topicProperties.getScenario1().getName());
        return TopicBuilder.name(topicProperties.getScenario1().getName()).partitions(4).replicas(4).build();
    }
    */

    private final TopicProperties topicProperties;

    @Bean
    public KafkaAdmin.NewTopics createKafkaTopics() {
        List<NewTopic> topics = topicProperties.getScenarios().stream().flatMap(scenario -> scenario.getScenario().stream()).map(TopicProperties.Topic::getName).map(topic -> TopicBuilder.name(topic).partitions(4).replicas(4).build()).collect(Collectors.toList());
        return new KafkaAdmin.NewTopics(topics.toArray(NewTopic[]::new));
        //List<String> topicNames = List.of("SLEUTH_ZIPKIN_TOPIC_CASE_1_1","SLEUTH_ZIPKIN_TOPIC_CASE_1_2"); // producer -> consumer
        //List<NewTopic> topics= topicNames.stream().map(topic -> TopicBuilder.name(topic).partitions(4).replicas(4).build()).collect(Collectors.toList());
    }
}
