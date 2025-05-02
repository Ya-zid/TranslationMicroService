package com.example.demo.model;

public class TranslationResponse {
    private String requestId;
    private String translatedText;
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