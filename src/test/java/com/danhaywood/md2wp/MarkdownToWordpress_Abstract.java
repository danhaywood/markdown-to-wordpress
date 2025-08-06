
package com.danhaywood.md2wp;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.approvaltests.Approvals;
import org.approvaltests.core.Scrubber;
import org.approvaltests.reporters.Junit5Reporter;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.scrubbers.NormalizeSpacesScrubber;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.danhaywood.md2wp.dom.MarkdownToWordpress;

@UseReporter(Junit5Reporter.class)
abstract class MarkdownToWordpress_Abstract {


    protected TestInfo testInfo;

    @BeforeEach
    void captureTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    protected abstract ResourceLoader getResourceLoader();
    protected abstract MarkdownToWordpress getConverter();


    protected Resource readMd(@NonNull Enum<?> scenario, @Nullable String prefix) {
        return readResource(scenario, prefix, ".md");
    }

    protected Resource readHtml(@NonNull Enum<?> scenario, @Nullable String prefix) {
        return readResource(scenario, prefix, ".html");
    }

    @SneakyThrows
    protected Resource readResource(String suffix) {
        return readResource(null, null, suffix);
    }

    @SneakyThrows
    protected Resource readResource(@Nullable Enum<?> scenario, @Nullable String prefix, String suffix) {
        String resourceName;
        if (scenario == null) {
            resourceName = String.format("%s.%s", getClass().getCanonicalName(), testInfo.getTestMethod().get().getName());
        } else {
            resourceName = String.format("%s.%s.%s", getClass().getCanonicalName().replaceAll("[.]", "/"), testInfo.getTestMethod().get().getName(), scenario.name());
        }
        if(prefix != null) {
            resourceName += "." + prefix;
        }
        resourceName += suffix;
        return getResourceLoader().getResource(resourceName);
    }


    protected void verifyHtml(Enum<?> scenario, String output) {
        Approvals.verifyHtml(output, Approvals.NAMES.withParameters(scenario.name())
                .withScrubber(CompositeScrubber.of(
                                new NormalizeSpacesScrubber()
                                ,new TimestampScrubber()
                                ,s -> s.replace(" \n", "\n")
                        )
                )
        );
    }


    private static class CompositeScrubber implements Scrubber {
        private final List<Scrubber> scrubbers = new ArrayList<>();

        public static CompositeScrubber of(Scrubber... scrubbers) {
            return new CompositeScrubber(scrubbers);
        }

        public CompositeScrubber(Scrubber... scrubbers ) {
            this.scrubbers.addAll(Arrays.asList(scrubbers));
        }

        @Override
        public String scrub(String str) {
            for (Scrubber scrubber : scrubbers) {
                str = scrubber.scrub(str);
            }
            return str;
        }
    }

}

