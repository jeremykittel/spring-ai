package com.example.application.integration;

import com.example.application.endpoints.ai.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AiServiceIntegrationTest extends BaseIntegrationTestConfig {

    @Autowired
    private AiService aiService;

    @Test
    public void testChat() {
        // given
        String query = "Hello";
        // when
        String response = aiService.chat(query);
        // then
        assertNotNull(response);
        assertEquals("Hello! How can I assist you today?", response);
    }
}
