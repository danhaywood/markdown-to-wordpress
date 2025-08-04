
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

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        String htmlBody = renderer.render(document);

        String wordpressHtml = wrapWithWordPressBlocks(htmlBody);
        Files.writeString(outputFile, wordpressHtml);
        return 0;
    }

    private String wrapWithWordPressBlocks(String htmlBody) {
        // Naively wrap paragraphs/headings/lists in WordPress block format
        return htmlBody
                .replaceAll("(?s)<p>(.*?)</p>", "<!-- wp:paragraph --><p>$1</p><!-- /wp:paragraph -->")
                .replaceAll("(?s)<h2>(.*?)</h2>", "<!-- wp:heading --><h2>$1</h2><!-- /wp:heading -->")
                .replaceAll("(?s)<h3>(.*?)</h3>", "<!-- wp:heading {\"level\":3} --><h3>$1</h3><!-- /wp:heading -->")
                .replaceAll("(?s)<ul>(.*?)</ul>", "<!-- wp:list --><ul>$1</ul><!-- /wp:list -->")
                .replaceAll("(?s)<li>(.*?)</li>", "<li>$1</li>");
    }
}
