package com.danhaywood.md2wp.dom.converters;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.Node;

abstract class ConverterFencedCodeBlockAbstract extends ConverterAbstract<FencedCodeBlock> {

    protected ConverterFencedCodeBlockAbstract(Context context, String cssName) {
        super(context, FencedCodeBlock.class, cssName);
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
