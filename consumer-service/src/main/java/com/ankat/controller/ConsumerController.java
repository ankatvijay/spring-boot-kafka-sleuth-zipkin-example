package com.ankat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ConsumerController {
    @PostMapping(value = "/web/responseToHTTP", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> responseToHTTP(HttpEntity<String> json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json.getBody(), HashMap.class);
        return ResponseEntity.ok(map.get("emp-name").toString());
    }
}
