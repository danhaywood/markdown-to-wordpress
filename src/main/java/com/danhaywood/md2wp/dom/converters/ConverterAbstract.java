package com.danhaywood.md2wp.dom.converters;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.danhaywood.md2wp.cal.Calendar;
import com.danhaywood.md2wp.config.Md2WpConfig;
import com.danhaywood.md2wp.ts.Timestamper;
import com.danhaywood.md2wp.wp.WordpressMediaService;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@RequiredArgsConstructor
public abstract class ConverterAbstract<T extends Node> implements Converter<T> {

    @Component
    @RequiredArgsConstructor
    static class Context {
        final Md2WpConfig config;
        final HtmlRenderer htmlRenderer;
        final Timestamper timestamper;
        final Calendar calendar;
        final WordpressMediaService wordpressMediaService;
    }

    final Context context;
    final Class<T> nodeClass;
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
    public void convert(Resource resource, T node, StringBuilder buf) {
        String render = context.htmlRenderer.render(node);
        final var markdownHtml = sanitize(render);
        final var convertedHtml = doConvert(markdownHtml);
        buf.append(convertedHtml);
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

    protected @NotNull String timestamp() {
        return context.timestamper.timestamp();
    }
}
