package com.example.converter;

import com.vladsch.flexmark.ast.Paragraph;

class ConverterParagraph extends Converter.Default<Paragraph> {
    public ConverterParagraph() {
        super(Paragraph.class, "paragraph");
    }
}
