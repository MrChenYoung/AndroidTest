package com.utiles;

public interface LocationCallback {
    /**
     * 定位成功调用方法
     * @param lon 定位获取到的经度
     * @param lat 定位获取到的维度
     */
    public abstract void requestLocationSuccess(String lon,String lat);

    /**
     * 定位失败
     */
    public abstract void requestLocationFailed();

    /**
     * 没有定位权限
     */
    public abstract void requestLocationNeedPermission();
}
