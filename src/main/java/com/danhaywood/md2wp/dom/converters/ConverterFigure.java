package com.danhaywood.md2wp.dom.converters;

import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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
        final var reference = imageRef.getReference().toString();// Causeway welcome page
        final var text = (Text) imageRef.getNext();
        final var link = text.getChars().toString();   // (images/causeway-welcome-page.png =400x)
        final var pattern = Pattern.compile("^\\(([^\\s]+.png)\\s*=([^)]+)\\)$");
        final var matcher = pattern.matcher(link);
        if (matcher.matches()) {
            final var imagePath = matcher.group(1); // images/causeway-welcome-page   (.png is stripped)
            final var size = matcher.group(2);      // 400x

            final var imageResource = resource.createRelative(imagePath);

            final var bufferedImage = ImageIO.read(imageResource.getInputStream());

            final var width = bufferedImage.getWidth();
            final var height = bufferedImage.getHeight();

            // <figure class="wp-block-image size-large is-resized"><img src="https://javapro.io/wp-content/uploads/2025/08/causeway-welcome-page-1024x564.png" alt="" class="wp-image-5814" style="width:400px" /></figure>
            final var base = "https://javapro.io/wp-content/uploads/";
            final var currMonth = "2025/08";

            // TODO: scale 1488 --> 1024
            //       scale  820 -->  564

            String fileName = base + currMonth + "/" + imagePath + "-" + width + "x" + height + ".png";

            System.out.println(fileName);

        }

        super.convert(resource, node, buf);
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return markdownHtml;
    }
}

