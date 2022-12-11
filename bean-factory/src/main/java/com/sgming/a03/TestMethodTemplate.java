package com.sgming.a03;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {
    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        //添加具体的后处理器实现
        beanFactory.addBeanPostProcessor(bean -> {
            System.out.println(">>>>>> 解析 @Autowired");
        });
        beanFactory.getBean();
    }
    // 模板方法 Template Method Pattern
    static class MyBeanFactory {
        private List<BeanPostProcessor> processors = new ArrayList<>();

        public Object getBean() {
            Object bean = new Object();
            System.out.println("构造 " + bean);
            System.out.println("依赖注入 " + bean);
            //再依赖注入后，调用 Bean后处理器 的扩展方法
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }
            System.out.println("初始化 " + bean);
            return bean;
        }
        public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
            //添加对应的自定义的 Bean后处理器
            processors.add(beanPostProcessor);
        }

    }
    //对未知的扩展，定义未接口，让具体的实现去实现它
    static interface BeanPostProcessor {
        void inject(Object bean);// 对依赖注入阶段扩展
    }
}
