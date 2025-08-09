package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Log4j2
class ConverterFencedCodeBlockYaml extends ConverterFencedCodeBlockAbstract {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterFencedCodeBlockYaml(Context context) {
        super(context, "yaml");
    }

}
