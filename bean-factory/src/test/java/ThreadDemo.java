/*
    需求：模拟取钱案例
 */
public class ThreadDemo {
    public static void main(String[] args){
        //1、定义线程类，创建一个共享的账户对象

        //2、创建2个线程对象，代表小明和小红同时进来了
        for (int i = 0; i < 10000; i++) {
            Account acc = new Account("ICBC-111", 100000);
            new DrawThread(acc, "小明").start();
            new DrawThread(acc, "小红").start();
            if(acc.getMoney() < 0) {
                System.out.println("出现线程安全问题");
            }
        }
    }
}
