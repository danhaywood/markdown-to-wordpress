
package com.example.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.approvaltests.Approvals;
import org.approvaltests.core.Scrubber;
import org.approvaltests.reporters.Junit5Reporter;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.scrubbers.NormalizeSpacesScrubber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(classes = MarkdownToWordpress.class)
@UseReporter(Junit5Reporter.class)
class MarkdownToWordpress_Test extends DefaultTest {

    @RequiredArgsConstructor
    @Getter
    enum Scenario {
//        para,
//        para2,
//        h1,
//        h2,
//        list,
//        code_bash,
//        code_java,
        figure,
        ;
    }

    @Autowired
    MarkdownToWordpress converter;

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void each(Scenario scenario) throws Exception {

        // given
        final var markdown = readMd(scenario, "_input");

        // when
        final var output = converter.convert(markdown);

        // then
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

