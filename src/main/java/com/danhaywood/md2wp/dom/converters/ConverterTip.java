package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Log4j2
class ConverterTip extends ConverterAbstract<BlockQuote> {

    private final HtmlRenderer htmlRenderer;

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterTip(Context context, HtmlRenderer htmlRenderer) {
        super(context, BlockQuote.class, "quote", null);
        this.htmlRenderer = htmlRenderer;
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node);
    }

    @Override
    public void convert(Resource resource, BlockQuote node, StringBuilder buf) {
        getLog().info("<!-- wp:blockquote -->");

        final var para = node.getFirstChild();
        final var firstChild = para.getFirstChild();
        final var quoteType = htmlRenderer.render(firstChild).replace("[!", "").replaceAll("]", "").trim();
        final var lastChild = para.getLastChild();
        final var markdownHtml = htmlRenderer.render(lastChild).trim();
        buf.append(
                """
                <!-- wp:%s {"canvasClassName":"cnvs-block-core-%s-%s"} -->
                <blockquote class="wp-block-%s">
                   <!-- wp:paragraph {"canvasClassName":"cnvs-block-core-paragraph-%s"} -->
                   <p>%s</p>
                   <!-- /wp:paragraph -->
                   <cite>%s</cite>
                </blockquote>
                <!-- /wp:%s -->
                """.formatted(cssName, cssName, timestamp(),
                        cssName, timestamp(),
                        quoteType,
                        markdownHtml,
                        cssName));
    }

}
