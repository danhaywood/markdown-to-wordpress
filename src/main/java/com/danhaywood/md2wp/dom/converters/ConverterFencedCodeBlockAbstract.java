package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import java.util.Objects;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.Node;

@Log4j2
abstract class ConverterFencedCodeBlockAbstract extends ConverterAbstract<FencedCodeBlock> {

    private final String cssAlternative;
    protected ConverterFencedCodeBlockAbstract(Context context, String cssName) {
        this(context, cssName, null);
    }
    protected ConverterFencedCodeBlockAbstract(Context context, String cssName, String cssAlternative) {
        super(context, FencedCodeBlock.class, cssName, null);
        this.cssAlternative = cssAlternative;
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && cssName.equals((downcast(node)).getInfo().toString());
    }

    @Override
    protected String doConvert(String markdownHtml) {
        getLog().info("<!-- wp:syntaxhighlighter/code");
        return """
                <!-- wp:syntaxhighlighter/code %s-->
                %s
                <!-- /wp:syntaxhighlighter/code -->
                """.formatted(language(), markdownHtml)
                .replaceAll("<pre><code class=\"language-" + cssName + "\">", "<pre class=\"wp-block-syntaxhighlighter-code\">")
                .replaceAll("</code></pre>", "</pre>")
                ;
    }

    private String language() {
        if(Objects.equals(cssAlternative, "")) {
            return "";
        }
        return "{\"language\":\"%s\"} ".formatted(cssAlternative != null ? cssAlternative : cssName);
    }
}
