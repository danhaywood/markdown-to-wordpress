package com.danhaywood.md2wp.services;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@Component
class ConverterFencedCodeBlockBash extends ConverterFencedCodeBlockAbstract {

    public ConverterFencedCodeBlockBash(HtmlRenderer htmlRenderer) {
        super(htmlRenderer, "bash");
    }

}
