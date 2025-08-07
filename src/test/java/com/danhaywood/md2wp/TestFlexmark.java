package com.danhaywood.md2wp;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class TestFlexmark {
    public static void main(String[] args) {
        String markdown = "If you open up [http://localhost:8080](http://localhost:8080), you'll see:";

        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, java.util.List.of(com.vladsch.flexmark.ext.autolink.AutolinkExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Document document = parser.parse(markdown);
        String html = renderer.render(document.getFirstChild());

        System.out.println(html);
    }
}
