package com.utiles;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;

public class LocationUtil {

    private Context context;
    private MyHandler mHandler = new MyHandler();

    private final int REQUEST_LOCATION_SUCCESS = 1;
    private final int REQUEST_LOCATION_FAILED = 0;
    private final int REQUEST_LOCATION_NO_PERMISSION = -1;

    public LocationUtil(Context context) {
        this.context = context;
    }

    /**
     * 定位
     * @param locationStyle 定位类型
     */
    private void requestLocation(final String locationStyle){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(locationStyle, 5000, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double lon = location.getLongitude();
                            double lat = location.getLatitude();

                            Message msg = Message.obtain();
                            msg.obj = lon + "," + lat;
//                            switch (locationStyle){
//                                case LocationManager.GPS_PROVIDER:
//                                    msg.what = GPS_LOCATION_SUCCESS;
//                                    break;
//                                case LocationManager.NETWORK_PROVIDER:
//                                    msg.what = NET_LOCATION_SUCCESS;
//                                    break;
//                                case LocationManager.PASSIVE_PROVIDER:
//                                    msg.what = STATION_LOCATION_SUCCESS;
//                                    break;
//                            }
                            msg.what = REQUEST_LOCATION_SUCCESS;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Message message = Message.obtain();
                            message.what = REQUEST_LOCATION_NO_PERMISSION;
                            mHandler.sendMessage(message);
                        }
                    });
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
//                    switch (locationStyle){
//                        case LocationManager.GPS_PROVIDER:
//                            msg.what = GPS_LOCATION_FAILE;
//                            break;
//                        case LocationManager.NETWORK_PROVIDER:
//                            msg.what = NET_LOCATION_FAILE;
//                            break;
//                        case LocationManager.PASSIVE_PROVIDER:
//                            msg.what = STATION_LOCATION_FAILE;
//                            break;
//                    }
                    msg.what = REQUEST_LOCATION_FAILED;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * handler
     */
    private class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case REQUEST_LOCATION_SUCCESS:
                    // 定位成功

                    break;
                case REQUEST_LOCATION_FAILED:
                    // 定位失败

                    break;
                case REQUEST_LOCATION_NO_PERMISSION:
                    // 没有定位权限

                    break;
            }
        }
    }

    @Override
    public void requestLocationSuccess(String lon, String lat) {

    }
}
