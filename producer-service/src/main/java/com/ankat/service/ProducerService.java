package com.ankat.service;

import com.ankat.config.TopicProperties;
import com.ankat.model.PendingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = TopicProperties.class)
@Service
public class ProducerService {


    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PendingRequest pendingRequest;
    private final Tracer tracer;

    public void sendMessage(String topicName, String json) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, UUID.randomUUID().toString(), json);
        record.headers().add(new RecordHeader("trace_id", tracer.currentSpan().context().spanId().getBytes()));
        kafkaTemplate.send(record).addCallback(
                result -> log.info("Message sent successfully for the key: {} and value: {} on topic: {} in partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition()),
                ex -> log.info("Error sending message and exception is {}", ex.getMessage(), ex)
        );
    }

    public String sendAsyncMessage(String topicName, String json) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, UUID.randomUUID().toString(), json);
        record.headers().add(new RecordHeader("trace_id", tracer.currentSpan().context().spanId().getBytes()));
        /*
        kafkaTemplate.send(record).completable().whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully for the key: {} and value: {} on topic: {} in partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
                pendingRequest.add(tracer.currentSpan().context().spanId(), completableFuture);
            } else {
                log.info("Error sending message and exception is {}", ex.getMessage(), ex);
            }
        });
        */
        SendResult<String, String> result = kafkaTemplate.send(record).completable().get();
        if (Objects.nonNull(result)) {
            log.info("Message sent successfully for the key: {} and value: {} on topic: {} in partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
            pendingRequest.add(tracer.currentSpan().context().spanId(), completableFuture);
        } else {
            throw new RuntimeException("Error sending message");
        }
        return completableFuture.get();
    }
}
