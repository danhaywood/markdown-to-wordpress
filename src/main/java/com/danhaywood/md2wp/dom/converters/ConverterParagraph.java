package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Paragraph;

@Component
@Order(200)   // must come after ConverterFigure
@Log4j2
class ConverterParagraph extends ConverterAbstract<Paragraph> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterParagraph(Context context) {
        super(context, Paragraph.class, "paragraph", null);
    }
}
