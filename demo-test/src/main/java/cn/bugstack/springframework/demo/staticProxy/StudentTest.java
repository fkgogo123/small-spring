package cn.bugstack.springframework.demo.staticProxy;

public class StudentTest {

    public static void main(String[] args) {
        StudentImpl student = new StudentImpl();
        StudentProxy studentProxy = new StudentProxy();
        studentProxy.setStudent(student);

        studentProxy.study();
        studentProxy.homework();
    }
}
