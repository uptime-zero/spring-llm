package com.springllm.controller;

import com.springllm.dto.ChatRequest;
import com.springllm.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatClientRequestSpec spec = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.conversationId()));

        if (request.model() != null && !request.model().isBlank()) {
            spec = spec.options(
                    OllamaChatOptions.builder()
                            .model(request.model())
            );
        }

        return ResponseEntity.ok(new ChatResponse(spec.call().content()));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(
            @RequestParam String message,
            @RequestParam(defaultValue = "default") String conversationId,
            @RequestParam(required = false) String model) {

        ChatClientRequestSpec spec = chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId));

        if (model != null && !model.isBlank()) {
            spec = spec.options(OllamaChatOptions.builder().model(model));
        }

        return spec.stream().content();
    }
}

