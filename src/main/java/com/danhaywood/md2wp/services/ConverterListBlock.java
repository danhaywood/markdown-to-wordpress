package com.danhaywood.md2wp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@Component
class ConverterListBlock extends Converter.Default<ListBlock> {

    @Autowired
    private List<Converter<?>> converters;

    public ConverterListBlock(HtmlRenderer htmlRenderer) {
        super(ListBlock.class, htmlRenderer, "list-block");
    }

    @Override
    public boolean convert(Resource resource, ListBlock node, StringBuilder buf) {
        buf.append(("<!-- wp:list {\"canvasClassName\":\"cnvs-block-core-list-%s\"} -->" +
                    "\n<ul>").formatted(timestamp()));
        appendConvertedChildren(resource, node, buf);
        buf.append("</ul>" +
                "\n<!-- /wp:list -->");
        return true;
    }

    private void appendConvertedChildren(Resource resource, Node node, StringBuilder buf) {
        node.getChildren()
                .forEach(child -> {
                    Optional<Converter<?>> first = converters.stream()
                            .filter(converter -> converter.supports(child))
                            .filter(converter -> converter.convertNode(resource, child, buf))
                            .findFirst();
                });
    }
}
