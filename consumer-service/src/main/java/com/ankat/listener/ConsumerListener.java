package com.ankat.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerListener {

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consume successfully for the key: {} & value: {} on partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.partition());
    }
}
