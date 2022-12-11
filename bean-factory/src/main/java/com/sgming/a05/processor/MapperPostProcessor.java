package com.sgming.a05.processor;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:com/sgming/a05/mapper/**/*.class");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        //BeanName生成器
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        for (Resource resource : resources) {
            MetadataReader reader = null;
            try {//读取文件资源的元数据
                reader = factory.getMetadataReader(resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassMetadata classMetadata = reader.getClassMetadata();
            if (classMetadata.isInterface()) {//这个类是接口

                AbstractBeanDefinition bd = BeanDefinitionBuilder
                        .genericBeanDefinition(MapperFactoryBean.class)//MapperFactoryBean生成的BeanDefinition
                        .addConstructorArgValue(classMetadata.getClassName())//设置构造参数：对应的Mapper接口
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)//按类型自动装配
                        .getBeanDefinition();//获取BeanDefinition
                //由于我们的命名是对面的Mapper接口名首字母小写
                AbstractBeanDefinition bd2 = BeanDefinitionBuilder
                        .genericBeanDefinition(classMetadata.getClassName())//生成对应的Mapper接口的BeanDefinition
                        .getBeanDefinition();
                String beanName = generator.generateBeanName(bd2, registry);
                registry.registerBeanDefinition(beanName, bd);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
