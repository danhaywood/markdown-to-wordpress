package com.danhaywood.md2wp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.danhaywood.md2wp.config.Md2WpConfig;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

@Configuration
@ComponentScan
@EnableConfigurationProperties(Md2WpConfig.class)
public class Md2WpModule {

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
