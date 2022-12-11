package com.sgming.a05.processor;

import com.sgming.a05.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
public class ComponentScanPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    //自己查看 ApplicationContextAware 接口的定义
    private ApplicationContext context;

    //在我们刷新容器的时候会执行, context.refresh()
    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //通过Spring提供的工具类，去查找 Config 类是否有 @ComponentScan 注解
        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);

        if (componentScan == null) {
            return;
        }
        //获取注解的 basePackages：扫描路径
        for (String packagePath : componentScan.basePackages()) {
            log.info("packagePath: {}", packagePath);
            //将包路径->文件路径: com.sgming.a05.component -> classpath*:com/sgming/a05/component/**/*.class
            String filePath = "classpath*:"+packagePath.replace(".", "/") + "/**/*.class";
            log.info("filePath: {}", filePath);
            //读取类的元信息 缓存元数据读取器工厂
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            //通过a01学的 getResources()，根据通配符获取多个资源
            Resource[] resources = null;

            try {//读取文件资源的 try-catch
                resources = context.getResources(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //BeanName生成工具
            AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
            //注意我们这里扫的.class类文件，不是扫描注解，所以有Bean4
            for (Resource resource : resources) {
                MetadataReader reader = null;
                try {//读取资源的元数据失败
                    reader = factory.getMetadataReader(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //getClassMetadata()：获取类元数据
                log.info("类名：{}", reader.getClassMetadata().getClassName());
                //查看类是否直接或间接加了指定的注解
                AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                //log.info("是否加了 @Component 注解：{}", annotationMetadata.hasAnnotation(Component.class.getName()));
                //log.info("是否加了 @Component 派生注解：{}", annotationMetadata.hasMetaAnnotation(Component.class.getName()));

                //这个类是否直接或间接加了 @Component注解
                if (annotationMetadata.hasAnnotation(Component.class.getName())
                        || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                    AbstractBeanDefinition bd = BeanDefinitionBuilder
                            .genericBeanDefinition(reader.getClassMetadata().getClassName())
                            .getBeanDefinition();

                    //生成BeanName的工具
                    String beanName = beanNameGenerator.generateBeanName(bd, registry);
                    registry.registerBeanDefinition(beanName, bd);
                }
            }


        }
    }
}
