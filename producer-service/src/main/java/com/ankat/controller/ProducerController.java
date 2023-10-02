package com.ankat.controller;

import com.ankat.config.TopicProperties;
import com.ankat.service.ProducerService;
import com.ankat.util.TopicUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@RestController
public class ProducerController {
    private final ProducerService producerService;
    private final TopicProperties topicProperties;

    @PostMapping(value = "/publishToConsume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToConsume(HttpEntity<String> json) {
        producerService.sendMessage(TopicUtil.getScenarioTopicName(topicProperties, 0, 0), json.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/publishToStreams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToStreams(HttpEntity<String> json) {
        producerService.sendMessage(TopicUtil.getScenarioTopicName(topicProperties, 1, 0), json.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/publishToAsync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publishToAsync(HttpEntity<String> json) throws ExecutionException, InterruptedException, TimeoutException {
        String response = producerService.sendAsyncMessage(TopicUtil.getScenarioTopicName(topicProperties, 2, 0), json.getBody());
        return ResponseEntity.ok(response);
    }


}
