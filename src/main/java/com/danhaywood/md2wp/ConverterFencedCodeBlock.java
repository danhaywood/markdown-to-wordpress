package com.danhaywood.md2wp;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

class ConverterFencedCodeBlock extends Converter.Default<FencedCodeBlock> {

    public ConverterFencedCodeBlock(HtmlRenderer htmlRenderer, String cssName) {
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
