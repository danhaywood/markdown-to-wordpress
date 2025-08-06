package com.danhaywood.md2wp.wp;

import lombok.Getter;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import com.danhaywood.md2wp.E2eTest;
import com.danhaywood.md2wp.Md2WpModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(classes = Md2WpModule.class)
class WordpressMediaService_E2eTest implements E2eTest {

    @Autowired private WordpressMediaService wordpressMediaService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired @Getter private ResourceLoader resourceLoader;

    @SneakyThrows
    @Test
    void happy_case() {
        final var mediaItem = wordpressMediaService.search("causeway-welcome-page").get();
        final var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mediaItem);
        System.out.println(json);
    }
}
