package com.example.demo.controller;

import com.example.demo.model.TranslationRequest;
import com.example.demo.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/translate")
@Tag(name = "Translation", description = "Translation API for submitting text to be translated")
public class TranslationController {

    private final KafkaProducerService kafkaProducerService;

    public TranslationController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(
        summary = "Submit translation request",
        description = "Submits text for translation between languages. The request is processed asynchronously via Kafka."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "202", 
            description = "Translation request accepted",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request parameters",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error", 
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> translate(
            @Parameter(
                description = "Translation request details", 
                required = true
            )
            @RequestBody TranslationRequest request) {
        if (request.getRequestId() == null) {
            request.setRequestId(UUID.randomUUID().toString());
        }

        kafkaProducerService.sendTranslationRequest(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Translation request accepted with ID: " + request.getRequestId());
        response.put("requestId", request.getRequestId());

        return ResponseEntity.accepted().body(response);
    }
}