package com.example.application.integration;

import com.example.application.Application;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ApplicationIntegrationTest extends BaseIntegrationTestConfig {
    @Test
    void contextLoads() {
    }

    @Test
    public void applicationContextTest() {
        Application.main(new String[] {});
    }
}
