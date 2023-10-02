package com.ankat.controller;

import com.ankat.config.TopicProperties;
import com.ankat.service.ProducerService;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@RestController
public class ProducerController {

    //@Value("${topic.scenario1.name}")
    private String scenario1TopicName = "SLEUTH_ZIPKIN_TOPIC_CASE_1_1";

    //@Value("${topic.scenario2.name}")
    private String scenario2TopicName = "SLEUTH_ZIPKIN_TOPIC_CASE_1_2";

    private final ProducerService producerService;

    private final TopicProperties topicProperties;

    @PostMapping(value = "/pushToConsume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> pushToConsume(HttpEntity<String> json) {
        producerService.sendMessage(getScenarioTopicName(0,0), json.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/publishToStreams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToStreams(HttpEntity<String> json) {
        producerService.sendMessage(getScenarioTopicName(1,0), json.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getScenarioTopicName(int scenarioIndex, int topicIndex){
        List<TopicProperties.Scenario> scenarios = topicProperties.getScenarios().stream().toList();
        return scenarios.get(scenarioIndex).getScenario().get(topicIndex).getName();
    }
}
