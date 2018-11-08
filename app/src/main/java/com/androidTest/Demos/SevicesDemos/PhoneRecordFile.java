package com.androidTest.Demos.SevicesDemos;

import java.io.File;

public class PhoneRecordFile {
    // 文件名字
    private String fileName;
    // 文件日期
    private String date;
    // 大小
    private String size;
    // 文件路径
    private File path;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
