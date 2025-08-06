package com.danhaywood.md2wp.dom.converters;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListItem;

@Component
class ConverterListItem extends ConverterAbstract<ListItem> {
    public ConverterListItem(Context context) {
        super(context, ListItem.class, "list-item");
    }
}
