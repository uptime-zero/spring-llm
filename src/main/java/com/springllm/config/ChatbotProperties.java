package com.springllm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatbot")
public record ChatbotProperties(
        ClientProperties client,
        MemoryProperties memory
) {
    public record ClientProperties(String systemPrompt, String model, double temperature, boolean think, int numPredict) {}
    public record MemoryProperties(int maxMessages) {}
}
