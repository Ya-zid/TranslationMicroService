package com.example.demo.model;

public class TranslationRequest {
    private String requestId;
    private String text;
    private String sourceLanguage;
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