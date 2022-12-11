package com.sgming.a04;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// AutowiredAnnotationBeanPostProcessor 运行分析
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //使用下面的方式注入，因为对象是我们自己new的，BeanFactory认为这是成品的Bean，就不会走，创建过程、依赖注入、初始化
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);//解析 ${}

        //1.查找那些属性，方法加了 @Autowired，这称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        //后处理器【组合】了BeanFactory，这是因为在解析@Autorwied是依赖其他Bean，那这些Bean来自于BeanFactory
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
        //postProcessProperties()在package com.sgming.a03.MyBeanPostProcessor有提到过，在依赖注入环节会执行
//        processor.postProcessProperties(null, bean1, "bean1");
//        System.out.println(bean1);
        //通过反射获取 findAutowiringMetadata 方法的对象
        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class
                .getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        //设置权限
        findAutowiringMetadata.setAccessible(true);
        //执行findAutowiringMetadata方法，injectionMetadata里面就有那些元数据加了@Autowired注解
        InjectionMetadata injectionMetadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        //在下面这行代码打断点，debug
        System.out.println();

        //2.调用 InjectionMetadata 来进行依赖注入，注入时按照类型查找值
        injectionMetadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        //3.如何按类型查找值，模仿@Autowired按类型注入(InjectionMetadata.inject()方法做的事情)，去按类型查找依赖的Bean的类型
        //3.1成员变量查找类型
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        //Field field：需要注入的成员变量（通过反射获取）；boolean required：是否时必须的
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        //解决依赖
        Object findBean3 = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(findBean3);

        //3.2方法参数查找类型
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object findBean2 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(findBean2);


        //3.3方法参数时按值传入
        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object findHome = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(findHome);
    }
}
