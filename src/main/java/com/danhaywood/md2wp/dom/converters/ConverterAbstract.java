package com.danhaywood.md2wp.dom.converters;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.danhaywood.md2wp.ts.Timestamper;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@RequiredArgsConstructor
public abstract class ConverterAbstract<T extends Node> implements Converter<T> {

    @Component
    @RequiredArgsConstructor
    static class Context {
        final HtmlRenderer htmlRenderer;
        final Timestamper timestamper;
    }

    final Class<T> nodeClass;
    final Context context;
    final String cssName;

    @Override
    public boolean supports(Node node) {
        return nodeClass.isAssignableFrom(node.getClass());
    }

    protected T downcast(Node node) {
        if (nodeClass.isAssignableFrom(node.getClass())) {
            return nodeClass.cast(node);
        }
        throw new IllegalArgumentException("Node is not of type " + nodeClass.getName() + ": " + node);
    }

    @Override
    public boolean convert(Resource resource, T node, StringBuilder buf) {
        String render = context.htmlRenderer.render(node);
        final var markdownHtml = sanitize(render);
        final var convertedHtml = doConvert(markdownHtml);
        buf.append(convertedHtml);
        return true;
    }

    private static @NotNull String sanitize(String render) {
        return render.trim();
    }

    protected String doConvert(String markdownHtml) {
        return
                """
                        <!-- wp:%s {"canvasClassName":"cnvs-block-core-%s-%s"} -->
                        %s
                        <!-- /wp:%s -->
                        """.formatted(cssName, cssName, timestamp(), markdownHtml, cssName);
    }

    protected static @NotNull String timestamp() {
        return String.valueOf(java.time.Instant.now().toEpochMilli());
    }
}
