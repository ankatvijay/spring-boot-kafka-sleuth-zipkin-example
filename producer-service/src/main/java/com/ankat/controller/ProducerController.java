package com.ankat.controller;

import com.ankat.config.TopicProperties;
import com.ankat.model.Employee;
import com.ankat.service.ProducerService;
import com.ankat.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@RestController
public class ProducerController {
    private final RestTemplate restTemplate;
    private final ProducerService producerService;
    private final TopicProperties topicProperties;

    @PostMapping(value = "/kafka/publishToConsume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToConsume(@RequestBody Employee employee) {
        producerService.sendMessage(TopicUtil.getScenarioTopicName(topicProperties, 0, 0), employee.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/kafka/publishToStreams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToStreams(@RequestBody Employee employee) {
        producerService.sendMessage(TopicUtil.getScenarioTopicName(topicProperties, 1, 0), employee.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/kafka/publishToAsync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publishToAsync(@RequestBody Employee employee) throws ExecutionException, InterruptedException {
        String response = producerService.sendAsyncMessage(TopicUtil.getScenarioTopicName(topicProperties, 2, 0), employee.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/kafka/publishToDatabase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publishToDatabase(@RequestBody Employee employee) {
        producerService.sendMessage(TopicUtil.getScenarioTopicName(topicProperties, 3, 0), employee.toString());
        //return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmployee).toUri()).build();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/web/publishToHTTP")
    public ResponseEntity<String> publishToHTTP(HttpEntity<String> httpEntity) {
        String response = restTemplate.postForObject("http://localhost:8070/sleuth/web/responseToHTTP", httpEntity, String.class);
        return ResponseEntity.ok(response);
    }
}
