package cn.bugstack.springframework.demo.staticProxy;

// 静态代理
public class StudentProxy implements Student {

    public void setStudent(Student student) {
        this.student = student;
    }

    private Student student;

    @Override
    public void study() {
        System.out.println("学生拿出书，翻开课本...");
        student.study();
    }

    @Override
    public void homework() {
        System.out.println("学生乘坐公交车回到家，开始写作业...");
        student.homework();
    }
}
