package org.tafia.admin.modules.common.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_XML;

/**
 * Created by Dason on 2018/1/13.
 */
@Configuration
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        fastJsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        fastJsonConverter.setSupportedMediaTypes(Collections.singletonList(APPLICATION_JSON_UTF8));
        messageConverters.add(fastJsonConverter);
        Jaxb2RootElementHttpMessageConverter jaxbConverter = new Jaxb2RootElementHttpMessageConverter();
        jaxbConverter.setDefaultCharset(StandardCharsets.UTF_8);
        jaxbConverter.setSupportedMediaTypes(Collections.singletonList(TEXT_XML));
        messageConverters.add(jaxbConverter);
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
