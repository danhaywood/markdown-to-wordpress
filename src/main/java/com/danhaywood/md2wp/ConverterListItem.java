package com.danhaywood.md2wp;

import com.vladsch.flexmark.ast.ListItem;
import com.vladsch.flexmark.html.HtmlRenderer;

class ConverterListItem extends Converter.Default<ListItem> {
    public ConverterListItem(HtmlRenderer htmlRenderer) {
        super(ListItem.class, htmlRenderer, "list-item");
    }
}
