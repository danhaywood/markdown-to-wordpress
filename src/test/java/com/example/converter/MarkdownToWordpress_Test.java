
package com.example.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.Junit5Reporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@UseReporter(Junit5Reporter.class)
class MarkdownToWordpress_Test extends AbstractTest {

    @RequiredArgsConstructor
    @Getter
    enum Scenario {
        para("para.md"),
        ;

        final String input;
    }

    MarkdownToWordpress converter;

    @BeforeEach
    void setUp() {
        converter = new MarkdownToWordpress();
    }

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void each(Scenario scenario) throws Exception {

        final var markdown = readMd(scenario, "_input");

        final var output = converter.convert(markdown);

        // TODO: this will need refinement to scrub out the unique identifiers of the approved responses vs expected.
        Approvals.verifyHtml(markdown, Approvals.NAMES.withParameters(scenario.name()));

    }
}


