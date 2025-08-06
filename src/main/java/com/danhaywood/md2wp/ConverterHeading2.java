package com.danhaywood.md2wp;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

class ConverterHeading2 extends Converter.Default<Heading> {
    public ConverterHeading2(HtmlRenderer htmlRenderer) {
        super(Heading.class, htmlRenderer, "heading");
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
