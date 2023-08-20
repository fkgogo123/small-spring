package cn.bugstack.springframework.demo.staticProxy;

public class StudentImpl implements Student {

    @Override
    public void study() {
        System.out.println("学生学习");
    }

    @Override
    public void homework() {
        System.out.println("学生写家庭作业");
    }
}
