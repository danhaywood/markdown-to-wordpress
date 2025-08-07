package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Log4j2
class ConverterBlockQuote extends ConverterAbstract<BlockQuote> {


    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterBlockQuote(Context context) {
        super(context, BlockQuote.class, "quote", null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node);
    }

    @Override
    public void convert(Resource resource, BlockQuote node, StringBuilder buf) {

        final var para = node.getFirstChild();

        final var markdownHtmlPara = context.htmlRenderer.render(para);
/*
        <p>
    [!TIP]
Notice that
    <code>@Name</code>
     annotation is once more used for the
    <code>name</code>
     parameter, meaning that the declarative validation rules for name that we saw earlier are automatically applied here.
</p>
*/
        final var pattern = Pattern.compile("\\[!(\\w+)\\]\\s*(.*?)</p>", Pattern.DOTALL);
        final var matcher = pattern.matcher(markdownHtmlPara);

        if (matcher.find()) {
            getLog().info("<!-- wp:blockquote -->");

            final var tag = matcher.group(1);       // The 'XXX' part, e.g., "TIP", "WARNING"
            final var body = matcher.group(2);      // The content from after the marker to </p>
            buf.append(
                    """
                    <!-- wp:%s {"canvasClassName":"cnvs-block-core-%s-%s"} -->
                    <blockquote class="wp-block-%s">
                       <!-- wp:paragraph {"canvasClassName":"cnvs-block-core-paragraph-%s"} -->
                       <p>%s</p>
                       <!-- /wp:paragraph -->
                       <cite>%s</cite>
                    </blockquote>
                    <!-- /wp:%s -->
                    """.formatted(cssName, cssName, timestamp(),
                            cssName, timestamp(),
                            tag,
                            body,
                            cssName));
        } else {
            getLog().warn("Unable to extract para text.");
        }
    }

}
