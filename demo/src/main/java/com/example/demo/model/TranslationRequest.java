package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class TranslationRequest {
    @Schema(description = "Unique identifier for the translation request", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private String requestId;
    
    @Schema(description = "Text to be translated", example = "Hello, world!")
    private String text;
    
    @Schema(description = "Source language code", example = "en")
    private String sourceLanguage;
    
    @Schema(description = "Target language code", example = "es")
    private String targetLanguage;

    // Constructors
    public TranslationRequest() {}

    public TranslationRequest(String requestId, String text, String sourceLanguage, String targetLanguage) {
        this.requestId = requestId;
        this.text = text;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getSourceLanguage() { return sourceLanguage; }
    public void setSourceLanguage(String sourceLanguage) { this.sourceLanguage = sourceLanguage; }
    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
}