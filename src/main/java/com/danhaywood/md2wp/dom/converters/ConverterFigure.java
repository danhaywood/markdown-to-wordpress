package com.danhaywood.md2wp.dom.converters;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.danhaywood.md2wp.wp.MediaItem;
import com.vladsch.flexmark.ast.ImageRef;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.util.ast.Node;

@Component
@Order(100)   // must come before ConverterParagraph
@Log4j2
class ConverterFigure extends ConverterAbstract<Paragraph> {

    @Override
    public Logger getLog() {
        return log;
    }

    public ConverterFigure(Context context) {
        super(context, Paragraph.class, null, null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getFirstChild() instanceof ImageRef;
    }

    @SneakyThrows
    @Override
    public void convert(Resource resource, Paragraph node, StringBuilder buf) {

        final var imageRef = (ImageRef) node.getFirstChild();
        final var altDesc = imageRef.getReference().toString();
        final var text = (Text) imageRef.getNext();
        final var link = text.getChars().toString();   // (images/causeway-welcome-page.png =400x)
        final var pattern = Pattern.compile("^\\(images/([^\\s]+.png)\\s*=([^)]+)x\\)$");
        final var matcher = pattern.matcher(link);

        if (matcher.matches()) {
            getLog().info("<figure class=\"wp-block-image size-full is-resized\">");
            final var imagePath = matcher.group(1); // causeway-welcome-page
            final var scaleToWidth = matcher.group(2);      // 400x

            Optional<MediaItem> mediaItemIfAny = context.wordpressMediaService.search(imagePath);
            mediaItemIfAny
                    .ifPresent(item -> buf.append(
                    """
                    <figure class="wp-block-image size-full is-resized"><img src="%s" alt="%s" class="wp-image-%d" style="width:%spx" /></figure>
                    """.formatted(item.getSourceUrlFull(), altDesc, item.getId(), scaleToWidth)
            ));
        } else {
            getLog().warn("Could not find image " + imageRef);
        }
    }
}

