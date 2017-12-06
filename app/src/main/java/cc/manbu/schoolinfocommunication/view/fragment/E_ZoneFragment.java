package cc.manbu.schoolinfocommunication.view.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Text;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseMapFragment;
import cc.manbu.schoolinfocommunication.bean.Device_Geography;
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.tools.Utils;
import cn.yc.base.view.custom.percent.PercentLinearLayout;
import cn.yc.base.view.custom.percent.PercentRelativeLayout;
import cc.manbu.schoolinfocommunication.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class E_ZoneFragment extends BaseMapFragment implements AMap.OnMapClickListener{
    public View rootView;
    public LayoutInflater layoutInflater;
    public ArrayList<BitmapDescriptor> markerList = new ArrayList<>();
    public Marker marker;
    public Device_Geography geography = null;
    public float zoom = 15;
    public MobileDevicAndLocation curDevice;
    public ArrayList<LatLng> fencePoints = new ArrayList<>();
    public boolean mAdd = true;
    public int currentIndex = -1;
    public Circle circle;
    public Polygon polyline;
    public int radius = 500;//半径，最小500，单位：米
    public int strokeColor = Color.RED;
    public int fillColor = Color.argb(127, 255, 255, 204);
    public int strokeWidth = 3;
    public boolean isFirst = true;
    public String nameString;
    public int alertType = 2;//默认进出围栏报警
    public int shape = 1; //1为多边形，2为圆形

    @BindView(R2.id.zone_alert)
    public TextView mZoneAlert;
    @BindView(R2.id.view_map)
    public LinearLayout view_map;
    @BindView(R2.id.id_llSeekbar)
    public LinearLayout id_llSeekbar;
    @BindView(R2.id.id_sbRadius)
    public SeekBar id_sbRadius;
    @BindView(R2.id.id_llBottom)
    public LinearLayout id_llBottom;
    @BindView(R2.id.id_llZoneName)
    public RelativeLayout id_llZoneName;
    @BindView(R2.id.id_tvZoneName)
    public TextView id_tvZoneName;
    @BindView(R2.id.id_tvClear)
    public TextView id_tvClear;
    @BindView(R2.id.id_tvAddress)
    public TextView id_tvAddress;
    @BindView(R2.id.id_tvSetArea)
    public TextView id_tvSetArea;
    public E_ZoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_ezone,container,false);
            ButterKnife.bind(this,rootView);
        }
        EventBus.getDefault().register(this);
        layoutInflater = inflater;
        if (getMGaodeMapView() == null) {
            LinearLayout mapRoot=(LinearLayout) rootView.findViewById(R.id.view_map);
            MapView mGaodeMapView=(MapView) mapRoot.findViewById(R.id.map_view);
            //mapRoot.addView(mGaodeMapView, lp);
            setMGaodeMapView(mGaodeMapView);
        }
        curDevice = Configs.get(Configs.Config.CurDevice,MobileDevicAndLocation.class);
        geography = new Device_Geography();
        onGaodeMapViewCreate(savedInstanceState);
        if(mZoneAlert!=null){
            mZoneAlert.setAlpha(0.6f);
        }
        initView();
        ButterKnife.bind(this,rootView);
        return rootView;
    }
    public void initView(){
        if (curDevice != null){
            id_tvAddress.setText(curDevice.getAddress());
        }
        if (Utils.CurGeography != null){
            mAdd = false;
            mZoneAlert.setVisibility(View.GONE);
            id_llBottom.setVisibility(View.GONE);
            id_llSeekbar.setVisibility(View.GONE);
            showGIS();
        }else {
            mAdd = true;
            mZoneAlert.setVisibility(View.VISIBLE);
            id_llBottom.setVisibility(View.VISIBLE);
        }
        id_tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fencePoints.size() > 0)
                    doClear();
                radius = 500;
            }
        });
        id_sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = 500 + progress * 5;
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (circle != null)
                circle.remove();
            addCircle();
        }
    };

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClick();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            doClear();
            initView();
            isFirst = true;
            initDeviceInMap();
        }
    }

    @Override
    public void onDestroy() {
        if(mTime!=null){
            mTime.cancel();
            mTime=null;
        }
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (markerList.size() == 0){
            addMarkers();
        }
        setUpMap();
    }
    @Override
    public void onPause() {
        super.onPause();
        clearMap();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recycleMarker();
        ((ViewGroup) rootView.getParent()).removeView(rootView);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        recycleMarker();
    }
    /**
     * 设置一些amap的属性
     */
    public void setUpMap() {
        AMap mGaodeMap =getMGaodeMapView().getMap();
        mGaodeMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mGaodeMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mGaodeMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        mGaodeMap.getUiSettings().setZoomControlsEnabled(false);//隐藏缩放控件
        AMap.OnCameraChangeListener ev = new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChangeFinish(CameraPosition arg0) {
                zoom = arg0.zoom;
            }
            @Override
            public void onCameraChange(CameraPosition arg0) {
                zoom = arg0.zoom;

            }
        };
        mGaodeMap.setOnCameraChangeListener(ev);
        setMGaodeMap(mGaodeMap);
        initDeviceInMap();
    }
    public void initDeviceInMap() {
        if (curDevice != null){
            LatLng latLng = new LatLng(curDevice.getOffsetLat(), curDevice.getOffsetLng());
            MarkerOptions mop = new MarkerOptions().position(latLng)
                    .title(curDevice.getDeviecName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mark0)).anchor(0.5f, 0.5f);
            if (marker != null)
                marker.remove();
            marker = getMGaodeMap().addMarker(mop);
            marker.setPosition(latLng);
            marker.setTitle(curDevice.getDeviecName());
            if (isFirst){
                isFirst = false;
                getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        }
    }
    public void addMarkers(){
        markerList.clear();
        for (int i = 0; i <= 10; i++) {
            int id = getResources().getIdentifier("mark"+i,"drawable", Configs.APP_PACKAGE_NAME);
            markerList.add(BitmapDescriptorFactory.fromResource(id));
        }
    }
    //recycle BitmapDescriptor
    public void recycleMarker(){
        for (int i = 0; i < markerList.size(); i++) {
            BitmapDescriptor b = markerList.get(i);
            b.recycle();
        }
        markerList.clear();
    }

    public void clearMap() {
        if (getMGaodeMap() != null) {
            getMGaodeMap().clear();
        }
        marker = null;
    }

    @Override
    public void onMapClick(LatLng point) {
        if (mAdd){
            if (currentIndex < -1)currentIndex = -1;

            currentIndex++;

            if (currentIndex > -1){
                if(shape==2){
                    fencePoints.clear();
                    currentIndex = 0;
                }
                fencePoints.add(currentIndex, point);
                refreshMap();
            }
        }
    }
    public void refreshMap() {

        if (circle != null || polyline != null) {
            getMGaodeMap().clear();
        }
        initDeviceInMap();
        if (currentIndex < 2 && currentIndex > -1) {
            addCircle();
        }
        if (currentIndex >= 2) {
            addPolygon();
            id_llSeekbar.setVisibility(View.GONE);
        }
    }
    public void doSave(){
        if (fencePoints != null && fencePoints.size() > 0) {
            // 圆形只保存一个点
            if (fencePoints.size() <= 2) {
                if (fencePoints.size() == 2)
                    fencePoints.remove(1);
                if (fencePoints.size() == 0 && curDevice != null){
                    double lat = curDevice.getOffsetLat();
                    double lng = curDevice.getOffsetLng();
                    LatLng latLng = new LatLng(lat,lng);
                    fencePoints.add(latLng);
                }
            }
            String strList = "";
            for (int i = 0; i < fencePoints.size(); i++) {
                LatLng latLng = fencePoints.get(i);
                strList += latLng.latitude + "," + latLng.longitude + ";";
            }
            R_Users user = Configs.get(Configs.Config.CurUser,R_Users.class);
            if (user != null){
                geography = new Device_Geography();
                geography.setGeography(strList);
                geography.set_id(UUID.randomUUID().toString());
                geography.setSerialnumber(curDevice != null ? curDevice.getSerialnumber() : user.getLoginName());
                geography.setCreateTime(new Date());
                geography.setName(nameString);
                geography.setType(alertType);
                geography.setShape(shape);
                if(shape==2){
                    geography.setRadius(radius);
                }
                getMLoadingDoialog().show();
                getMNetHelper().saveGeography(geography,mAdd);
            }else {
                showMessage(getResources().getString(R.string.text_no_user_msg));
            }
        }else {
            showMessage(getResources().getString(R.string.draw_electronicfence));
        }
    }
    public void showGIS(){
        if (Utils.CurGeography != null){
            String pointStr = Utils.CurGeography.getGeography();
            if (!TextUtils.isEmpty(pointStr)) {
                String[] points = pointStr.split(";");
                for (String point1 : points) {
                    String[] point = point1.split(",");
                    LatLng latLng = new LatLng(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
                    fencePoints.add(latLng);
                    currentIndex++;
                }
            }
        }
        int count = fencePoints.size();
        if (count > 0 && count < 3) {
            addCircle();
        }
        if (count >= 3) {
            addPolygon();
        }
        getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(fencePoints.get(0), zoom));
    }
    //圆形围栏
    public void addCircle() {

        if (currentIndex == -1 && curDevice != null){
            double lat = curDevice.getOffsetLat();
            double lng = curDevice.getOffsetLng();
            LatLng latLng = new LatLng(lat,lng);
            fencePoints.add(latLng);
            currentIndex = -2;
        }
        if (currentIndex == 1) {
            radius = (int) AMapUtils.calculateLineDistance(fencePoints.get(0),
                    fencePoints.get(1));
            if (radius < 500){
                radius = 500;
            }
            if(radius>1000){
                radius = 1000;
            }
        }
        if (fencePoints.size() <= 0)return;

        // 绘制一个圆形
        circle = getMGaodeMap().addCircle(new CircleOptions().center(fencePoints.get(0))
                .radius(radius).strokeColor(strokeColor).fillColor(fillColor)
                .strokeWidth(strokeWidth));
    }
    //多边形围栏
    public void addPolygon() {

        PolygonOptions options = new PolygonOptions();
        for (int i = 0; i <= currentIndex; i++) {
            options.add(fencePoints.get(i));
        }

        options.add(fencePoints.get(0));
        polyline = getMGaodeMap().addPolygon(options.strokeWidth(strokeWidth)
                .strokeColor(strokeColor).fillColor(fillColor));
    }
    public void doClear(){
        currentIndex = -1;
        fencePoints.clear();
        refreshMap();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event){
        String msg = event.getMessage();
        if (Constant.EVENT_CLEAR_E_ZONE_SHAPES.equals(msg)){
            if (fencePoints.size() > 0)
                doClear();
            radius = 500;
        }else if(Constant.EVENT_E_ZONE_TYPE.equals(msg)){
            shape = event.getFlg();
            clearMap();
            fencePoints.clear();
            initDeviceInMap();
            Log.e(getClass().getSimpleName(),"onEventMainThread "+shape);
            if(shape==1){
                mZoneAlert.setVisibility(View.VISIBLE);
                id_llSeekbar.setVisibility(View.GONE);
            }else {
                mZoneAlert.setVisibility(View.GONE);
                id_llSeekbar.setVisibility(View.VISIBLE);
                currentIndex = -1;
                addCircle();
            }
        }
    }

    public CountDownTimer mTime;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SaveOrUpdateGeography".equals(msg)){
            getMNetHelper().accessE_ZoneList();
            getMLoadingDoialog().dismiss();
            doClear();
            Utils.isSuccess = true;
            showMessage(getResources().getString(R.string.text_zone_add_success));
        }
    }

    private void initClick(){
        id_llZoneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindowDialog(rootView);
            }
        });
        id_tvSetArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = id_tvZoneName.getText().toString().trim();
                geography = Utils.CurGeography;
                if (geography == null){
                    if (nameString.equals(getResources().getString(R.string.text_e_zone_name))){
                        showMessage(getResources().getString(R.string.text_tips_input_zone_name));
                    }else {
                        if(fencePoints.size() <= 2 && shape==1){
                            //shape = 2;
                            showMessage(getResources().getString(R.string.text_points_too_less));
                        }else {
//                            shape = 1;
//                            radius = 0;
                            doSave();
                        }
                    }
                }
            }
        });
    }

    public HashMap<Integer,Boolean> mRadio=new HashMap<>();
    public void showPopupWindowDialog(final View view){
        if(mRadio.size()<=0){
            mRadio.put(0,true);
            mRadio.put(1,false);
            mRadio.put(0,false);
        }
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        View contentView=View.inflate(getActivity(),R.layout.pw_zone,null);
        TextView id_buttonCancel = (TextView) contentView.findViewById(R.id.pw_zone_cancel);
        TextView id_buttonSend = (TextView) contentView.findViewById(R.id.pw_zone_ok);
        PercentLinearLayout pl1=(PercentLinearLayout)contentView.findViewById(R.id.pw_zone_check_parent1);
        final ImageView imag1=(ImageView)contentView.findViewById(R.id.pw_zone_check1);
        final ImageView imag2=(ImageView)contentView.findViewById(R.id.pw_zone_check2);
        final ImageView imag3=(ImageView)contentView.findViewById(R.id.pw_zone_check3);
        PercentLinearLayout pl2=(PercentLinearLayout)contentView.findViewById(R.id.pw_zone_check_parent2);
        PercentLinearLayout pl3=(PercentLinearLayout)contentView.findViewById(R.id.pw_zone_check_parent3);
        final PercentRelativeLayout clear=(PercentRelativeLayout) contentView.findViewById(R.id.pw_zone_clear);
        pl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imag1.setBackgroundResource(R.drawable.radio_selected);
                imag2.setBackgroundResource(R.drawable.radio);
                imag3.setBackgroundResource(R.drawable.radio);
                mRadio.put(0,true);
                mRadio.put(1,false);
                mRadio.put(0,false);
                alertType = 0;
            }
        });
        pl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imag2.setBackgroundResource(R.drawable.radio_selected);
                imag1.setBackgroundResource(R.drawable.radio);
                imag3.setBackgroundResource(R.drawable.radio);
                mRadio.put(0,false);
                mRadio.put(1,true);
                mRadio.put(0,false);
                alertType = 1;
            }
        });

        pl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imag3.setBackgroundResource(R.drawable.radio_selected);
                imag1.setBackgroundResource(R.drawable.radio);
                imag2.setBackgroundResource(R.drawable.radio);
                mRadio.put(0,false);
                mRadio.put(1,false);
                mRadio.put(0,true);
                alertType = 2;
            }
        });
        final EditText id_edZoneName = (EditText) contentView.findViewById(R.id.pw_zone_ed);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_edZoneName.setText(null);
            }
        });
        id_edZoneName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>0){
                    clear.setVisibility(View.VISIBLE);
                }else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final PopupWindow popupWindow = new PopupWindow(contentView,
                (int)(width*0.88), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        id_buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        id_buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = id_edZoneName.getText().toString().trim();
                if (name.equals("")){
                    showMessage(getResources().getString(R.string.text_tips_input_zone_name));
                    return;
                }
                for (Device_Geography dg : Utils.fenceList){
                    String oriName = dg.getName().trim();
                    if (name.equals(oriName)){
                        showMessage(getResources().getString(R.string.text_tips_zone_name_exits));
                        return;
                    }
                }
                nameString = name;
                id_tvZoneName.setText(name);
                popupWindow.dismiss();

            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        //        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
