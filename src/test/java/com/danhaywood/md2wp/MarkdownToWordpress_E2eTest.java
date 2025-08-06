
package com.danhaywood.md2wp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.approvaltests.reporters.Junit5Reporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import com.danhaywood.md2wp.dom.MarkdownToWordpress;

@SpringBootTest(classes = Module.class)
@UseReporter(Junit5Reporter.class)
@ActiveProfiles("private")
class MarkdownToWordpress_E2eTest extends MarkdownToWordpress_Abstract {

    @RequiredArgsConstructor
    @Getter
    enum Scenario {
        figure,
        ;
    }

    @Getter @Autowired ResourceLoader resourceLoader;
    @Getter @Autowired MarkdownToWordpress converter;

    @ParameterizedTest
    @EnumSource(Scenario.class)
    void each(Scenario scenario) throws Exception {

        // given
        final var markdownResource = readMd(scenario, "_input");

        // when
        final var output = converter.convert(markdownResource);

        // then
        verifyHtml(scenario, output);
    }

}

