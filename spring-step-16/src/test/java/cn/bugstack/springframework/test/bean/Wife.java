package cn.bugstack.springframework.test.bean;

public class Wife {

    private Husband husband;
    private IMother mother; // 婆婆
    private String name;

    public String queryHusband() {
        return "Wife.husband、Mother.callMother: " + mother.callMother() + "名称：" + name;
    }
}
