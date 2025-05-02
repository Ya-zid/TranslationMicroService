package com.example.demo.service;

import com.example.demo.model.TranslationRequest;
import com.example.demo.model.TranslationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final TranslationService translationService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(TranslationService translationService, 
                              KafkaTemplate<String, String> kafkaTemplate, 
                              ObjectMapper objectMapper) {
        this.translationService = translationService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "translation-requests", groupId = "translation-group")
    public void handleTranslationRequest(String message) {
        try {
            TranslationRequest request = objectMapper.readValue(message, TranslationRequest.class);
            
            String translatedText = translationService.translateText(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
            );

            TranslationResponse response = new TranslationResponse(
                request.getRequestId(),
                translatedText,
                request.getTargetLanguage()
            );

            kafkaTemplate.send("translation-responses", 
                objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            handleError(message, e);
        }
    }

    private void handleError(String originalMessage, Exception e) {
        try {
            kafkaTemplate.send("translation-errors", 
                String.format("Error processing message: %s. Error: %s", 
                    originalMessage, e.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}