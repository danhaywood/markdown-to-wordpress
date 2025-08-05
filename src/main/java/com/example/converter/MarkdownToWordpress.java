
package com.example.converter;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

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

        Node document = parser.parse(markdown);
        String htmlBody = renderer.render(document);

        String wordpressHtml = wrapWithWordPressBlocks(htmlBody);
        return wordpressHtml;
    }

    private String wrapWithWordPressBlocks(String htmlBody) {
        String trimmed = htmlBody.trim();
        return "<!-- wp:paragraph {\"canvasClassName\":\"cnvs-block-core-paragraph-1754216031731\"} -->\n"
             + trimmed + "\n"
             + "<!-- /wp:paragraph -->\n";
    }
}
