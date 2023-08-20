package cn.bugstack.springframework.test.bean;


import java.time.LocalDate;

public class Husband {

    private String wifeName;

    private LocalDate marriageDate; // 添加一个日期类型的转换操作

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    @Override
    public String toString() {
        return "Husband{" +
                "wifiName='" + wifeName + '\'' +
                ", marriageDate=" + marriageDate +
                '}';
    }
}
