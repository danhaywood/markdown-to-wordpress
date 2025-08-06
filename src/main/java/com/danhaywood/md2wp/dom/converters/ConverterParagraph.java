package com.danhaywood.md2wp.dom.converters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
@Order(200)   // must come after ConverterFigure
class ConverterParagraph extends ConverterAbstract<Paragraph> {
    public ConverterParagraph(Context context) {
        super(Paragraph.class, context, "paragraph");
    }
}
