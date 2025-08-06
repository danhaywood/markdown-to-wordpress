package com.danhaywood.md2wp;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

class ConverterHeading1 extends Converter.Default<Heading> {

    public ConverterHeading1(HtmlRenderer htmlRenderer) {
        super(Heading.class, htmlRenderer, "heading");
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getLevel() == 1;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return "";
    }
}
