
package com.danhaywood.md2wp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import com.danhaywood.md2wp.dom.*;


@CommandLine.Command(
        name = "md2wp",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Converts Markdown to WordPress-compatible HTML blocks"
)
@Import({Md2WpModule.class})
@SpringBootApplication
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class Md2WpApp implements Runnable {

    final ResourceLoader resourceLoader;
    final MarkdownToWordpress markdownToWordpress;

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "Input markdown file",
            required = true
    )
    private Path inputFile;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Output HTML file",
            required = true
    )
    private Path outputFile;

    @SneakyThrows
    @Override
    public void run() {
        final var resource = resourceLoader.getResource(inputFile.toUri().toString()); // "file:/.../input.md"
        final var wordpressHtml = markdownToWordpress.convert(resource);
        Files.writeString(outputFile, wordpressHtml);
    }

    public static void main(String[] args) {
        final var ctx = SpringApplication.run(Md2WpApp.class, args);
        final var factory = ctx.getBean(CommandLine.IFactory.class);
        final var app = ctx.getBean(Md2WpApp.class);

        int exit = new CommandLine(app, factory).execute(args);
        System.exit(exit);
    }

}
