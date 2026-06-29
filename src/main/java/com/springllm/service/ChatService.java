package com.springllm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springllm.config.ChatbotProperties;
import com.springllm.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.ThinkOption;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ChatClient chatClient;
    private final ChatbotProperties chatbotProperties;

    public Flux<String> stream(ChatRequest request) {
        String effectiveModel = resolveModel(request.model());
        String persona = resolvePersona(effectiveModel);

        ChatClientRequestSpec spec = chatClient.prompt()
                .system(persona)
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.conversationId()));

        if (request.model() != null && !request.model().isBlank()) {
            ChatbotProperties.ClientProperties defaults = chatbotProperties.client();
            spec = spec.options(OllamaChatOptions.builder()
                    .model(request.model())
                    .temperature(defaults.temperature())
                    .numPredict(defaults.numPredict())
                    .thinkOption(new ThinkOption.ThinkBoolean(defaults.think())));
        }

        return spec.stream().content()
                .map(chunk -> {
                    try {
                        return OBJECT_MAPPER.writeValueAsString(chunk);
                    } catch (JsonProcessingException e) {
                        return "\"\"";
                    }
                })
                .doOnComplete(() ->
                        log.info("stream.complete conversationId={}", request.conversationId()))
                .doOnError(e ->
                        log.error("stream.error conversationId={}", request.conversationId(), e));
    }

    private String resolveModel(String requestModel) {
        if (requestModel != null && !requestModel.isBlank()) return requestModel;
        return chatbotProperties.client().model();
    }

    private String resolvePersona(String model) {
        ChatbotProperties.PersonaProperties personas = chatbotProperties.personas();
        if (personas.lite().models().contains(model)) {
            return personas.lite().systemPrompt();
        }
        return personas.full().systemPrompt();
    }
}
