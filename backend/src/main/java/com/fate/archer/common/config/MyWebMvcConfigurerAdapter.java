package com.fate.archer.common.config;

import com.fate.archer.common.domain.BusinessConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagesWeb/**").addResourceLocations("file:" + BusinessConstant.FILEADDRESS + "db/image/");
        registry.addResourceHandler("/videoWeb/**").addResourceLocations("file:" + BusinessConstant.FILEADDRESS + "db/video/");
//        registry.addResourceHandler("/imagesWeb/**").addResourceLocations("file:/usr/project/pro_cos/db/image/");
//        registry.addResourceHandler("/videoWeb/**").addResourceLocations("file:/usr/project/pro_cos/db/video/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
