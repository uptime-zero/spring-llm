package com.springllm.controller;

import com.springllm.dto.ChatRequest;
import com.springllm.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody ChatRequest request, ServerHttpResponse response) {
        log.info("stream.request conversationId={} model={}", request.conversationId(), request.model());
        response.getHeaders().set("Cache-Control", "no-cache");
        response.getHeaders().set("Connection", "keep-alive");
        response.getHeaders().set("X-Accel-Buffering", "no");
        return chatService.stream(request);
    }
}
