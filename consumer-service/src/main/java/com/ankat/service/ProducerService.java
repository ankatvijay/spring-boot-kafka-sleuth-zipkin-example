package com.ankat.service;

import com.ankat.config.TopicProperties;
import com.ankat.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = TopicProperties.class)
@Service
public class ProducerService {
    private final TopicProperties topicProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ConsumerRecord<String, String> consumerRecord) {
        ProducerRecord<String, String> record = new ProducerRecord<>(TopicUtil.getScenarioTopicName(topicProperties, 2, 2), consumerRecord.key(), consumerRecord.value());
        Headers headers = consumerRecord.headers();
        Optional<Header> internalHeader = StreamSupport.stream(headers.spliterator(), false).filter(header -> "trace_id".equals(header.key())).findFirst();
        if (internalHeader.isPresent()) {
            record.headers().add(new RecordHeader("trace_id", internalHeader.get().value()));
        }

        kafkaTemplate.send(record).addCallback(
                result -> {
                    log.info("Message sent successfully for the key: {} and value: {} on topic: {} in partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
                },
                ex -> {
                    log.info("Error sending message and exception is {}", ex.getMessage(), ex);
                }
        );
    }
}
