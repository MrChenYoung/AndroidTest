package com.utiles;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtil {

    private Context context;

    // 定位管理者
    private LocationManager locationManager;
    // 定位结果监听器
    private MyLocationListener locationListener = new MyLocationListener();
    // 定位结果回调
    private LocationCallback callback;

    public LocationUtil(Context context, LocationCallback callback) {
        this.context = context;
        this.callback = callback;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 定位
     * @param locationStyle 定位类型
     */
    public void requestLocation(final String locationStyle){
        try {
            locationManager.requestLocationUpdates(locationStyle, 5000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();

            // 定位失败
            callback.requestLocationFailed();
        }
    }

    /**
     * 定位监听
     */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // 定位成功
            double lon = location.getLongitude();
            double lat = location.getLatitude();

            // 回调
            callback.requestLocationSuccess(String.valueOf(lon),String.valueOf(lat));

            // 只定位一次,定位成功就取消更新位置
            locationManager.removeUpdates(locationListener);

//            Toast.makeText(context,"111",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 定位状态改变

//            Toast.makeText(context, "222" + provider + ":" + status,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // 定位获取权限
//            Toast.makeText(context,"333",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // 定位没有权限
            callback.requestLocationNeedPermission();

//            Toast.makeText(context,"444",Toast.LENGTH_LONG).show();
        }
    }
}
