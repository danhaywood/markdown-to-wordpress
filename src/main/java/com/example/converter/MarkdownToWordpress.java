
package com.example.converter;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.RequiredArgsConstructor;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;


@CommandLine.Command(name = "markdown-to-wordpress", mixinStandardHelpOptions = true, version = "1.0",
        description = "Converts Markdown to WordPress-compatible HTML blocks")
public class MarkdownToWordpress implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "Input markdown file")
    private Path inputFile;

    @CommandLine.Parameters(index = "1", description = "Output HTML file")
    private Path outputFile;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MarkdownToWordpress()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        String markdown = Files.readString(inputFile);

        String wordpressHtml = convert(markdown);
        Files.writeString(outputFile, wordpressHtml);
        return 0;
    }

    @NonNull
    String convert(String markdown) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        final var buf = new StringBuilder();
        final var node = parser.parse(markdown);

        node.getChildren()
                .forEach(child -> {
                    converters.stream()
                            .filter(converter -> converter.supports(child))
                            .map(converter -> converter.convert(renderer, child)).filter(Objects::nonNull)
                            .findFirst()
                            .ifPresent(buf::append);
        });
        return buf.toString();
    }

    interface Converter {
        boolean supports(Node node);
        String convert(HtmlRenderer renderer, Node node);
    }
    @RequiredArgsConstructor
    abstract static class AbstractConverter implements Converter {
        final Class<? extends Node> nodeClass;

        @Override
        public boolean supports(Node node) {
            return nodeClass.isAssignableFrom(node.getClass());
        }
        @Override
        public String convert(HtmlRenderer renderer, Node node) {
            final var markdownHtml = renderer.render(node).trim();
            return doConvert(markdownHtml);
        }

        protected abstract String doConvert(String markdownHtml);

    }

    final List<Converter> converters = new ArrayList<>() {{
        add(new AbstractConverter(Paragraph.class) {
            @Override
            protected String doConvert(String markdownHtml) {
                return
                        """
                        <!-- wp:paragraph {"canvasClassName":"cnvs-block-core-paragraph-%s"} -->
                        %s
                        <!-- /wp:paragraph -->
                        """.formatted(timestamp(), markdownHtml);
            }
        });
        // h1
        add(new AbstractConverter(Heading.class) {

            @Override
            public boolean supports(Node node) {
                return super.supports(node) && ((Heading) node).getLevel() == 1;
            }

            @Override
            protected String doConvert(String markdownHtml) {
                return "";
            }
        });
        // h2
        add(new AbstractConverter(Heading.class) {

            @Override
            public boolean supports(Node node) {
                return super.supports(node) && ((Heading) node).getLevel() == 2;
            }

            @Override
            protected String doConvert(String markdownHtml) {
                return
                        """
                        <!-- wp:heading {"canvasClassName":"cnvs-block-core-heading-%s"} -->
                        %s
                        <!-- /wp:heading -->
                        """.formatted(timestamp(), markdownHtml)
                        .replaceAll("<h2>", "<h2 class=\"wp-block-heading\">");
            }
        });
    }};


    private static @NotNull String timestamp() {
        return String.valueOf(java.time.Instant.now().toEpochMilli());
    }
}
