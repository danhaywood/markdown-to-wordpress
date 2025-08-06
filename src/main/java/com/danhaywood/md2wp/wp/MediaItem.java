package com.danhaywood.md2wp.wp;

import lombok.Data;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaItem {

    private long id;
    private String source_url;

    private MediaDetails media_details;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MediaDetails {
        private Map<String, SizeInfo> sizes;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SizeInfo {
        private String file;
        private int width;
        private int height;
        private String source_url;
    }

    @JsonIgnore
    public String getSourceUrlFull() {
        return sourceUrlOf("full");
    }

    private @Nullable String sourceUrlOf(String size) {
        return Optional.ofNullable(media_details.getSizes().get(size)).map(SizeInfo::getSource_url).orElse(null);
    }
}
