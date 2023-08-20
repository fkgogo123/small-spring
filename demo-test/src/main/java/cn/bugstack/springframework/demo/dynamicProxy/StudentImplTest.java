package cn.bugstack.springframework.demo.dynamicProxy;


public class StudentImplTest {
    public static void main(String[] args) {
        StudentImpl student = new StudentImpl();
        StudentImplProxy studentImplProxy = new StudentImplProxy();
        studentImplProxy.setStudent(student);

        Student proxyObject = (Student) studentImplProxy.getDynamicProxyObject();
        System.out.println(proxyObject.queryStudentName("001"));


    }
}
