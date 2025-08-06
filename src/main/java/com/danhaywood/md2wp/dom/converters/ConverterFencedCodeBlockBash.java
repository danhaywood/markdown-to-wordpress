package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.danhaywood.md2wp.ts.Timestamper;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
@Log4j2
class ConverterFencedCodeBlockBash extends ConverterFencedCodeBlockAbstract {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterFencedCodeBlockBash(Context context) {
        super(context, "bash");
    }

}
