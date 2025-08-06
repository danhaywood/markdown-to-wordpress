package com.danhaywood.md2wp.dom.converters;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@Component
class ConverterHeading2 extends ConverterAbstract<Heading> {
    public ConverterHeading2(Context context) {
        super(Heading.class, context, "heading");
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getLevel() == 2;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return super.doConvert(markdownHtml).replaceAll("<h2>", "<h2 class=\"wp-block-heading\">");
    }
}
