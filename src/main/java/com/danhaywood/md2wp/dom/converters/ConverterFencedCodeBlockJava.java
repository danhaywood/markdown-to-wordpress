package com.danhaywood.md2wp.dom.converters;

import org.springframework.stereotype.Component;

import com.danhaywood.md2wp.ts.Timestamper;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
class ConverterFencedCodeBlockJava extends ConverterFencedCodeBlockAbstract {

    public ConverterFencedCodeBlockJava(Context context) {
        super(context, "java");
    }

}
