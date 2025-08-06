package com.danhaywood.md2wp.services;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.ListItem;
import com.vladsch.flexmark.html.HtmlRenderer;

@Component
class ConverterListItem extends Converter.Default<ListItem> {
    public ConverterListItem(HtmlRenderer htmlRenderer) {
        super(ListItem.class, htmlRenderer, "list-item");
    }
}
