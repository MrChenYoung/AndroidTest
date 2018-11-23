package com.androidTest.other;

import android.Manifest;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;
import com.androidTest.main.PermissionBaseActivity;
import com.utiles.LocationCallback;
import com.utiles.LocationModifyOffset;
import com.utiles.LocationPointDouble;
import com.utiles.LocationUtil;
import com.utiles.PermissonUtil;

import java.io.InputStream;

import pub.devrel.easypermissions.AfterPermissionGranted;

public class MapAndLocationActivity extends PermissionBaseActivity {

    private TextView tv_net_lon;
    private TextView tv_net_lat;
    private TextView tv_station_lon;
    private TextView tv_station_lat;
    private TextView tv_gps_lon;
    private TextView tv_gps_lat;
    private TextView tv_best_title;
    private TextView tv_best_lon;
    private TextView tv_best_lat;
    private TextView tv_s2c_title;
    private TextView tv_s2c_lon;
    private TextView tv_s2c_lat;

    private final int REQUEST_LOCATION_CODE = 500;

    // GPS定位
    private LocationUtil gpsLocationUtil;
    // 基站定位
    private LocationUtil stationLocUtil;
    // 网络定位
    private LocationUtil netLocationUtil;
    // 从系统获取最优的定位方式
    private LocationUtil bestLocationUtil;
    // 加载loading
    private RelativeLayout cover;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_location);

        // 初始化界面
        initUI();

        // 初始化定位管理器
        initLocationManagers();

        // GPS定位
        gpsLocation();
    }

    /**
     * 初始化界面
     */
    private void initUI(){
        // 网络定位
        LinearLayout netLocation = findViewById(R.id.net_location);
        TextView tv_net_title = netLocation.findViewById(R.id.tv_title);
        tv_net_title.setText("网络定位");
        tv_net_lon = netLocation.findViewById(R.id.tv_longitude);
        tv_net_lat = netLocation.findViewById(R.id.tv_latitude);

        // 基站定位
        LinearLayout stationLocation = findViewById(R.id.base_station_location);
        TextView tv_station_title = stationLocation.findViewById(R.id.tv_title);
        tv_station_title.setText("基站定位");
        tv_station_lon = stationLocation.findViewById(R.id.tv_longitude);
        tv_station_lat = stationLocation.findViewById(R.id.tv_latitude);

        // GPS定位
        LinearLayout gpsLocation = findViewById(R.id.GPS_location);
        TextView tv_gps_title = gpsLocation.findViewById(R.id.tv_title);
        tv_gps_title.setText("GPS定位");
        tv_gps_lon = gpsLocation.findViewById(R.id.tv_longitude);
        tv_gps_lat = gpsLocation.findViewById(R.id.tv_latitude);

        // 由系统决定最优的定位方式
        LinearLayout bestLocation = findViewById(R.id.best_location);
        tv_best_title = bestLocation.findViewById(R.id.tv_title);
        tv_best_title.setText("最优的定位方式");
        tv_best_lon = bestLocation.findViewById(R.id.tv_longitude);
        tv_best_lat = bestLocation.findViewById(R.id.tv_latitude);

        // GPS定位出来的标准坐标转换成火星坐标
        LinearLayout s2cLocation = findViewById(R.id.s2c_location);
        tv_s2c_title = s2cLocation.findViewById(R.id.tv_title);
        tv_s2c_title.setText("GPS定位的标准坐标转换成火星坐标");
        tv_s2c_lon = s2cLocation.findViewById(R.id.tv_longitude);
        tv_s2c_lat = s2cLocation.findViewById(R.id.tv_latitude);

        // 获取加载loading
        cover = findViewById(R.id.cover);
    }

    /**
     * 初始化定位管理器
     */
    private void initLocationManagers(){
        // GPS定位
        gpsLocationUtil = new LocationUtil(this,new GPSLocationCallback());
        // 基站定位
        stationLocUtil = new LocationUtil(this,new StationLocationCallback());
        // 网络定位
        netLocationUtil = new LocationUtil(this,new NetLocationCallback());
        // 从系统获取最优的定位方式
        bestLocationUtil = new LocationUtil(this,new BestLocationCallback());
    }

    /**
     * GPS定位
     */
    @AfterPermissionGranted(REQUEST_LOCATION_CODE)
    private void gpsLocation(){
        if (PermissonUtil.hasPermissions(this,this, REQUEST_LOCATION_CODE,"请求访问GPS",new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION})){
            cover.setVisibility(View.VISIBLE);
            gpsLocationUtil.requestLocation(LocationManager.GPS_PROVIDER);
        }
    }


    /**
     * 系统决定最优定位方式
     */
    private void bestLocation(){
        // 获取系统最有定位方式
        Criteria criteria = new Criteria();
        // 设置定位精度一般
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 允许使用流量,网络定位
        criteria.setCostAllowed(true);
        // 获取最优的定位方式
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String bestProvider = manager.getBestProvider(criteria,true);
        String bestLocationStyle = "";
        if (bestProvider.equals(LocationManager.GPS_PROVIDER)){
            bestLocationStyle = "GPS定位";
        }else if (bestProvider.equals(LocationManager.PASSIVE_PROVIDER)){
            bestLocationStyle = "基站定位";
        }else if (bestProvider.equals(LocationManager.NETWORK_PROVIDER)){
            bestLocationStyle = "网络定位";
        }
        tv_best_title.setText("最优的定位方式(" + bestLocationStyle + ")");

        // 最优方式定位
        bestLocationUtil.requestLocation(bestProvider);
    }

    /**
     * GPS定位回调类
     */
    private class GPSLocationCallback implements LocationCallback {
        @Override
        public void requestLocationSuccess(String lon, String lat) {
            tv_gps_lon.setText(lon);
            tv_gps_lat.setText(lat);

            // GPS定位成功，开始基站定位(由于系统LocationManager的特殊性,不能并行请求定位)
            stationLocUtil.requestLocation(LocationManager.PASSIVE_PROVIDER);

            // 标准坐标转火星坐标
            locations2c(lon,lat);
        }

        @Override
        public void requestLocationFailed() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"GPS定位失败",Toast.LENGTH_LONG).show();
        }

        @Override
        public void requestLocationNeedPermission() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"没有定位权限,请检查GPS是否开启",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 基站定位回调类
     */
    private class StationLocationCallback implements LocationCallback {
        @Override
        public void requestLocationSuccess(String lon, String lat) {
            tv_station_lat.setText(lat);
            tv_station_lon.setText(lon);

            // 基站定位成功,开始网络(WIFI定位)
            netLocationUtil.requestLocation(LocationManager.NETWORK_PROVIDER);
        }

        @Override
        public void requestLocationFailed() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"基站定位失败",Toast.LENGTH_LONG).show();
        }

        @Override
        public void requestLocationNeedPermission() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"没有定位权限,请检查GPS是否开启",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 网络定位回调类
     */
    private class NetLocationCallback implements LocationCallback {
        @Override
        public void requestLocationSuccess(String lon, String lat) {
            tv_net_lon.setText(lon);
            tv_net_lat.setText(lat);

            // 网络定位成功，开始系统最优定位
            bestLocation();
        }

        @Override
        public void requestLocationFailed() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"网络定位失败",Toast.LENGTH_LONG).show();
        }

        @Override
        public void requestLocationNeedPermission() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"没有定位权限,请检查GPS是否开启",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 网络定位回调类
     */
    private class BestLocationCallback implements LocationCallback {
        @Override
        public void requestLocationSuccess(String lon, String lat) {
            tv_best_lon.setText(lon);
            tv_best_lat.setText(lat);

            // 系统最优方案定位成功
            cover.setVisibility(View.GONE);
        }

        @Override
        public void requestLocationFailed() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"最优定位方式失败",Toast.LENGTH_LONG).show();
        }

        @Override
        public void requestLocationNeedPermission() {
            cover.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"没有定位权限,请检查GPS是否开启",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 标准坐标转换成火星坐标
     */
    private void locations2c(final String lon,final String lat){
        // 标准坐标转换成火星坐标
        new Thread(){
            @Override
            public void run() {
                try {
                    InputStream is = getResources().openRawResource(R.raw.axisoffset);
                    LocationModifyOffset instance = LocationModifyOffset.getInstance(is);
                    final LocationPointDouble pointDouble = instance.s2c(new LocationPointDouble(Double.parseDouble(lon),Double.parseDouble(lat)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_s2c_lon.setText(String.valueOf(pointDouble.getX()));
                            tv_s2c_lat.setText(String.valueOf(pointDouble.getY()));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"标准坐标转火星坐标失败",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();

    }
}
