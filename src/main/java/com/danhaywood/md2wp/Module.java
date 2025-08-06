package com.danhaywood.md2wp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

@Configuration
@ComponentScan
public class Module {

    @Bean
    public MutableDataSet options() {
        return new MutableDataSet();
    }

    @Bean
    public Parser parser(MutableDataSet options) {
        return Parser.builder(options).build();
    }

    @Bean
    public HtmlRenderer htmlRenderer(MutableDataSet options) {
        return HtmlRenderer.builder(options).build();
    }
}
