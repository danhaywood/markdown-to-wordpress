package com.danhaywood.md2wp.dom.converters;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListBlock;
import com.vladsch.flexmark.util.ast.Node;

@Component
class ConverterListBlock extends ConverterAbstract<ListBlock> {

    @Autowired
    private List<Converter<?>> converters;

    public ConverterListBlock(Context context) {
        super(context, ListBlock.class, "list-block");
    }

    @Override
    public void convert(Resource resource, ListBlock node, StringBuilder buf) {
        buf.append(("<!-- wp:list {\"canvasClassName\":\"cnvs-block-core-list-%s\"} -->" +
                    "\n<ul>").formatted(timestamp()));
        appendConvertedChildren(resource, node, buf);
        buf.append("</ul>" +
                "\n<!-- /wp:list -->");
    }

    private void appendConvertedChildren(Resource resource, Node node, StringBuilder buf) {
        node.getChildren()
                .forEach(child -> {
                    converters.stream()
                            .filter(converter -> converter.supports(child))
                            .findFirst()
                            .ifPresent(converter -> converter.convertNode(resource, child, buf));
                });
    }
}
