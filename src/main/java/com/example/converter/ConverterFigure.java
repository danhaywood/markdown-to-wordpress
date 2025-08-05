package com.example.converter;

import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.vladsch.flexmark.ast.ImageRef;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;

class ConverterFigure extends Converter.Default<Paragraph> {

    private final ResourceLoader resourceLoader;

    public ConverterFigure(ResourceLoader resourceLoader) {
        super(Paragraph.class, null);
        this.resourceLoader = resourceLoader;
    }

    @Override
    public boolean supports(Node node) {
        return super.supports(node) && ((Paragraph)node).getFirstChild() instanceof ImageRef;
    }

    @SneakyThrows
    @Override
    public boolean convert(Resource resource, HtmlRenderer renderer, Paragraph node, StringBuilder buf) {
        ImageRef imageRef = (ImageRef) node.getFirstChild();
        String reference = imageRef.getReference().toString();// Causeway welcome page
        Text text = (Text) imageRef.getNext();
        String link = text.getChars().toString();   // (images/causeway-welcome-page.png =400x)
        Pattern pattern = Pattern.compile("^\\(([^\\s]+.png)\\s*=([^)]+)\\)$");
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            String imagePath = matcher.group(1); // images/causeway-welcome-page   (.png is stripped)
            String size = matcher.group(2);      // 400x

            final var imageResource = resource.createRelative(imagePath);

            BufferedImage bufferedImage = ImageIO.read(imageResource.getInputStream());

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // <figure class="wp-block-image size-large is-resized"><img src="https://javapro.io/wp-content/uploads/2025/08/causeway-welcome-page-1024x564.png" alt="" class="wp-image-5814" style="width:400px" /></figure>
            String base = "https://javapro.io/wp-content/uploads/";
            String currMonth = "2025/08";

            // TODO: scale 1488 --> 1024
            //       scale  820 -->  564

            String fileName = base + currMonth + "/" + imagePath + "-" + width + "x" + height + ".png";

            System.out.println(fileName);


        }

        return super.convert(resource, renderer, node, buf);
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return markdownHtml;
    }
}

