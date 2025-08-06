
package com.danhaywood.md2wp.services;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


@Service
public class MarkdownToWordpress  {

    final ResourceLoader resourceLoader;
    final HtmlRenderer htmlRenderer;

    final List<Converter<?>> converters = new ArrayList<>();

    @Autowired
    public MarkdownToWordpress(final ResourceLoader resourceLoader, final HtmlRenderer htmlRenderer, List<Converter<?>> converters) {
        this.resourceLoader = resourceLoader;
        this.htmlRenderer = htmlRenderer;
        converters.forEach(this.converters::add);
    }

    @NonNull
    public String convert(Resource resource) throws IOException {
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
