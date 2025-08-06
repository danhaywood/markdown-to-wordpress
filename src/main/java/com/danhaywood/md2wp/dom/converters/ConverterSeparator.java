package com.danhaywood.md2wp.dom.converters;

import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.ThematicBreak;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Log4j2
class ConverterSeparator extends ConverterAbstract<ThematicBreak> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterSeparator(Context context) {
        super(context, ThematicBreak.class, "separator", null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node);
    }

    @Override
    public void convert(Resource resource, ThematicBreak node, StringBuilder buf) {
        getLog().info("<!-- wp:separator -->");

        buf.append(
                """
                <!-- wp:%s {"canvasClassName":"cnvs-block-core-%s-%s"} -->
                <hr class="wp-block-%s has-alpha-channel-opacity"/>
                <!-- /wp:%s -->
                """.formatted(cssName, cssName, timestamp(), cssName, cssName));
    }

}
