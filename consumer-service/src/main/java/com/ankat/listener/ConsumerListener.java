package com.ankat.listener;

import com.ankat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConsumerListener {
    private final ProducerService producerService;
    @KafkaListener(topics = "${topic.scenarios[0].scenario.[0].name}", groupId = "${topic.scenarios[0].scenario.[0].consumer-group}")
    public void consumeFromTopic(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromTopic successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }

    @KafkaListener(topics = "${topic.scenarios[1].scenario.[1].name}", groupId = "${topic.scenarios[1].scenario.[1].consumer-group}")
    public void consumeFromStreams(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromStreams successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }

    @KafkaListener(topics = "${topic.scenarios[2].scenario.[1].name}", groupId = "${topic.scenarios[2].scenario.[1].consumer-group}")
    public void consumeFromAsync(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromAsync successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
        producerService.sendMessage(consumerRecord);
    }
}
