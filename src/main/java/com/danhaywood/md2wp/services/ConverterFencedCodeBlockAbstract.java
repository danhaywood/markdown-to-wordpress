package com.danhaywood.md2wp.services;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

abstract class ConverterFencedCodeBlockAbstract extends Converter.Default<FencedCodeBlock> {

    protected ConverterFencedCodeBlockAbstract(HtmlRenderer htmlRenderer, String cssName) {
        super(FencedCodeBlock.class, htmlRenderer, cssName);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && cssName.equals((downcast(node)).getInfo().toString());
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return """
                <!-- wp:syntaxhighlighter/code {"language":"%s"} -->
                %s
                <!-- /wp:syntaxhighlighter/code -->
                """.formatted(cssName, markdownHtml)
                .replaceAll("<pre><code class=\"language-" + cssName + "\">", "<pre class=\"wp-block-syntaxhighlighter-code\">")
                .replaceAll("</code></pre>", "</pre>")
                ;
    }
}
