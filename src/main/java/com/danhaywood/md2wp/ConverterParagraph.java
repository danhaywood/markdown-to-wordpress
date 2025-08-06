package com.danhaywood.md2wp;

import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.html.HtmlRenderer;

class ConverterParagraph extends Converter.Default<Paragraph> {
    public ConverterParagraph(HtmlRenderer htmlRenderer) {
        super(Paragraph.class, htmlRenderer, "paragraph");
    }
}
