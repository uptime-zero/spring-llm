package com.springllm;

import com.springllm.config.ChatbotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ChatbotProperties.class)
public class SpringLlmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLlmApplication.class, args);
    }

}
