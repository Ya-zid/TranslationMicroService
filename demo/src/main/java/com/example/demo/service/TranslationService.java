package com.example.demo.service;
import space.dynomake.libretranslate.Translator;
import space.dynomake.libretranslate.Language;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    public TranslationService() {
        // Set the API endpoint to your self-hosted instance
        Translator.setUrlApi("http://localhost:5000/translate"); // Replace with your actual URL if different

        // If your instance requires an API key, set it here
        // Translator.setApiKey("your_api_key");
    }

    public String translateText(String text, String sourceLang, String targetLang) {
        return Translator.translate(sourceLang, targetLang, text);
    }
}
