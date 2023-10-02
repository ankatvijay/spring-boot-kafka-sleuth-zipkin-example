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
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

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
                result -> {
                    log.info("Message sent successfully for the key: {} and value: {} on topic: {} in partition: {}", result.getProducerRecord().key(), result.getProducerRecord().value(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
                },
                ex -> {
                    log.info("Error sending message and exception is {}", ex.getMessage(), ex);
                }
        );
    }

    public String sendAsyncMessage(String topicName, String json) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, UUID.randomUUID().toString(), json);
        record.headers().add(new RecordHeader("trace_id", tracer.currentSpan().context().spanId().getBytes()));
        kafkaTemplate.send(record).completable().get();
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        pendingRequest.add(tracer.currentSpan().context().spanId(),completableFuture);
        return completableFuture.get(300,TimeUnit.MILLISECONDS);
    }
}
