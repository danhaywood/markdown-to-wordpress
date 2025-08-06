
package com.danhaywood.md2wp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import com.danhaywood.md2wp.dom.MarkdownToWordpress;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(classes = Md2WpModule.class)
class MarkdownToWordpress_E2eTest extends MarkdownToWordpress_Abstract implements E2eTest {

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

