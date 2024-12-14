package com.example.canteen.config;

import com.example.canteen.converter.StringToCategoryConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToCategoryConverter stringToCategoryConverter;

    public WebConfig(StringToCategoryConverter stringToCategoryConverter) {
        this.stringToCategoryConverter = stringToCategoryConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToCategoryConverter);
    }
}