package com.ankat.service;

import com.ankat.config.TopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = TopicProperties.class)
@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TopicProperties topicProperties;
    private final Tracer tracer;

    public void sendMessage(String json) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicProperties.getName(), UUID.randomUUID().toString(), json);
        record.headers().add(new RecordHeader("Created_Timestamp", Long.toString(System.currentTimeMillis()).getBytes()));
        record.headers().add(new RecordHeader("trace_id", tracer.currentSpan().context().spanId().getBytes()));
        log.info("Trace-Id: {}",tracer.currentSpan().context().spanId());
        kafkaTemplate.send(record).addCallback(
                result -> {
                    log.info("Message sent successfully for the key: {} and value: {} on partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().partition());
                },
                ex -> {
                    log.info("Error sending message and exception is {}", ex.getMessage(), ex);
                }
        );
    }
}
