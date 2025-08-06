package com.danhaywood.md2wp.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("md2wp")
@Data
public class Md2WpConfig {

    private Wordpress wordpress = new Wordpress();

    @Data
    public static class Wordpress {

        private int authorId;
        @NotEmpty private String username;
        @NotEmpty private String applicationPassword;

        private String baseUrl = "https://javapro.io/";
        private String mediaRestUrlSuffix = "wp-json/wp/v2/media";

        public String getMediaRestUrl() {
            return baseUrl + mediaRestUrlSuffix;
        }
    }

}
