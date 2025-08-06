package com.danhaywood.md2wp.dom.converters;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListItem;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
class ConverterListItem extends ConverterAbstract<ListItem> {
    public ConverterListItem(Context context) {
        super(ListItem.class, context, "list-item");
    }
}
