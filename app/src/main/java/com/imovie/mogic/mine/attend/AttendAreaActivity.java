package com.imovie.mogic.mine.attend;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.imovie.mogic.R;
import com.imovie.mogic.card.ChargeSuccessActivity;
import com.imovie.mogic.mine.adapter.WifiAdapter;
import com.imovie.mogic.mine.attend.model.AttendWay;
import com.imovie.mogic.mine.model.AttendAreaModel;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AttendAreaActivity extends AppCompatActivity implements SensorEventListener {
    public static final int MSG_REFRESH = 6;
    private TitleBar titleBar;
    private TextView tvName;
    private TextView tvMyAddress;
    private TextView tvAddress;
    private TextView tvSettingWifi;
    public NoScrollListView lvWifiList;
    public ArrayList<AttendWay> list;
    public WifiAdapter adapter;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0x00000000;
    private static final int accuracyCircleStrokeColor = 0x00000000;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private Marker mMarkerA;
    BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_company);
    BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_my);

    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    private double latitude = 23.106415;
    private double longitude = 113.378623;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_area_activity);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        initView();
        setView();
        initListener();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(19.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_mark_my);
            mBaiduMap.setMyLocationConfigeration((new MyLocationConfiguration(
                    mCurrentMode, false, mCurrentMarker,
                    accuracyCircleFillColor, accuracyCircleStrokeColor)));
            tvMyAddress.setText(location.getAddrStr());
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        adapter.notifyDataSetChanged();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("重新定位");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvSettingWifi = (TextView) findViewById(R.id.tvAddress);
        lvWifiList = (NoScrollListView) findViewById(R.id.lvWifiList);
        tvMyAddress = (TextView) findViewById(R.id.tvMyAddress);

    }

    private void setView(){
        list = new ArrayList<>();
        adapter = new WifiAdapter(this,list);
        lvWifiList.setAdapter(adapter);
        AttendAreaModel areaModel = (AttendAreaModel) getIntent().getSerializableExtra("areaModel");
        if (areaModel.attendWayList.size()>0){
            for(int i=0;i<areaModel.attendWayList.size();i++) {
                if(areaModel.attendWayList.get(i).type==1) {
                    adapter.list.add(areaModel.attendWayList.get(i));
                    adapter.notifyDataSetChanged();
                }else{
                    tvName.setText(areaModel.attendWayList.get(i).name);
                    tvAddress.setText(areaModel.attendWayList.get(i).address);
                    latitude = areaModel.attendWayList.get(i).latitude;
                    longitude = areaModel.attendWayList.get(i).longitude;
                }
            }
        }

        LatLng llA = new LatLng(latitude, longitude);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        mBaiduMap.addOverlay(ooA);
        // 添加圆
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x8891BCF2)
                .center(llA).stroke(new Stroke(5, 0xFF91BCF2))
                .radius(areaModel.distanceRange);
        mBaiduMap.addOverlay(ooCircle);

    }

    private void initListener() {
        tvSettingWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                startActivity(intent);
            }
        });

    }

}

