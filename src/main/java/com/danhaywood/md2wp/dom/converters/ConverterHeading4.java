package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Log4j2
class ConverterHeading4 extends ConverterAbstract<Heading> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterHeading4(Context context) {
        super(context, Heading.class, "heading", "\"level\":4,");
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getLevel() == 4;
    }

    @Override
    protected String doConvert(String markdownHtml) {
        // <!-- wp:heading {"level":4,"canvasClassName":"cnvs-block-core-heading-1754471284200"} -->
        getLog().info("<h4 class=\"wp-block-heading\">");
        return super.doConvert(markdownHtml).replaceAll("<h4>", "<h4 class=\"wp-block-heading\">");
    }
}
