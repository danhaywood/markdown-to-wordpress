
package com.danhaywood.md2wp;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.IOException;
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

    final ResourceLoader resourceLoader;

    final List<Converter<?>> converters = new ArrayList<>();

    @CommandLine.Parameters(index = "0", description = "Input markdown file")
    private Path inputFile;

    @CommandLine.Parameters(index = "1", description = "Output HTML file")
    private Path outputFile;

    @Autowired
    public MarkdownToWordpress(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        final var options = new MutableDataSet();
        final var parser = Parser.builder(options).build();
        final var htmlRenderer = HtmlRenderer.builder(options).build();

        converters.add(new ConverterFigure(htmlRenderer));  // must be before ConverterParagraph.
        converters.add(new ConverterParagraph(htmlRenderer));
        converters.add(new ConverterHeading1(htmlRenderer));
        converters.add(new ConverterHeading2(htmlRenderer));
        converters.add(new ListBlockConverter(htmlRenderer, converters));
        converters.add(new ConverterListItem(htmlRenderer));
        converters.add(new ConverterFencedCodeBlock(htmlRenderer, "bash"));
        converters.add(new ConverterFencedCodeBlock(htmlRenderer, "java"));
    }

    @SneakyThrows
    @Override
    public void run() {
        Resource resource = resourceLoader.getResource(inputFile.toString());
        String wordpressHtml = convert(resource);
        Files.writeString(outputFile, wordpressHtml);
    }

    @NonNull
    String convert(Resource resource) throws IOException {
        String markdown = resource.getContentAsString(StandardCharsets.UTF_8);

        final var options = new MutableDataSet();
        final var parser = Parser.builder(options).build();
        final var htmlRenderer = HtmlRenderer.builder(options).build();

        final var buf = new StringBuilder();
        final var node = parser.parse(markdown);

        appendConvertedChildren(resource, htmlRenderer, node, buf);
        return buf.toString();
    }

    private void appendConvertedChildren(Resource resource, HtmlRenderer renderer, Node node, StringBuilder buf) {
        node.getChildren()
                .forEach(child -> {
                    converters.stream()
                            .filter(converter -> converter.supports(child))
                            .filter(converter -> converter.convertNode(resource, child, buf))
                            .findFirst();
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(MarkdownToWordpress.class, args);
    }

}
