package com.danhaywood.md2wp.services;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.html.HtmlRenderer;

@Component
class ConverterFencedCodeBlockJava extends ConverterFencedCodeBlockAbstract {

    public ConverterFencedCodeBlockJava(HtmlRenderer htmlRenderer) {
        super(htmlRenderer, "java");
    }

}
