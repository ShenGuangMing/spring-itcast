package com.sgming.a02;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

public class A02Application {
    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testClassPathXmlApplicationContext1();
//        testFileSystemXmlApplicationContext();
//        testFileSystemXmlApplicationContext1();
//        testAnnotationConfigApplicationContext();
        AnnotationConfigServletWebServerApplicationContext();
    }

    private static void AnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }
    static class WebConfig {
        @Bean
        public ServletWebServerFactory servletWebServerFactory() {//内嵌一个web容器
            return new TomcatServletWebServerFactory();
        }
        @Bean
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();//当前这个servlet还不知道运行再那个 web 容器上面
        }
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            //将 DispatcherServlet 注册进 web 容器中
            //"/" 拦截所有请求
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }
        //一个简单的控制器，这里的 Controller 是接口，不是注解
        @Bean("/hello")//里面的参数是匹配的路径
        public Controller controller1() {
            return ((request, response) -> {
                response.getWriter().print("hello");//向页面写一个hello
                return null;
            });
        }
    }

    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigA01.class);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("bean6是否成功注入bean5:");
        System.out.println(context.getBean(Bean6.class).getBean5());
    }

    private static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:b01.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("bean6是否成功注入bean5:");
        System.out.println(context.getBean(Bean6.class).getBean5());
    }

    private static void testClassPathXmlApplicationContext1() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取前:");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        System.out.println("\n读取后:");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    private static void testFileSystemXmlApplicationContext() {
        System.out.println("==========testFileSystemXmlApplicationContext==========");
        //这里之所以没有些绝对路径是因为，run/debug configurations 里面配置了 work directory 配置了项目的路径
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("bean-factory\\src\\main\\resources\\b01.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("bean6是否成功注入bean5:");
        System.out.println(context.getBean(Bean6.class).getBean5());
    }

    private static void testFileSystemXmlApplicationContext1() {
        System.out.println("==========testFileSystemXmlApplicationContext2==========");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取前:");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new FileSystemResource("bean-factory\\src\\main\\resources\\b01.xml"));
        System.out.println("\n读取后:");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    static class ConfigA01 {
        @Bean
        public Bean5 bean5() {
            return new Bean5();
        }

        @Bean
        public Bean6 bean6(Bean5 bean5) {
            Bean6 bean6 = new Bean6();
            bean6.setBean5(bean5);
            return bean6;
        }
    }

    static class Bean5 {
    }

    static class Bean6 {
        private Bean5 bean5;

        public Bean5 getBean5() {
            return bean5;
        }

        public void setBean5(Bean5 bean5) {
            this.bean5 = bean5;
        }
    }
}
