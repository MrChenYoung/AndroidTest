package com.androidtest.Demos.ContentProviderDemos;

public class SMSInfo {
    // 短信地址
    private String address;
    // 发件人姓名
    private String person;
    // 短信类型
    private int type;
    // 短信内容
    private String body;
    // 短信时间
    private String date;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
