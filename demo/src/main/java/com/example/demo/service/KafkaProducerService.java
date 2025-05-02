package com.example.demo.service;

import com.example.demo.model.TranslationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTranslationRequest(TranslationRequest request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send("translation-requests", request.getRequestId(), message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending translation request to Kafka", e);
        }
    }
}