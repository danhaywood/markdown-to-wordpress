
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

import com.danhaywood.md2wp.dom.MarkdownToWordpress;

@SpringBootTest(classes = Md2WpModule.class)
@UseReporter(Junit5Reporter.class)
class MarkdownToWordpress_Test extends MarkdownToWordpress_Abstract {


    @RequiredArgsConstructor
    @Getter
    enum Scenario {
        para,
        para2,
        h1,
        h2,
        h3,
        list,
        code_bash,
        code_java,
        separator,
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

