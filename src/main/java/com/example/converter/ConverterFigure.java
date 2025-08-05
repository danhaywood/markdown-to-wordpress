package com.example.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public boolean convert(HtmlRenderer renderer, Paragraph node, StringBuilder buf) {
        ImageRef imageRef = (ImageRef) node.getFirstChild();
        String reference = imageRef.getReference().toString();// Causeway welcome page
        Text text = (Text) imageRef.getNext();
        String link = text.getChars().toString();   // (images/causeway-welcome-page.png =400x)
        Pattern pattern = Pattern.compile("^\\(([^\\s]+)\\s*=([^)]+)\\)$");
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            String imagePath = matcher.group(1); // images/causeway-welcome-page.png
            String size = matcher.group(2);      // 400x



        }

        return super.convert(renderer, node, buf);
    }

    @Override
    protected String doConvert(String markdownHtml) {
        return markdownHtml;
    }
}

