package com.sgming.a05.processor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class AtBeanPostProcessor implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    private ApplicationContext context;


    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //直接扫描config包
        Resource[] resources = null;
        try {
            resources = context.getResources("classpath*:com/sgming/a05/config/**/*.class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //读取类的元信息 缓存元数据读取器工厂
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        for (Resource resource : resources) {
            MetadataReader reader = null;
            try {//读取这个类文件(.class)的元数据
                reader = factory.getMetadataReader(resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
            //判断这个注解是否有 Configuration
            if (annotationMetadata.hasAnnotation(Configuration.class.getName())) {
                //获取这个类有哪些方法加了@Bean
                Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
                for (MethodMetadata method : methods) {
                    //Bean定义生成器
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                    //把Config类理解为工厂类，那些被@Bean注解的是工厂方法，返回生产好的Bean，所以需要 工厂方法名 和 工厂名
                    builder.setFactoryMethodOnBean(method.getMethodName(), "config");
                    //由于工厂方法需要一些参数，需要设置自动装配模式
                    builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                    //设置 BeanDefinition 初始化方法名字
                    String initMethod = (String) method.getAnnotationAttributes(Bean.class.getName()).get("initMethod");
                    if (initMethod.length() != 0) {
                        builder.setInitMethodName(initMethod);
                    }
                    //生成Bean定义
                    AbstractBeanDefinition bd = builder.getBeanDefinition();
                    //在我们使用@Bean注入对象时，我们一般是把方法名作为Bean的名字
                    registry.registerBeanDefinition(method.getMethodName(), bd);
                }
            }

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
