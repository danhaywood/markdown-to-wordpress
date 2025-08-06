
package com.danhaywood.md2wp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.danhaywood.md2wp.dom.*;


@Import({Module.class})
@CommandLine.Command(
        name = "md2wp",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Converts Markdown to WordPress-compatible HTML blocks"
)
@SpringBootApplication
@EnableConfigurationProperties({Module.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class Md2WpApp implements Runnable {

    final ResourceLoader resourceLoader;
    final MarkdownToWordpress markdownToWordpress;

    @CommandLine.Parameters(index = "0", description = "Input markdown file")
    private Path inputFile;

    @CommandLine.Parameters(index = "1", description = "Output HTML file")
    private Path outputFile;

    @SneakyThrows
    @Override
    public void run() {
        Resource resource = resourceLoader.getResource(inputFile.toString());
        String wordpressHtml = markdownToWordpress.convert(resource);
        Files.writeString(outputFile, wordpressHtml);
    }

    public static void main(String[] args) {
        SpringApplication.run(Md2WpApp.class, args);
    }

}
