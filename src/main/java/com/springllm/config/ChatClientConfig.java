package com.springllm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.ThinkOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ChatbotProperties chatbotProperties;
    private final ChatMemory chatMemory;

    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel) {
        ChatbotProperties.ClientProperties client = chatbotProperties.client();
        ChatClient.Builder builder = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .temperature(client.temperature())
                                .model(client.model())
                                .numPredict(client.numPredict())
                                .thinkOption(new ThinkOption.ThinkBoolean(client.think()))
                );
        if (client.systemPrompt() != null && !client.systemPrompt().isBlank()) {
            builder = builder.defaultSystem(client.systemPrompt());
        }
        return builder.build();
    }
}
