package com.springllm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "chatbot")
public record ChatbotProperties(
        ClientProperties client,
        MemoryProperties memory,
        PersonaProperties personas
) {
    public record ClientProperties(String systemPrompt, String model, double temperature, boolean think, int numPredict) {}
    public record MemoryProperties(int maxMessages) {}

    public record PersonaProperties(PersonaConfig full, PersonaConfig lite) {
        public record PersonaConfig(String systemPrompt, List<String> models) {}
    }
}
