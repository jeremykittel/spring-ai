package com.example.application.integration;

import com.example.application.Application;
import org.junit.jupiter.api.Test;

public class ApplicationIntegrationTest extends BaseIntegrationTestConfig {
    @Test
    void contextLoads() {
    }

    @Test
    public void applicationContextTest() {
        Application.main(new String[] {});
    }
}
