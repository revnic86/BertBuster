package com.rob.bertbuster.config;

import com.rob.bertbuster.service.impl.MovieServiceImpl;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider movieTools(MovieServiceImpl movieService) {


        // Scans the service for @Tool
        return MethodToolCallbackProvider.builder()
                .toolObjects(movieService)
                .build();
    }
}
