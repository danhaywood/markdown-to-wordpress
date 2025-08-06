package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Log4j2
class ConverterHeading1 extends ConverterAbstract<Heading> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterHeading1(Context context) {
        super(context, Heading.class, "heading", null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getLevel() == 1;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        getLog().info("h1");
        return "";
    }
}
