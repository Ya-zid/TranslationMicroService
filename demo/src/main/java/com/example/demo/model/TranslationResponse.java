package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class TranslationResponse {
    @Schema(description = "Unique identifier matching the original request", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private String requestId;
    
    @Schema(description = "The translated text", example = "¡Hola, mundo!")
    private String translatedText;
    
    @Schema(description = "The target language code", example = "es")
    private String targetLanguage;

    public TranslationResponse() {}

    public TranslationResponse(String requestId, String translatedText, String targetLanguage) {
        this.requestId = requestId;
        this.translatedText = translatedText;
        this.targetLanguage = targetLanguage;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getTranslatedText() { return translatedText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }
    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
}