package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Log4j2
class ConverterFencedCodeBlockTxt extends ConverterFencedCodeBlockAbstract {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterFencedCodeBlockTxt(Context context) {
        super(context, "txt", "");
    }

}
