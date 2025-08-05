
package com.example.converter;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.SneakyThrows;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Component
@CommandLine.Command(
        name = "markdown-to-wordpress",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Converts Markdown to WordPress-compatible HTML blocks"
)
@SpringBootApplication
public class MarkdownToWordpress implements Runnable {

    private final ResourceLoader resourceLoader;

    final List<Converter<?>> converters = new ArrayList<>();

    @CommandLine.Parameters(index = "0", description = "Input markdown file")
    private Path inputFile;

    @CommandLine.Parameters(index = "1", description = "Output HTML file")
    private Path outputFile;

    @Autowired
    public MarkdownToWordpress(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        converters.add(new ConverterFigure(resourceLoader));  // must be before ConverterParagraph.
        converters.add(new ConverterParagraph());
        converters.add(new ConverterHeading1());
        converters.add(new ConverterHeading2());
        converters.add(new ListBlockConverter());
        converters.add(new Converter.Default<>(ListItem.class, "list-item"));
        converters.add(new ConverterFencedCodeBlock("bash"));
        converters.add(new ConverterFencedCodeBlock("java"));
    }

    @SneakyThrows
    @Override
    public void run() {
        Resource resource = resourceLoader.getResource(inputFile.toString());
        String markdown = resource.getContentAsString(StandardCharsets.UTF_8);
        String wordpressHtml = convert(markdown);
        Files.writeString(outputFile, wordpressHtml);
    }

    @NonNull
    String convert(String markdown) {
        final var options = new MutableDataSet();
        final var parser = Parser.builder(options).build();
        final var htmlRenderer = HtmlRenderer.builder(options).build();

        final var buf = new StringBuilder();
        final var node = parser.parse(markdown);

        appendConvertedChildren(htmlRenderer, node, buf);
        return buf.toString();
    }

    private void appendConvertedChildren(HtmlRenderer renderer, Node node, StringBuilder buf) {
        node.getChildren()
                .forEach(child -> {
                    converters.stream()
                            .filter(converter -> converter.supports(child))
                            .filter(converter -> converter.convertNode(renderer, child, buf))
                            .findFirst();
        });
    }


    private class ListBlockConverter extends Converter.Default<ListBlock> {
        public ListBlockConverter() {
            super(ListBlock.class, "list-block");
        }

        @Override
        public boolean convert(HtmlRenderer renderer, ListBlock node, StringBuilder buf) {
            buf.append("<!-- wp:list {\"canvasClassName\":\"cnvs-block-core-list-" +
                    timestamp() +
                    "\"} -->\n" +
                    "<ul>");
            appendConvertedChildren(renderer, node, buf);
            buf.append("</ul>\n" +
                       "<!-- /wp:list -->");
            return true;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MarkdownToWordpress.class, args);
    }

}
