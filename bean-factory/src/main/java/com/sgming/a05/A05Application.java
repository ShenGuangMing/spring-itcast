package com.sgming.a05;

import com.sgming.a05.config.Config;
import com.sgming.a05.processor.AtBeanPostProcessor;
import com.sgming.a05.processor.MapperPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class A05Application {
    public static void main(String[] args) throws IOException {
        // GenericApplicationContext 是一个【干净】的容器，没有添加 BeanFactory 后处理器和 Bean 后处理器
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
//        context.registerBean(ComponentScanPostProcessor.class);
        context.registerBean(AtBeanPostProcessor.class);
        context.registerBean(MapperPostProcessor.class);

        //初始化容器
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            log.info(" ========== {}", name);
        }
        //销毁容器
        context.close();
    }
}
