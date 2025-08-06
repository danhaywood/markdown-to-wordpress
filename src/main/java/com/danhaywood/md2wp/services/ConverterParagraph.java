package com.danhaywood.md2wp.services;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
@Order(200)   // must come after ConverterFigure
class ConverterParagraph extends Converter.Default<Paragraph> {
    public ConverterParagraph(HtmlRenderer htmlRenderer) {
        super(Paragraph.class, htmlRenderer, "paragraph");
    }
}
