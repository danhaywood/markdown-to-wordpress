package com.example.converter;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

interface Converter<T extends Node> {
    boolean supports(Node node);

    default boolean convertNode(Resource resource, HtmlRenderer renderer, Node node, StringBuilder buf) {
        return convert(resource, renderer, (T)node, buf);
    }
    boolean convert(Resource resource, HtmlRenderer renderer, T node, StringBuilder buf);

    @RequiredArgsConstructor
    class Default<T extends Node> implements Converter<T> {
        final Class<T> nodeClass;
        final String cssName;

        @Override
        public boolean supports(Node node) {
            return nodeClass.isAssignableFrom(node.getClass());
        }

        @Override
        public boolean convert(Resource resource, HtmlRenderer renderer, T node, StringBuilder buf) {
            String render = renderer.render(node);
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
}
