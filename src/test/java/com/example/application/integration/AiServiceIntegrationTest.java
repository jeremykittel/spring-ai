package com.example.application.integration;

import com.example.application.endpoints.ai.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AiServiceIntegrationTest {
    @Autowired
    private AiService aiService;
    @Test
    public void testChat() {
        String query = "Hello";
        String response = aiService.chat(query);
        assertNotNull(response);
        assertEquals("Hello! How can I assist you today?", response);
    }
}
