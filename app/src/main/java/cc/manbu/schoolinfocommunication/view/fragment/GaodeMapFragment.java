package cc.manbu.schoolinfocommunication.view.fragment;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.base.BaseMapFragment;
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.listener.HttpRespnse;
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener;
import cc.manbu.schoolinfocommunication.tools.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class GaodeMapFragment extends BaseMapFragment implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {
    public View rootView;
    public View infoWindow;
    @BindView(R2.id.view_map)
    public LinearLayout view_map;
    @BindView(R2.id.id_ivLoacte)
    public ImageView id_ivLoacte;
    @BindView(R2.id.id_topLine)
    public TextView id_topLine;

    public Marker marker;
    public ArrayList<BitmapDescriptor> markerList = new ArrayList<>();
    public GeocodeSearch geocoderSearch;
    public float zoom = 16;// 表示地图的缩放级别
    public String addressName;
    public LayoutInflater layoutInflater;
    public boolean isDraw = true;
    public boolean isLocating = false;
    public PopupWindow popupWindow;

    public GaodeMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_gaode_map, container);
            ButterKnife.bind(this, rootView);
        }
        if (infoWindow == null) {
            infoWindow = inflater.inflate(R.layout.layout_pop_deviceinfo, null);
        }
        layoutInflater = inflater;
        EventBus.getDefault().register(this);
        if (getMGaodeMapView() == null) {
            MapView mGaodeMapView = new MapView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            view_map.addView(mGaodeMapView, lp);
            setMGaodeMapView(mGaodeMapView);
            if (markerList.size() == 0) {
                addMarkers();
            }
        }
        onGaodeMapViewCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id_ivLoacte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLocating && !Utils.isFastClick()) {
                    isLocating = true;
                    x.task().run(new Runnable() {
                        @Override
                        public void run() {
                            getMNetHelper().serachLocate();
                            int[] delayedDurations = {5000, 10000, 15000};
                            int i = 0;
                            //设备单点定位后延时3次获取设备位置
                            while (i < delayedDurations.length) {
                                try {
                                    Thread.sleep(delayedDurations[i]);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.w("GaodeMapFragment", "第" + (i + 1) + "次获取设备位置");
                                i++;
                                refreshDeviceLocation();
                            }
                            isLocating = false;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (markerList.size() == 0) {
            addMarkers();
        }
        startTimer();
        initMapView();
    }

    boolean isTimerRunning;
    Thread timerThread;

    public void startTimer() {
        isTimerRunning = true;
        timerThread = new Thread() {
            @Override
            public void run() {
                while (isTimerRunning) {
                    refreshDeviceLocation();
                    try {
                        Thread.sleep(25000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        timerThread.start();
    }

    public void cancelTimer() {
        isTimerRunning = false;
        timerThread = null;
    }


    public void refreshDeviceLocation() {
        MobileDevicAndLocation devicAndLocation = getMNetHelper().
                getMobileDevicAndLocationBySync(Configs.getCurDeviceSerialnumber());
        if (devicAndLocation != null) {
            Configs.put(Configs.Config.CurDevice, devicAndLocation);
            LatLng latLng = null;
            latLng = new LatLng(devicAndLocation.getOffsetLat(),
                    devicAndLocation.getOffsetLng());
            MarkerOptions mop = new MarkerOptions().position(latLng)
                    .anchor(0.5f, 0.5f)
                    .title(devicAndLocation.getDeviecName())
                    .icons(markerList).draggable(true).period(6);
            if (marker != null) marker.remove();
            marker = getMGaodeMap().addMarker(mop);
            marker.setPosition(latLng);
            marker.setTitle(devicAndLocation.getDeviecName());
            //                                mGaodeMap.animateCamera(CameraUpdateFactory
            //                                        .newLatLngZoom(latLng, zoom));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(latLng,//新的中心点坐标
                            zoom, //新的缩放级别
                            30, //俯仰角0°~45°（垂直与地图时为0）
                            0  ////偏航角 0~360° (正北方为0)
                    ));
            getMGaodeMap().animateCamera(cameraUpdate, 1000L, null);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            startTimer();
        } else {
            cancelTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTimer();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    //recycle BitmapDescriptor
    public void recycleMarker() {
        for (int i = 0; i < markerList.size(); i++) {
            BitmapDescriptor b = markerList.get(i);
            b.recycle();
        }
        markerList.clear();
    }

    public void addMarkers() {
        markerList.clear();
        for (int i = 0; i <= 10; i++) {
            int id = getResources().getIdentifier("mark" + i, "drawable", Configs.APP_PACKAGE_NAME);
            markerList.add(BitmapDescriptorFactory.fromResource(id));
        }
    }

    /**
     * Init the Map
     */
    public void initMapView() {

        AMap mGaodeMap = getMGaodeMapView().getMap();
        mGaodeMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGaodeMap.getUiSettings().setZoomControlsEnabled(false);
        mGaodeMap.setMyLocationEnabled(false);// 是否可触发定位并显示定位层
        mGaodeMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mGaodeMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mGaodeMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        geocoderSearch = new GeocodeSearch(getActivity());//逆地理编码
        geocoderSearch.setOnGeocodeSearchListener(GeocodeSearchListener);
        mGaodeMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                if (marker != null) {
                    if (marker.isInfoWindowShown()) {
                        animWindow(true);
                        //marker.hideInfoWindow();
                    }
                }
                //hideMeum();
            }
        });
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
        LogUtil.i("initDeviceInMap()" + "生成地图");
        MobileDevicAndLocation devicAndLocation = Configs.get(Configs.Config.CurDevice,
                MobileDevicAndLocation.class);
        if (devicAndLocation != null) {
            try {
                LatLng latLng = null;
                latLng = new LatLng(devicAndLocation.getOffsetLat(),
                        devicAndLocation.getOffsetLng());
                //mGaodeMap.setPointToCenter(0,0);//设置地图以屏幕坐标(0,0)为中心进行旋转，默认是以屏幕中心旋转
                MarkerOptions mop = new MarkerOptions().position(latLng)
                        .anchor(0.5f, 0.5f)
                        .title(devicAndLocation.getDeviecName())
                        .icons(markerList).draggable(true).period(6);
                if (marker != null)
                    marker.remove();
                marker = getMGaodeMap().addMarker(mop);
                marker.setPosition(latLng);
                marker.setTitle(devicAndLocation.getDeviecName());

                //mGaodeMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(latLng,//新的中心点坐标
                                zoom, //新的缩放级别
                                30, //俯仰角0°~45°（垂直与地图时为0）
                                0  ////偏航角 0~360° (正北方为0)
                        ));
                getMGaodeMap().animateCamera(cameraUpdate, 2000L, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        marker.showInfoWindow();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } catch (Exception e) {
                LogUtil.e("initDeviceInMap()", e);
            }
        }
    }

    public void clearMap() {
        if (getMGaodeMap() != null) {
            getMGaodeMap().clear();
        }
        marker = null;
    }

    public GeocodeSearch.OnGeocodeSearchListener GeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {

        @Override
        public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

        }

        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {//逆地理编码回调
            try {
                addressName = getString(R.string.tips_no_data);
                if (rCode == 0) {
                    if (result != null
                            && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        addressName = result.getRegeocodeAddress()
                                .getFormatAddress() + getString(R.string.nearby);
                    }
                }
            } catch (Exception e) {
                LogUtil.e("onRegeocodeSearched()", e);
            }
        }
    };

    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null)
            infoWindow = layoutInflater.inflate(R.layout.layout_pop_deviceinfo, null);
        final LinearLayout id_llInfoWindow = (LinearLayout) infoWindow.findViewById(R.id.id_llInfoWindow);
        TextView id_tvConnWay = (TextView) infoWindow.findViewById(R.id.id_tvConnWay);
        TextView id_tvBaterry = (TextView) infoWindow.findViewById(R.id.id_tvBaterry);
        TextView id_tvLocatTime = (TextView) infoWindow.findViewById(R.id.id_tvLocatTime);
        TextView id_tvAddress = (TextView) infoWindow.findViewById(R.id.id_tvAddress);
        TextView id_tvStatus = (TextView) infoWindow.findViewById(R.id.id_tvStatus);
        final LatLng latLng = marker.getPosition();
        Point point = getMGaodeMap().getProjection().toScreenLocation(latLng);
        final int h = Configs.get(Configs.Config.ScreenHeight, Integer.TYPE);
        final int w = Configs.get(Configs.Config.ScreenWidth, Integer.TYPE);
        final float cx = point.x;
        final float cy = point.y;
        //LogUtil.e("cx:"+cx+"cy:"+cy+"sx:"+sx+"sy:"+sy+"w"+w+"h:"+h);//cx:270.0cy:379.0sx:0.0sy:0.0w540h:960
        MobileDevicAndLocation curDev = Configs.get(Configs.Config.CurDevice,
                MobileDevicAndLocation.class);
        if (curDev != null) {
            String address = curDev.getAddress();
            int locType = curDev.getLty();
            String locTime = curDev.getGpsTime();
            short power = curDev.getElectricity();
            boolean isOffLine = curDev.getDeviceOnLineState() == 1;
            String fontBlck = "<font color=\"#000000\">";
            String fontColor = isOffLine ? "<font color=\"#A9A9A9\">" : "<font color=\"#0FEB42\">";
            String end = "</font>";
            String type = "";
            String device_status = isOffLine ? getString(R.string.text_off_line) : getString(R.string.text_on_line);
            if (locType == 1) type = "GPRS";
            else if (locType == 2) type = "LBS";
            else if (locType == 3) type = "WIFI";
            id_tvConnWay.setText(Html.fromHtml(String.format(getString(R.string.text_locate_type), fontBlck, end, type)));
            id_tvBaterry.setText(Html.fromHtml(String.format(getString(R.string.text_battery), fontBlck, end, power + "%")));
            id_tvLocatTime.setText(Html.fromHtml(String.format(getString(R.string.text_loc_time), fontBlck, end, locTime)));
            id_tvAddress.setText(address);
            id_tvStatus.setText(Html.fromHtml(String.format(getString(R.string.text_dev_status), fontBlck, end, fontColor, device_status, end)));
        }
        id_llInfoWindow.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isDraw) {
                    isDraw = false;
                    ViewGroup.LayoutParams layoutParams = id_llInfoWindow.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = w - 80;
                    id_llInfoWindow.setLayoutParams(layoutParams);
                }
                if (id_llInfoWindow.getViewTreeObserver().isAlive()) {
                    id_llInfoWindow.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        animWindow(false);
        return false;
    }

    public void animWindow(final boolean isReversed) {
        if (infoWindow != null) {
            float from = 0;
            float to = 1;
            if (isReversed) {
                from = 1;
                to = 0;
            }
            AnimatorSet set = new AnimatorSet();
            float sx = infoWindow.getX();
            float sy = infoWindow.getY();
            float ex = sx + infoWindow.getWidth();
            float ey = sy + infoWindow.getHeight();
            set.playTogether(
                    ObjectAnimator.ofFloat(infoWindow, "scaleX", from, to),
                    ObjectAnimator.ofFloat(infoWindow, "scaleY", from, to),
                    ObjectAnimator.ofFloat(infoWindow, "alpha", from, to)
            );
            ViewHelper.setPivotX(infoWindow, ex / 2);
            ViewHelper.setPivotY(infoWindow, ey);
            set.setDuration(1000L).start();
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isReversed)
                        marker.hideInfoWindow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    public void showPopLayout(View v) {
        View popView = layoutInflater.inflate(R.layout.layout_pop_type, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.showAsDropDown(v, 0, -2);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewEvent event = new ViewEvent();
                event.setMessage(Constant.EVENT_HIDE_TRACK_POP);
                EventBus.getDefault().post(event);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event) {
        String msg = event.getMessage();
        if (Constant.EVENT_SHOW_TRACK_POP.equals(msg)) {
            showPopLayout(id_topLine);
        }
    }
}
