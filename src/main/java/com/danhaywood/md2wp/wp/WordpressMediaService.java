package com.danhaywood.md2wp.wp;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.danhaywood.md2wp.config.Md2WpConfig;

@Service
public class WordpressMediaService {

    private final RestClient restClient;
    private final Md2WpConfig config;

    public WordpressMediaService(RestClient.Builder builder, Md2WpConfig config) {
        this.restClient = builder.build();
        this.config = config;
    }

    public Optional<MediaItem> search(String filename) {
        final var configWordpress = config.getWordpress();
        String mediaRestUrl = configWordpress.getMediaRestUrl();

        String url = "%s?author=%d&search=%s".formatted(mediaRestUrl, configWordpress.getAuthorId(), filename);

        String auth = configWordpress.getUsername() + ":" + configWordpress.getApplicationPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        return restClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .retrieve()
                .<List<MediaItem>>body(new ParameterizedTypeReference<>() {})
                .stream().findFirst();
    }
}
