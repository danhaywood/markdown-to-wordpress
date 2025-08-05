package com.example.converter;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;

class ConverterHeading2 extends Converter.Default<Heading> {
    public ConverterHeading2() {
        super(Heading.class, "heading");
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && ((Heading) node).getLevel() == 2;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return super.doConvert(markdownHtml).replaceAll("<h2>", "<h2 class=\"wp-block-heading\">");
    }
}
