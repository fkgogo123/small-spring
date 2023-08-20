package cn.bugstack.springframework.demo.reflect;


public class StudentImpl implements Student {

    @Override
    public void sayHello(String name) {
        System.out.println("你好：" + name + "。 ");
    }
}
