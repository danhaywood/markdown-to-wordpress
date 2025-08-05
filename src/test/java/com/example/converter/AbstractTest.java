package com.example.converter;

import lombok.*;

import java.nio.charset.StandardCharsets;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import com.google.common.io.Resources;

public abstract class AbstractTest {

    protected TestInfo testInfo;

    @BeforeEach
    void captureTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    protected @NonNull String readMd(@NonNull Enum<?> scenario, @Nullable String prefix) {
        return readResource(scenario, prefix, ".md");
    }

    protected @NonNull String readHtml(@NonNull Enum<?> scenario, @Nullable String prefix) {
        return readResource(scenario, prefix, ".html");
    }

    @SneakyThrows
    protected @NonNull String readResource(String suffix) {
        return readResource(null, null, suffix);
    }

    @SneakyThrows
    protected @NonNull String readResource(@Nullable Enum<?> scenario, @Nullable String prefix, String suffix) {
        String resourceName;
        if (scenario == null) {
            resourceName = String.format("%s.%s", getClass().getSimpleName(), testInfo.getTestMethod().get().getName());
        } else {
            resourceName = String.format("%s.%s.%s", getClass().getSimpleName(), testInfo.getTestMethod().get().getName(), scenario.name());
        }
        if(prefix != null) {
            resourceName += "." + prefix;
        }
        resourceName += suffix;
        return Resources.toString(Resources.getResource(getClass(), resourceName), StandardCharsets.UTF_8);
    }



}
