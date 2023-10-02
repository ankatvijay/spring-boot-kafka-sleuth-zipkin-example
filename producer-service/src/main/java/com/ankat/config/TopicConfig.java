package com.ankat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@Configuration
public class TopicConfig {

    @Bean
    public KafkaAdmin.NewTopics createKafkaTopics(TopicProperties topicProperties) {
        NewTopic[] topics = topicProperties.getScenarios().stream().flatMap(scenario -> scenario.getScenario().stream()).map(TopicProperties.Topic::getName).map(topic -> TopicBuilder.name(topic).partitions(4).replicas(4).build()).toArray(NewTopic[]::new);
        return new KafkaAdmin.NewTopics(topics);
    }

    /*
    @Bean
    ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(KafkaTemplate<String, String> template,ConcurrentKafkaListenerContainerFactory<String, String> factory) {
        ProducerFactory<String,String> pf = template.getProducerFactory();
        factory.setReplyTemplate(template);
        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer("SLEUTH_ZIPKIN_TOPIC_CASE_3_2");
        container.getContainerProperties().setGroupId("sleuth_zipkin_consumer_group_case_3_2");
        ReplyingKafkaTemplate<String,String,String> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, container);
        replyingKafkaTemplate.setDefaultTopic("SLEUTH_ZIPKIN_TOPIC_CASE_3_2");
        return replyingKafkaTemplate;
    }

    @Bean
    ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(KafkaTemplate<String, String> template,DefaultKafkaConsumerFactory<String, String> consumerFactory) {
        ProducerFactory<String,String> pf = template.getProducerFactory();
        consumerFactory.getConfigurationProperties();
        factory.setReplyTemplate(template);
        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer("SLEUTH_ZIPKIN_TOPIC_CASE_3_2");
        container.getContainerProperties().setGroupId("sleuth_zipkin_consumer_group_case_3_2");
        ReplyingKafkaTemplate<String,String,String> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, container);
        replyingKafkaTemplate.setDefaultTopic("SLEUTH_ZIPKIN_TOPIC_CASE_3_2");
        return replyingKafkaTemplate;
    }
    */
}
