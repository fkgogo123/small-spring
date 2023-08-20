package cn.bugstack.springframework.demo.dynamicProxy;

public class StudentTest {

    public static void main(String[] args) {
        StudentProxy studentProxy = new StudentProxy();
        Student proxyObject = studentProxy.getDynamicProxyObject();
        System.out.println(proxyObject.queryStudentName("001"));
    }
}
