package com.danhaywood.md2wp;

import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;

import com.vladsch.flexmark.ast.ListBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

class ListBlockConverter extends Converter.Default<ListBlock> {
    private final List<Converter<?>> converters;

    public ListBlockConverter(HtmlRenderer htmlRenderer, List<Converter<?>> converters) {
        super(ListBlock.class, htmlRenderer, "list-block");
        this.converters = converters;
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
