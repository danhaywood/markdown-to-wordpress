package com.danhaywood.md2wp.dom.converters;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

public interface Converter<T extends Node> {
    boolean supports(Node node);

    default boolean convertNode(Resource resource, Node node, StringBuilder buf) {
        return convert(resource, (T)node, buf);
    }
    boolean convert(Resource resource, T node, StringBuilder buf);
}
