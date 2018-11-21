package com.utiles;

public interface LocationInterface {
    /**
     * 定位成功调用方法
     * @param lon 经度
     * @param lat 维度
     */
    public abstract void requestLocationSuccess(String lon,String lat);
}
