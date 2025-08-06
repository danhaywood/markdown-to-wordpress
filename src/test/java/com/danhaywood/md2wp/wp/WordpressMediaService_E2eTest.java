package com.danhaywood.md2wp.wp;

import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.danhaywood.md2wp.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = Module.class)
@ActiveProfiles("private")
class WordpressMediaService_E2eTest {

    @Autowired private WordpressMediaService wordpressMediaService;
    @Autowired private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void happy_case() {
        final var mediaItem = wordpressMediaService.search("causeway-welcome-page").get();
        final var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mediaItem);
        System.out.println(json);
    }
}
