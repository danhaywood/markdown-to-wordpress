package com.example.converter;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;

class ConverterHeading1 extends Converter.Default<Heading> {
    public ConverterHeading1() {
        super(Heading.class, "heading");
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && ((Heading) node).getLevel() == 1;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return "";
    }
}
