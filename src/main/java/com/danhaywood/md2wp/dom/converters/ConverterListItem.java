package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListItem;

@Component
@Log4j2
class ConverterListItem extends ConverterAbstract<ListItem> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterListItem(Context context) {
        super(context, ListItem.class, "list-item", null);
    }

}
