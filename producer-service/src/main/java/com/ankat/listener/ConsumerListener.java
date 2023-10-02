package com.ankat.listener;

import com.ankat.model.PendingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConsumerListener {
    private final PendingRequest pendingRequest;
    @KafkaListener(topics = "${topic.scenarios[2].scenario.[2].name}", groupId = "${topic.scenarios[2].scenario.[2].consumer-group}")
    public void consumeFromTopic(@Payload ConsumerRecord<String, String> consumerRecord) {
        log.info("Message consume successfully for the key: {} and value: {} on topic: {} in partition: {}", consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.partition());
        Headers headers = consumerRecord.headers();
        Optional<Header> internalHeader = StreamSupport.stream(headers.spliterator(), false).filter(header -> "trace_id".equals(header.key())).findFirst();
        if (internalHeader.isPresent()) {
            String tracId = new String(internalHeader.get().value());
            if(Objects.nonNull(pendingRequest.get(tracId))){
                pendingRequest.get(tracId).complete("Done Circle");
                pendingRequest.remove(String.valueOf(internalHeader.get().value()));
            }
        }
    }
}
