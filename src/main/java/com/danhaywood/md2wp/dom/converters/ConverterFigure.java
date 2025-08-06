package com.danhaywood.md2wp.dom.converters;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

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
class ConverterFigure extends ConverterAbstract<Paragraph> {

    public ConverterFigure(Context context) {
        super(context, Paragraph.class, null);
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && (downcast(node)).getFirstChild() instanceof ImageRef;
    }

    @SneakyThrows
    @Override
    public void convert(Resource resource, Paragraph node, StringBuilder buf) {

        final var imageRef = (ImageRef) node.getFirstChild();
        final var text = (Text) imageRef.getNext();
        final var link = text.getChars().toString();   // (images/causeway-welcome-page.png =400x)
        final var pattern = Pattern.compile("^\\(([^\\s]+.png)\\s*=([^)]+)\\)$");
        final var matcher = pattern.matcher(link);

        if (matcher.matches()) {
            final var imagePath = matcher.group(1); // images/causeway-welcome-page   (.png is stripped)
            final var scaleToWidth = matcher.group(2);      // 400x

            context.wordpressMediaService.search(imagePath)
                    .ifPresent(item -> buf.append(
                    """
                    <figure class="wp-block-image size-full is-resized"><img src="%s" alt="%s" class="wp-image-%d" style="width:%spx" /></figure>
                    """.formatted(item.getSourceUrlFull(), imagePath, item.getId(), scaleToWidth)
            ));
        }
    }
}

