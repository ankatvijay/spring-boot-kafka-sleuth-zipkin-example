package com.ankat.controller;

import com.ankat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping(value = "/pushToConsume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> pushToConsume(HttpEntity<String> json) {
        producerService.sendMessage(json.getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
