package com.ankat.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerListener {
    @KafkaListener(topics = "${topic.scenarios[0].scenario.[0].name}", groupId = "${topic.scenarios[0].scenario.[0].consumer-group}")
    public void consumeFromTopic(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consume successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }

    @KafkaListener(topics = "${topic.scenarios[1].scenario.[1].name}", groupId = "${topic.scenarios[1].scenario.[1].consumer-group}")
    public void consumeFromStreams(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consume successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }
}
