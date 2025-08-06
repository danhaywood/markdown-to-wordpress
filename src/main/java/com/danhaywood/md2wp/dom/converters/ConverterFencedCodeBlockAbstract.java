package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.Node;

@Log4j2
abstract class ConverterFencedCodeBlockAbstract extends ConverterAbstract<FencedCodeBlock> {

    protected ConverterFencedCodeBlockAbstract(Context context, String cssName) {
        super(context, FencedCodeBlock.class, cssName, null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && cssName.equals((downcast(node)).getInfo().toString());
    }

    @Override
    protected String doConvert(String markdownHtml) {
        getLog().info("<!-- wp:syntaxhighlighter/code {\"language\":\"%s\"}".formatted(cssName));
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
