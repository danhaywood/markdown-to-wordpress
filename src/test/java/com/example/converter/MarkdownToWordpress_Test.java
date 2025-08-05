
package com.example.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.Junit5Reporter;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.scrubbers.NormalizeSpacesScrubber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@UseReporter(Junit5Reporter.class)
class MarkdownToWordpress_Test extends AbstractTest {

    @RequiredArgsConstructor
    @Getter
    enum Scenario {
        para,
        para2,
        h1,
        h2,
//        list,
//        code_bash,
        ;
    }

    MarkdownToWordpress converter;

    @BeforeEach
    void setUp() {
        converter = new MarkdownToWordpress();
    }

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void each(Scenario scenario) throws Exception {

        // given
        final var markdown = readMd(scenario, "_input");

        // when
        final var output = converter.convert(markdown);

        // then
        Approvals.verifyHtml(output, Approvals.NAMES.withParameters(scenario.name())
                .withScrubber(new NormalizeSpacesScrubber())
                .withScrubber(new TimestampScrubber()));

    }

}

