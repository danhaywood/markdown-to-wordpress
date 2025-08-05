
package com.example.converter;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
            final var markdownHtml = renderer.render(child).trim();
            for (Converter converter : converters) {
                final var convertedHtml = converter.convert(markdownHtml);
                if (convertedHtml != null) {
                    buf.append(convertedHtml);
                    break;
                }
            }
        });
        return buf.toString();
    }

    interface Converter {
        String convert(String html);
    }

    final List<Converter> converters = new ArrayList<>() {{
        add(html -> {
            if (html.startsWith("<p>") && html.endsWith("</p>")) {
                return
                    """
                    <!-- wp:paragraph {"canvasClassName":"cnvs-block-core-paragraph-%s"} -->
                    %s
                    <!-- /wp:paragraph -->
                    """.formatted(timestamp(), html);
            }
            return null;
        });
        add(html -> {
            if (html.startsWith("<h2>") && html.endsWith("</h2>")) {
                return
                        """
                        <!-- wp:heading {"canvasClassName":"cnvs-block-core-heading-%s"} -->
                        %s
                        <!-- /wp:heading -->
                        """.formatted(timestamp(), html)
                                .replaceAll("<h2>", "<h2 class=\"wp-block-heading\">");
            }
            return null;
        });
    }};


    private static @NotNull String timestamp() {
        return String.valueOf(java.time.Instant.now().toEpochMilli());
    }
}
