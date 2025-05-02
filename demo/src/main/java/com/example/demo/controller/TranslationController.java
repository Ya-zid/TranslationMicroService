package com.example.demo.controller;

import com.example.demo.model.TranslationRequest;
import com.example.demo.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private final KafkaProducerService kafkaProducerService;

    public TranslationController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<String> translate(@RequestBody TranslationRequest request) {
        if (request.getRequestId() == null) {
            request.setRequestId(UUID.randomUUID().toString());
        }

        kafkaProducerService.sendTranslationRequest(request);

        return ResponseEntity.accepted()
            .body("Translation request accepted with ID: " + request.getRequestId());
    }
}