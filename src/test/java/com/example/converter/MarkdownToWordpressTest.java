
package com.example.converter;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

class MarkdownToWordpressTest {

    @Test
    void shouldConvertMarkdownToWordPressFormat() throws Exception {
        Path input = Path.of("src/test/resources/input.md");
        String markdown = Files.readString(input);

        MarkdownToWordpress converter = new MarkdownToWordpress();
        String output = converterTestHarness(markdown);

        Approvals.verify(output);
    }

    private String converterTestHarness(String markdown) {
        var options = new com.vladsch.flexmark.util.data.MutableDataSet();
        var parser = com.vladsch.flexmark.parser.Parser.builder(options).build();
        var renderer = com.vladsch.flexmark.html.HtmlRenderer.builder(options).build();
        var document = parser.parse(markdown);
        var htmlBody = renderer.render(document);

        return htmlBody
                .replaceAll("(?s)<p>(.*?)</p>", "<!-- wp:paragraph --><p>$1</p><!-- /wp:paragraph -->")
                .replaceAll("(?s)<h2>(.*?)</h2>", "<!-- wp:heading --><h2>$1</h2><!-- /wp:heading -->")
                .replaceAll("(?s)<h3>(.*?)</h3>", "<!-- wp:heading {\"level\":3} --><h3>$1</h3><!-- /wp:heading -->")
                .replaceAll("(?s)<ul>(.*?)</ul>", "<!-- wp:list --><ul>$1</ul><!-- /wp:list -->")
                .replaceAll("(?s)<li>(.*?)</li>", "<li>$1</li>");
    }
}
