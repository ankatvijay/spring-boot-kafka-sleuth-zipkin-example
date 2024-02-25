package com.ankat.listener;

import com.ankat.model.Employee;
import com.ankat.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${topic.scenarios[0].scenario.[0].name}", groupId = "${topic.scenarios[0].scenario.[0].consumer-group}")
    public void consumeFromTopicAndSendToLog(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromTopicAndSendToLog successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }

    @KafkaListener(topics = "${topic.scenarios[1].scenario.[1].name}", groupId = "${topic.scenarios[1].scenario.[1].consumer-group}")
    public void consumeFromStreamsAndSendToLog(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromStreamsAndSendToLog successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
    }

    @KafkaListener(topics = "${topic.scenarios[2].scenario.[1].name}", groupId = "${topic.scenarios[2].scenario.[1].consumer-group}")
    public void consumeFromAsyncAndSendToProducer(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consumeFromAsyncAndSendToProducer successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
        producerService.sendMessage(consumerRecord);
    }

    @KafkaListener(topics = "${topic.scenarios[3].scenario.[0].name}", groupId = "${topic.scenarios[3].scenario.[0].consumer-group}")
    public void consumeFromTopicAndSendToDatabase(@Payload ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("Message consumeFromTopicAndSendToDatabase successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
        Employee employee = objectMapper.readValue(consumerRecord.value(), Employee.class);
        producerService.insertRecord(employee);
    }
}
