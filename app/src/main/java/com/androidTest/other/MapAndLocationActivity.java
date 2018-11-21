package com.androidTest.other;

import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;
import com.androidTest.main.PermissionBaseActivity;
import com.utiles.LocationInterface;
import com.utiles.PermissonUtil;

import pub.devrel.easypermissions.AfterPermissionGranted;

public class MapAndLocationActivity extends PermissionBaseActivity implements LocationInterface {

    private TextView tv_net_lon;
    private TextView tv_net_lat;
    private TextView tv_station_lon;
    private TextView tv_station_lat;
    private TextView tv_gps_lon;
    private TextView tv_gps_lat;

    private final int REQUEST_LOCATION_GPS_CODE = 500;
    private final int REQUEST_LOCATION_STATION_CODE = 600;
    private final int REQUEST_LOCATION_NET_CODE = 700;

    private final int GPS_LOCATION_SUCCESS = 1;
    private final int STATION_LOCATION_SUCCESS = 2;
    private final int NET_LOCATION_SUCCESS = 3;
    private final int GPS_LOCATION_FAILE = 4;
    private final int STATION_LOCATION_FAILE = 5;
    private final int NET_LOCATION_FAILE = 6;
    private final int LOCATION_NO_PERMISSION = 7;


    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String locationResult = (String) msg.obj;
            String[] result = locationResult.split(",");
            String tipMessage = null;
            switch (msg.what){
                case GPS_LOCATION_SUCCESS:
                        tv_gps_lon.setText(result[0]);
                        tv_gps_lat.setText(result[1]);
                    break;
                case NET_LOCATION_SUCCESS:
                    tv_net_lon.setText(result[0]);
                    tv_net_lat.setText(result[1]);
                    break;
                case STATION_LOCATION_SUCCESS:
                    tv_station_lat.setText(result[1]);
                    tv_station_lon.setText(result[0]);
                    break;
                case GPS_LOCATION_FAILE:
                    tipMessage = "GPS定位失败";
                    break;
                case NET_LOCATION_FAILE:
                    tipMessage = "网络定位失败";
                    break;
                case STATION_LOCATION_FAILE:
                    tipMessage = "基站定位失败";
                    break;
                case LOCATION_NO_PERMISSION:
                    tipMessage = "没有定位权限,请检查GPS是否开启";
                    break;
            }

            if (tipMessage != null){
                Toast.makeText(getApplicationContext(),tipMessage,Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_location);

        // 初始化界面
        initUI();

        // GPS定位
        gpsLocation();

        // 基站定位
        baseStationLocation();

        // 网络定位
        networkLocation();
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
    }

    /**
     * GPS定位
     */
    @AfterPermissionGranted(REQUEST_LOCATION_GPS_CODE)
    private void gpsLocation(){
        if (PermissonUtil.hasPermissions(this,this, REQUEST_LOCATION_GPS_CODE,"请求访问GPS",new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION})){
            requestLocation(LocationManager.GPS_PROVIDER);
        }
    }

    /**
     * 基站定位
     */
    @AfterPermissionGranted(REQUEST_LOCATION_STATION_CODE)
    private void baseStationLocation(){
        if (PermissonUtil.hasPermissions(this,this,REQUEST_LOCATION_STATION_CODE,"请求使用GPS",new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION})){
            requestLocation(LocationManager.PASSIVE_PROVIDER);
        }

    }

    /**
     * 网络定位
     */
    @AfterPermissionGranted(REQUEST_LOCATION_NET_CODE)
    private void networkLocation(){
        if (PermissonUtil.hasPermissions(this,this,REQUEST_LOCATION_NET_CODE,"需要定位权限",new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION})){
            requestLocation(LocationManager.NETWORK_PROVIDER);
        }
    }



}
