package com.springllm.dto;

public record ChatRequest(String message, String conversationId, String model) {
}
