package cc.manbu.schoolinfocommunication.view.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.base.BaseMapFragment;
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.httputils.NetHelper;
import cc.manbu.schoolinfocommunication.tools.DateUtil;
import cc.manbu.schoolinfocommunication.tools.GaodeTraceUtil;
import cc.manbu.schoolinfocommunication.tools.ScreenUtils;
import cc.manbu.schoolinfocommunication.view.customer.ProgressTextView;

/**
 * 轨迹回放
 *
 * @author gongyong2014
 *
 */
public class HistoryRouteFragment extends BaseMapFragment implements OnMarkerClickListener, InfoWindowAdapter,
        AMap.OnCameraChangeListener,CalendarDatePickerDialogFragment.OnDateSetListener,RadialTimePickerDialogFragment.OnTimeSetListener {
    private LinearLayout view_map;
    private View infoWindow;
    private PowerManager pm;
    private WakeLock mWakeLock;
    private PopupWindow window;
    private LayoutInflater layoutInflater;
    private TextView id_tvStartTime, id_tvEndTime;
    private TextView id_tvStartDate,id_tvEndDate;
    private ImageView id_ivEditStart,id_ivEditEnd;
    private TextView id_tvSlectOk;
    private TextView id_topLine;
    protected String startDateStr;
    protected String endDateStr;
    private ProgressBar pb_play_progress;
    private LinearLayout ll_progress_container;
    private TextView pop_play_finished;
    private View rootView;
    private MobileDevicAndLocation curDevice;
    private String startDate;
    private String startTime;
    private String endTime;
    private String endDate;
    private boolean isStart = true;
    private boolean isDraw = true;

    protected List<LatLng> totlaLatlngList = new ArrayList<LatLng>();
    LinearLayout view_Bootom_Tool;
    SeekBar SeekBat_PlaySpeed;
    Button Btn_Play;
    Button btn_revPlay;
    Button btn_Fin;
    private TextView progress_text;
    private ImageButton button_cancle;
    // 表示地图的缩放级别
    float zoom = 14;
    //	int[] Scale = {1000000,500000,200000,100000,50000,30000,20000,10000,5000,2000,1000,500,200,100,50,25,10,5};//高德地图比例尺


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_route, null);
        if (infoWindow == null){
            infoWindow = inflater.inflate(R.layout.layout_pop_deviceinfo, null);
        }
        EventBus.getDefault().register(this);
        curDevice = Configs.get(Configs.Config.CurDevice,MobileDevicAndLocation.class);
        layoutInflater = inflater;
        view_map = (LinearLayout) rootView.findViewById(R.id.view_map);
        mSnackBar = (ProgressTextView) rootView.findViewById(R.id.mSnackBar);
        ((GradientDrawable)(mSnackBar.getBackground())).setColor(0xCF241F1F);
        mSnackBar.setVisibility(View.GONE);
        pb_play_progress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        ll_progress_container = (LinearLayout) rootView.findViewById(R.id.ll_progress_container);
        pop_play_finished = (TextView) rootView.findViewById(R.id.pop_play_finished);
        btn_showLine = (Button) rootView.findViewById(R.id.btn_showLine);
        btn_manual_zoom_map = (Button) rootView.findViewById(R.id.btn_manual_zoom_map);
        btn_rectify_track = (Button) rootView.findViewById(R.id.btn_rectify_track);
        id_topLine = (TextView) rootView.findViewById(R.id.id_topLine);
        BabyAnimationDrawables[0] = new AnimationDrawable();
        BabyAnimationDrawables[0].addFrame(getResources().getDrawable(R.drawable.baby_run3),80);
        BabyAnimationDrawables[0].addFrame(getResources().getDrawable(R.drawable.baby_run4),80);
        BabyAnimationDrawables[0].setOneShot(false);
        BabyAnimationDrawables[1] = new AnimationDrawable();
        BabyAnimationDrawables[1].addFrame(getResources().getDrawable(R.drawable.baby_run1),80);
        BabyAnimationDrawables[1].addFrame(getResources().getDrawable(R.drawable.baby_run2),80);
        BabyAnimationDrawables[1].setOneShot(false);
        currentBabyAnimationDrawable = BabyAnimationDrawables[0];
        baby_view = rootView.findViewById(R.id.baby_view);
        baby_view.setBackgroundDrawable(currentBabyAnimationDrawable);
        baby_view.setVisibility(View.GONE);
        isShowingLine = Configs.get(Configs.Config.IsHistoryRoutePlayingShowLine,Boolean.TYPE);
        isShowingLine = isShowingLine==null?true:isShowingLine;
        isManualZoomMap = Configs.get(Configs.Config.IsHistoryRouteManualZoomMap,Boolean.TYPE);
        isManualZoomMap = isManualZoomMap==null?false:isManualZoomMap;
        isRectifyTrack = Configs.get(Configs.Config.IsHistoryRouteRectifyTrack,Boolean.TYPE);
        isRectifyTrack = isRectifyTrack==null?true:isRectifyTrack;
        onChanageHistoryRoutePlayMode();
        onChanageHistoryRouteZoomMapMode();
        onChanageHistoryRouteRectifyMode();
        btn_showLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingLine = !isShowingLine;
                onChanageHistoryRoutePlayMode();
                Configs.put(Configs.Config.IsHistoryRoutePlayingShowLine,isShowingLine);
            }
        });
        btn_manual_zoom_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isManualZoomMap = !isManualZoomMap;
                onChanageHistoryRouteZoomMapMode();
                Configs.put(Configs.Config.IsHistoryRouteManualZoomMap,isManualZoomMap);
                mSnackBar.setText(isManualZoomMap?R.string.tips_manual_zoom_map_mode:R.string.tips_auto_zoom_map_mode);
                mSnackBar.startWordAinmation(false,800,800,mSnackBar.getText().toString(),0,null,true);
                if(view_Bootom_Tool.getVisibility() == View.VISIBLE){
                    btn_revPlay.performClick();
                }
            }
        });
        btn_rectify_track.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isRectifyTrack = !isRectifyTrack;
                onChanageHistoryRouteRectifyMode();
                Configs.put(Configs.Config.IsHistoryRouteRectifyTrack,isRectifyTrack);
                List<LatLng> data = isRectifyTrack?rectifyTrackData:orginTrackData;
                mSnackBar.setText(isRectifyTrack?R.string.tips_play_rectified_trace:R.string.tips_play_origin_trace);
                mSnackBar.startWordAinmation(false,800,800,mSnackBar.getText().toString(),0,null,true);
                if(data.size()>0){
                    isRuning = false;
                    totlaLatlngList.clear();
                    totlaLatlngList.addAll(data);
                    if(mMoveMarker!=null){
                        mMoveMarker.hideInfoWindow();
                    }
                    btn_revPlay.performClick();
                }
            }
        });
        mScaleGestureDetectorr = new ScaleGestureDetector(getActivity(),new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.w("onScale()","ooooooooooooooooooooo");
                if(isManualZoomMap!=null && !isManualZoomMap && isRuning && totlaLatlngList.size()>0){
                    Btn_Play.performClick();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }
        });
        MapView mGaodeMapView = new MapView(getActivity());
        view_map.addView(mGaodeMapView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        setMGaodeMapView(mGaodeMapView);
        onGaodeMapViewCreate(savedInstanceState);
        pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        SeekBat_PlaySpeed = (SeekBar) rootView.findViewById(R.id.SeekBat_PlaySpeed);
        view_Bootom_Tool = (LinearLayout) rootView.findViewById(R.id.view_Bootom_Tool);
        view_Bootom_Tool.setVisibility(View.GONE);
        Btn_Play = (Button) rootView.findViewById(R.id.Btn_Play);
        btn_revPlay = (Button) rootView.findViewById(R.id.btn_revPlay);
        btn_Fin = (Button) rootView.findViewById(R.id.btn_Fin);
        progress_text = (TextView) rootView.findViewById(R.id.progress_text);
        button_cancle = (ImageButton) rootView.findViewById(R.id.button_cancle);
        button_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isRuning = false;
                isContinue = false;
                currentPolyline = null;
                currentPolylineOptions = null;
                lastCameraCenter = null;
                babyLatlng = null;
                mMoveMarker = null;
                MoveRotationArray = null;
                locationTimeList.clear();
                orginTrackData.clear();
                rectifyTrackData.clear();
                totlaLatlngList.clear();
                isQueryRectifyTrackFinished = false;
                GaodeTraceUtil.reset();
                view_Bootom_Tool.setVisibility(View.GONE);
                ll_progress_container.setVisibility(View.GONE);
                ClearMap();
                initDeviceInMap();
                currentBabyAnimationDrawable.stop();
                baby_view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        baby_view.setVisibility(View.GONE);
                    }
                },350L);
            }
        });
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // int dpi = metric.densityDpi;//屏幕像素密度，一英寸
        //value_px = (float) (dpi/2.54);
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event) {
        String msg = event.getMessage();
        if (Constant.EVENT_POP_TIME_TRACE.equals(msg)){
            View contentView = layoutInflater.inflate(R.layout.layout_pop_view, null);
            window = new PopupWindow(rootView);
            window.setContentView(contentView);
            window.setAnimationStyle(R.style.popwin_anim_style);
            window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setFocusable(true); // 设置PopupWindow可获得焦点
            window.setTouchable(true); // 设置PopupWindow可触摸
            window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
            window.setBackgroundDrawable(new BitmapDrawable());// 设置后点击popupWindow区域外可自动隐藏
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewEvent event = new ViewEvent();
                    event.setMessage(Constant.EVENT_HIDE_POP_TIME_TRACE);
                    EventBus.getDefault().post(event);
                }
            });
            id_tvStartTime = (TextView) contentView.findViewById(R.id.id_tvStartTime);
            id_tvEndTime = (TextView) contentView.findViewById(R.id.id_tvEndTime);
            id_tvStartDate = (TextView) contentView.findViewById(R.id.id_tvStartDate);
            id_tvEndDate = (TextView) contentView.findViewById(R.id.id_tvEndDate);
            id_ivEditEnd = (ImageView) contentView.findViewById(R.id.id_ivEditEnd);
            id_ivEditStart = (ImageView) contentView.findViewById(R.id.id_ivEditStart);

            id_ivEditStart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isStart = false;
                    setDateAndTime();
                }
            });
            id_ivEditEnd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isStart = true;
                    setDateAndTime();
                }
            });
            try {
                String lastActivityTime = curDevice.getGpsTime();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                DateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.CHINA);
                Date endDate = df.parse(lastActivityTime);
                Date startDate = new Date(endDate.getTime() - 5 * 60 * 60 * 1000);
                endDateStr = lastActivityTime;
                startDateStr = df.format(startDate);
                id_tvEndDate.setText(dfDate.format(endDate));
                id_tvEndTime.setText(dfTime.format(endDate));
                id_tvStartDate.setText(dfDate.format(startDate));
                id_tvStartTime.setText(dfTime.format(startDate));
            } catch (Exception e) {
                Log.e("initDefaultDate()", e.getMessage());
            }
            id_tvSlectOk = (TextView) contentView.findViewById(R.id.id_tvSlectOk);
            id_tvSlectOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    getMLoadingDoialog().show();
                    ClearMap();
                    isRuning = true;
                    isContinue = true;
                    orginTrackData.clear();
                    rectifyTrackData.clear();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                NetHelper bl = NetHelper.getInstance();
                                String deviceSerialNumber = Configs.getCurDeviceSerialnumber();
                                if (deviceSerialNumber == null || "".equals(deviceSerialNumber)) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            getMLoadingDoialog().dismiss();
                                            String msg =getResources().getString(R.string.tips_no_device);
                                            showMessage(msg);
                                        }
                                    });
                                    return;
                                }
                                // 如果设备存在 则开始加载轨迹数据
                                String traceDataStr = bl.getDeviceTraceDataStr(deviceSerialNumber, startDateStr, endDateStr);
                                if (traceDataStr == null || traceDataStr.equals("")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            getMLoadingDoialog().dismiss();
                                            String msg = getResources().getString(R.string.no_data_current_time);
                                            showMessage(msg);
                                        }
                                    });
                                    return;
                                }

                                // 清除以前的数据 开始装载新的数据
                                totlaLatlngList.clear();
                                GaodeTraceUtil.reset();
                                String[] locations = traceDataStr.split(";");
                                for (int i = 0; i < locations.length; i++) {
                                    String[] loc_data = locations[i].split(",");
                                    double lat1 = Double.parseDouble(loc_data[2]);
                                    double lng1 = Double.parseDouble(loc_data[3]);
                                    Date date = DateUtil.parse("yyyy-MM-dd HH:mm:ss",loc_data[4]);
                                    GaodeTraceUtil.addLatlng(lat1,lng1,date.getTime());
                                    locationTimeList.add(loc_data[4]);
                                    LatLng latlng1 = new LatLng(lat1, lng1);
                                    orginTrackData.add(latlng1);
                                }
                                // 显示底部工具条
                                final Runnable task = new Runnable() {
                                    @Override
                                    public void run() {
                                        currentPolyline = null;
                                        currentPolylineOptions = null;
                                        lastCameraCenter = null;
                                        babyLatlng = null;
                                        mMoveMarker = null;
                                        view_Bootom_Tool.setVisibility(View.VISIBLE);
                                        Btn_Play.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View arg0) {
                                                if (isRuning) {
                                                    isRuning = false;
                                                    Btn_Play.setBackgroundResource(R.drawable.start_search);
                                                } else {
                                                    isRuning = true;
                                                    moveFromAt(curBabyPosition+1+steps);
                                                    Btn_Play.setBackgroundResource(R.drawable.stop);
                                                }

                                            }
                                        });
                                        btn_revPlay.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View arg0) {
                                                isRuning = false;
                                                mHandler.removeCallbacksAndMessages(null);
                                                getMGaodeMap().stopAnimation();
                                                currentPolyline = null;
                                                currentPolylineOptions = null;
                                                babyLatlng = null;
                                                mMoveMarker = null;
                                                isContinue = true;
                                                ClearMap();
                                                if(curDevice!=null){
                                                    MarkerOptions mop = new MarkerOptions().position(new LatLng(curDevice.getOffsetLat(),curDevice.getOffsetLng())).anchor(0.5f, 0.5f).title(curDevice.getDeviecName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mark0));
                                                    mDeviceMarker = getMGaodeMap().addMarker(mop);
                                                }
                                                Btn_Play.setBackgroundResource(R.drawable.stop);
                                                btn_revPlay.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        isRuning = true;
                                                        moveFromAt(0);
                                                    }
                                                },150L);
                                            }
                                        });
                                        btn_Fin.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View arg0) {
                                                view_Bootom_Tool.setVisibility(View.GONE);
                                                ll_progress_container.setVisibility(View.GONE);
                                                isRuning = false;
                                                isContinue = false;
                                                babyLatlng = null;
                                                mMoveMarker = null;
                                                currentPolyline = null;
                                                currentPolylineOptions = null;
                                                lastCameraCenter = null;
                                                locationTimeList.clear();
                                                MoveRotationArray = null;
                                                isQueryRectifyTrackFinished = false;
                                                GaodeTraceUtil.reset();
                                                orginTrackData.clear();
                                                rectifyTrackData.clear();
                                                currentBabyAnimationDrawable.stop();
                                                ClearMap();
                                                initDeviceInMap();
                                                baby_view.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        baby_view.setVisibility(View.GONE);
                                                    }
                                                },350L);
                                            }
                                        });
                                    }
                                };
                                rectifyTrackData.clear();
                                //开始轨迹纠偏
                                GaodeTraceUtil.queryProcessedTrace(LBSTraceClient.TYPE_AMAP, new GaodeTraceUtil.MyTraceListener() {
                                    @Override
                                    public void onTraceHandled(boolean isFinished, List<TraceLocation> data) {
                                        for(TraceLocation tl:data){
                                            LatLng p = new LatLng(tl.getLatitude(),tl.getLongitude());
                                            rectifyTrackData.add(p);
                                        }
                                        if(isFinished){
                                            isQueryRectifyTrackFinished = true;
                                            if(isRectifyTrack){
                                                totlaLatlngList.clear();
                                                totlaLatlngList.addAll(rectifyTrackData);
                                            }
                                            Log.w("GaodeTraceUtil","所有轨迹纠偏完成,纠偏后轨迹点为"+rectifyTrackData.size()+"个");
                                        }
                                    }

                                    @Override
                                    public void onTraceFirstQueryFinished() {
                                        if(isRectifyTrack){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    task.run();
                                                    getMLoadingDoialog().dismiss();
                                                    ClearMap();
                                                    moveFromAt(0);
                                                }
                                            });
                                        }
                                    }
                                });
                                if(!isRectifyTrack){
                                    totlaLatlngList.addAll(orginTrackData);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            task.run();
                                            getMLoadingDoialog().dismiss();
                                            ClearMap();
                                            moveFromAt(0);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.e("StartTrace()", e.getMessage());
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        getMLoadingDoialog().dismiss();
                                    }
                                });
                            } finally {
                                getMLoadingDoialog().dismiss();
                            }

                        }
                    }).start();
                }
            });
            //window.showAtLocation(rootView, Gravity.RIGHT | Gravity.TOP, ScreenUtils.dip2px(context, 20), ScreenUtils.dip2px(context, 66));
            window.showAsDropDown(id_topLine,0,-2);
        }
    }

    private Marker mMoveMarker,mDeviceMarker;
    private Boolean isShowingLine;
    private Button btn_showLine,btn_manual_zoom_map,btn_rectify_track;
    private int mMapViewPointY,mMapViewPointX;
    private View baby_view;
    private AnimationDrawable[] BabyAnimationDrawables = new AnimationDrawable[2];
    private AnimationDrawable currentBabyAnimationDrawable;
    boolean isRuning = false;
    boolean isContinue = false;
    //是否手动缩放地图
    Boolean isManualZoomMap;
    //是否使用轨迹纠偏
    Boolean isRectifyTrack;
    private boolean isQueryRectifyTrackFinished;

    private int curBabyPosition;
    private PolylineOptions currentPolylineOptions;
    private Polyline currentPolyline;
    private List<String> locationTimeList = new ArrayList<>();
    private LatLng babyLatlng;
    private List<LatLng> orginTrackData = new ArrayList<>();
    private List<LatLng> rectifyTrackData = new ArrayList<>();
    private static final int MinMovementDurration = 800;
    private static final int MaxMovementDurration = 2400;
    private ScaleGestureDetector mScaleGestureDetectorr;
    private ProgressTextView mSnackBar;

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mScaleGestureDetectorr.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }*/

    private void onChanageHistoryRoutePlayMode(){
        btn_showLine.setBackgroundResource(isShowingLine?R.drawable.history_line:R.drawable.history_point);
    }
    private void onChanageHistoryRouteZoomMapMode(){
        btn_manual_zoom_map.setBackgroundResource(isManualZoomMap?R.drawable.history_trace_manual_zoom:R.drawable.history_trace_auto_zoom);
    }
    private void onChanageHistoryRouteRectifyMode(){
        btn_rectify_track.setBackgroundResource(isRectifyTrack?R.drawable.history_trace_rectify:R.drawable.history_trace_no_rectify);
    }

    private void moveBaby(LatLng target){
        if(mMapViewPointY==0){
            int[] p = new int[2];
            getMGaodeMapView().getLocationOnScreen(p);
            mMapViewPointY = p[0];
        }
        if(baby_view.getVisibility() == View.GONE){
            baby_view.setVisibility(View.VISIBLE);
        }
        Point p = getMGaodeMap().getProjection().toScreenLocation(target);
        babyLatlng = target;
        Log.w(this.getClass().getSimpleName(),String.format("moveBaby():%s,%s---->%s,%s",
                target.latitude,target.longitude,p.x,p.y));
        baby_view.setX(p.x-33);
        baby_view.setY(p.y-mMapViewPointY+baby_view.getHeight()+ ScreenUtils.dip2px(getActivity(),48));
        baby_view.invalidate();
    }

    /**
     * 改变小人的方向
     * @param direction 0:向左 1:向右
     */
    private void chanageBabyBearing(int direction){
        if(currentBabyAnimationDrawable != BabyAnimationDrawables[direction]){
            currentBabyAnimationDrawable.stop();
            currentBabyAnimationDrawable = BabyAnimationDrawables[direction];
            baby_view.setBackgroundDrawable(currentBabyAnimationDrawable);
            currentBabyAnimationDrawable.start();
        }else{
            if(!currentBabyAnimationDrawable.isRunning()){
                currentBabyAnimationDrawable.start();
            }
        }
    }

    /**
     * 判断当前zoom下该坐标位置是否在地图的可视范围之内
     * @return
     */
    private boolean isLatlngVisibleRegionOnMap(LatLng latLng, int padding){
        if(mMapViewPointY==0){
            int[] p = new int[2];
            getMGaodeMapView().getLocationOnScreen(p);
            mMapViewPointX = p[0];
            mMapViewPointY = p[1];
        }
        Point p = getMGaodeMap().getProjection().toScreenLocation(latLng);
        return p.x >= mMapViewPointX + padding && p.x<= mMapViewPointX + getMGaodeMapView().getWidth() - padding
                && p.y >= mMapViewPointY + padding && p.y <= mMapViewPointY + getMGaodeMapView().getHeight() - padding;
    }
    private LatLng getEndPoint(LatLng latLng,int index){
        int i = index + 1;
        int steps = 0;
        LatLng endPoint = totlaLatlngList.get(i);
        while (latLng.equals(endPoint)){
            i++;    steps++;
            if (i < totlaLatlngList.size()){
                endPoint = totlaLatlngList.get(i);
            }else {
                totlaLatlngList.get(index + 1);
                break;
            }
        }
        this.steps = steps;
        return endPoint;
    }
    private LatLng lastCameraCenter = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private float[] MoveRotationArray;
    private int steps;
    private void moveFromAt(int index){
        if(isRectifyTrack && !isQueryRectifyTrackFinished){
            totlaLatlngList.clear();
            totlaLatlngList.addAll(rectifyTrackData);
        }
        //Log.w("moveFromAt()","isRunning:"+isRuning+",totalSize:"+totlaLatlngList.size()+",正在绘制第"+index+"个轨迹点");
        if(isRuning){
            if(index<totlaLatlngList.size()-1){
                final LatLng startPoint = totlaLatlngList.get(index);
                curBabyPosition = index;
                final LatLng endPoint = getEndPoint(startPoint,index);//totlaLatlngList.get(index+1);

                final boolean isNeedChangeZoomAndMoveCamera = curBabyPosition>0  && !isLatlngVisibleRegionOnMap(startPoint,100);
                if(isNeedChangeZoomAndMoveCamera){
                    lastCameraCenter = startPoint;
                }
                Projection mProjection = getMGaodeMap().getProjection();
                final Point p1 = mProjection.toScreenLocation(startPoint);
                final Point p2 = mProjection.toScreenLocation(endPoint);
                if(index==0){
                    lastCameraCenter = startPoint;
                    mMoveMarker = getMGaodeMap().addMarker(new MarkerOptions()
                            .position(startPoint)
                            .alpha(0.7f)
//                            .anchor(0.5f,0.5f)
                            .title(isRectifyTrack?null:locationTimeList.get(curBabyPosition+1))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                    currentPolylineOptions = new PolylineOptions().width(ScreenUtils.dip2px(getActivity(),3))
                            .geodesic(true)
                            //							.color(0xFFE400FF)
                            .setCustomTexture(BitmapDescriptorFactory.fromAsset("tracelinetexture1.png"))
                            .visible(isShowingLine)
                            .width(40f)
                            .add(startPoint);
                    mMoveMarker.showInfoWindow();
                    view_Bootom_Tool.setVisibility(View.VISIBLE);
                    getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(lastCameraCenter,zoom),50,null);
                }
                int d = (curBabyPosition+1) * 100 / (totlaLatlngList.size()-1);
                progress_text.setText(getActivity().getResources().getString(R.string.has_completed)+ d + "%");
                pb_play_progress.setProgress(d);
                ll_progress_container.setVisibility(View.VISIBLE);
                chanageBabyBearing(p2.x>p1.x?1:0);
                int animateDuration = (100-SeekBat_PlaySpeed.getProgress())*400/100+200;
                if(isManualZoomMap){
                    //可以手动缩放地图，没有小人跑动动画效果
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            if(mMoveMarker!=null && curBabyPosition!=0){
                                mMoveMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_arrow));
                            }
                            if(currentPolylineOptions!=null && isManualZoomMap){
                                currentPolylineOptions.add(endPoint);
                                if(currentPolyline == null){
                                    currentPolyline = getMGaodeMap().addPolyline(currentPolylineOptions);
                                }else{
                                    currentPolyline.setVisible(isShowingLine);
                                    currentPolyline.setPoints(currentPolylineOptions.getPoints());
                                }
                                if(isNeedChangeZoomAndMoveCamera){
                                    getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(lastCameraCenter,zoom),50,null);
                                }
                                boolean isLastPoint = curBabyPosition+1==totlaLatlngList.size()-1;
                                mMoveMarker = getMGaodeMap().addMarker(new MarkerOptions()
                                        .position(endPoint)
                                        .alpha(0.7f)
//                                        .anchor(0.5f,0.5f)
                                        .title(isRectifyTrack?null:locationTimeList.get(curBabyPosition+1))
                                        .icon(isLastPoint?BitmapDescriptorFactory.fromResource(R.drawable.marker_end):BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                mMoveMarker.showInfoWindow();
                                moveBaby(endPoint);
                                //							mMoveMarker.setRotateAngle(MoveRotationArray[index+1]);
                                //							Log.w("moveFromAt()","第"+(index+1)+"轨迹点顺时针旋转了"+MoveRotationArray[index+1]+"°");
                                if(isLastPoint){
                                    chanageBabyBearing(p2.x>p1.x?1:0);
                                }
                                if(isManualZoomMap){
                                    moveFromAt(curBabyPosition+1+steps);
                                }
                            }
                        }
                    };
                    mHandler.postDelayed(task,animateDuration);
                }else{
                    if(mMoveMarker!=null && curBabyPosition!=0){
                        //					mMoveMarker.setAlpha(1f);
                        //					mMoveMarker.setFlat(true);
                        mMoveMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_arrow));
                    }
                    //不能手动缩放地图，有小人跑动动画效果
                    getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().
                            include(startPoint).include(endPoint).build(), 200),animateDuration,new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            zoom = getMGaodeMap().getCameraPosition().zoom;
                            if(currentPolylineOptions!=null){
                                currentPolylineOptions.add(endPoint);
                                if(currentPolyline == null){
                                    currentPolyline = getMGaodeMap().addPolyline(currentPolylineOptions);
                                }else{
                                    currentPolyline.setVisible(isShowingLine);
                                    currentPolyline.setPoints(currentPolylineOptions.getPoints());
                                }
                                boolean isLastPoint = curBabyPosition+1==totlaLatlngList.size()-1;
                                mMoveMarker = getMGaodeMap().addMarker(new MarkerOptions()
                                        .position(endPoint)
                                        .alpha(0.7f)
                                        .title(isRectifyTrack?null:locationTimeList.get(curBabyPosition+1))
                                        .icon(isLastPoint?BitmapDescriptorFactory.fromResource(R.drawable.marker_end):BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                mMoveMarker.showInfoWindow();
                            }

                            Projection mProjection = getMGaodeMap().getProjection();
                            final Point p1 = mProjection.toScreenLocation(startPoint);
                            final Point p2 = mProjection.toScreenLocation(endPoint);
                            final boolean isRight = p2.x>p1.x;
                            chanageBabyBearing(isRight?1:0);
                            //SeekBat_PlaySpeed
                            //两点距离
                            float result[] = new float[3];
                            Location.distanceBetween(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude, result);
                            float distance = result[0];
                            int duration = 0;
                            double k = 0;
                            int d = (int) Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
                            if(distance>111000){
                                duration = MinMovementDurration/2;
                            }else{
                                if(d==0){
                                    d = 4;
                                }
                                DisplayMetrics metric =new DisplayMetrics();
                                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
                                k = d*1d/(Math.min(metric.widthPixels,metric.heightPixels)/2);
                                duration = d<5?-1:d/(k>1?10:2);
                            }
                            duration = (int)((100-SeekBat_PlaySpeed.getProgress())*((1/k+1)*duration+MinMovementDurration)/100+240);
                            duration = Math.min(duration,MaxMovementDurration);
                            duration = Math.max(duration,100);
                            //							Log.w(TAG,"duration="+duration);
                            //							Log.w(TAG,"speed="+(d*1.0d/duration));
                            ValueAnimator va = ValueAnimator.ofFloat(0,1).setDuration(duration);
                            final int sp = ScreenUtils.dip2px(getActivity(),48);
                            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    float v = (float) valueAnimator.getAnimatedValue();
                                    float x = p1.x + (p2.x-p1.x)*v;
                                    float y = p1.y + (p2.y-p1.y)*v;
                                    baby_view.setX(x-33);
                                    baby_view.setY(y-mMapViewPointY+baby_view.getHeight()+sp);
                                    if(!isRuning){
                                        valueAnimator.cancel();
                                        //valueAnimator.end();
                                    }
                                    baby_view.invalidate();
                                }
                            });
                            va.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    currentBabyAnimationDrawable.start();
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    moveBaby(endPoint);
                                    currentBabyAnimationDrawable.stop();
                                    //									int d = (curBabyPosition+1) * 100 / (totlaLatlngList.size()-1);
                                    //									progress_text.setText(context.getResources().getString(R.string.has_completed)+ d + "%");
                                    //									pb_play_progress.setProgress(d);
                                    //									ll_progress_container.setVisibility(View.VISIBLE);
                                    if(isRuning && !isManualZoomMap){
                                        moveFromAt(curBabyPosition+1+steps);
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                    onAnimationEnd(animator);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            va.start();
                        }

                        @Override
                        public void onCancel() {
                            Log.w("moveFromAt()","Map cancel animate camera");
                            Btn_Play.performClick();
                        }
                    });
                }
            }else{
                if(totlaLatlngList.size()==1){
                    mMoveMarker = getMGaodeMap().addMarker(new MarkerOptions()
                            .position(totlaLatlngList.get(0)).title(locationTimeList.get(0))
                            .alpha(0.7f)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                    mMoveMarker.showInfoWindow();
                    getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(totlaLatlngList.get(0), zoom), 50, new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            moveBaby(totlaLatlngList.get(0));
                        }

                        @Override
                        public void onCancel() {
                            onFinish();
                        }
                    });
                }
                btn_Fin.performClick();
                final int height = pop_play_finished.getHeight();
                TranslateAnimation ta1 = new TranslateAnimation(0, 0, 0,
                        height);
                ta1.setDuration(500);
                ta1.setFillAfter(true);
                ta1.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                TranslateAnimation ta2 = new TranslateAnimation(
                                        0f, 0f, height, 0);
                                ta2.setDuration(500);
                                pop_play_finished.startAnimation(ta2);
                                currentBabyAnimationDrawable.stop();
                                baby_view.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                });
                pop_play_finished.startAnimation(ta1);
            }
        }
    }

    private boolean beforePausePlayState;
    @Override
    public void onPause() {
        mWakeLock.release();
        if(isContinue){
            beforePausePlayState = isRuning;
            if(isRuning){
                Btn_Play.performClick();
            }
        }else{
            ClearMap();
        }
        super.onPause();

    }

    @Override
    public void onResume() {
        mWakeLock.acquire();
        super.onResume();
        initMapView();
        if(isContinue && beforePausePlayState){
            Btn_Play.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Btn_Play.performClick();
                }
            },150);
        }
        beforePausePlayState = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Init the Map
     */
    private void initMapView() {
        AMap mGaodeMap = getMGaodeMapView().getMap();
        mGaodeMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mGaodeMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        mGaodeMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                if (mDeviceMarker != null) {
                    if (mDeviceMarker.isInfoWindowShown()) {
                        mDeviceMarker.hideInfoWindow();
                    }
                }
            }
        });
        mGaodeMap.setOnCameraChangeListener(this);
        setMGaodeMap(mGaodeMap);
        initDeviceInMap();

    }

    private void initDeviceInMap() {
        Log.i("initDeviceInMap()", "生成地图");
        if (curDevice != null) {

            try {

                LatLng latLng = new LatLng(curDevice.getOffsetLat(), curDevice.getOffsetLng());
                if(mDeviceMarker==null){
                    MarkerOptions mop = new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.mark0));
                    mDeviceMarker = getMGaodeMap().addMarker(mop);
                }else{
                    mDeviceMarker.setPosition(latLng);
                }
                mDeviceMarker.setTitle(curDevice.getDeviecName());
                getMGaodeMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            } catch (Exception e) {
                Log.e("initDeviceInMap()", e.getMessage());
            }

        }

    }

    void ClearMap() {
        if(getMGaodeMap()!=null){
            getMGaodeMap().clear();
        }
        mDeviceMarker = null;
        Log.w("ClearMap()", "**************Clear Map!*****************");
    }


    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLowMemory() {
        Log.w("onLowMemory()", "释放资源");
        super.onLowMemory();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if(!marker.equals(mDeviceMarker)){
            return null;
        }
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
        final int h = Configs.get(Configs.Config.ScreenHeight,Integer.TYPE);
        final int w = Configs.get(Configs.Config.ScreenWidth,Integer.TYPE);
        final float cx = point.x;
        final float cy = point.y;
        //LogUtil.e("cx:"+cx+"cy:"+cy+"sx:"+sx+"sy:"+sy+"w"+w+"h:"+h);//cx:270.0cy:379.0sx:0.0sy:0.0w540h:960
        MobileDevicAndLocation curDev = Configs.get(Configs.Config.CurDevice,
                MobileDevicAndLocation.class);
        if (curDev != null){
            String address = curDev.getAddress();
            int locType = curDev.getLty();
            String locTime = curDev.getGpsTime();
            short power = curDev.getElectricity();
            boolean isOffLine = curDev.getDeviceOnLineState() == 1;
            String fontBlck = "<font color=\"#000000\">";
            String fontColor = isOffLine? "<font color=\"#A9A9A9\">" : "<font color=\"#0FEB42\">";
            String end = "</font>";
            String type = "";
            String device_status = isOffLine ? getString(R.string.text_off_line) : getString(R.string.text_on_line);
            if (locType == 1)type = "GPRS";
            else if (locType == 2)type = "LBS";
            else if (locType == 3)type = "WIFI";
            id_tvConnWay.setText(Html.fromHtml(String.format(getString(R.string.text_locate_type),fontBlck,end,type)));
            id_tvBaterry.setText(Html.fromHtml(String.format(getString(R.string.text_battery),fontBlck,end,power+"%")));
            id_tvLocatTime.setText(Html.fromHtml(String.format(getString(R.string.text_loc_time),fontBlck,end,locTime)));
            id_tvAddress.setText(address);
            id_tvStatus.setText(Html.fromHtml(String.format(getString(R.string.text_dev_status),fontBlck,end,fontColor,device_status,end)));
        }
        id_llInfoWindow.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isDraw){
                    isDraw = false;
                    ViewGroup.LayoutParams layoutParams = id_llInfoWindow.getLayoutParams();
                    layoutParams.height =ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = w - 80;
                    id_llInfoWindow.setLayoutParams(layoutParams);
                }
                if (id_llInfoWindow.getViewTreeObserver().isAlive()){
                    id_llInfoWindow.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
        return infoWindow;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        zoom = cameraPosition.zoom;
        if(babyLatlng!=null && view_Bootom_Tool.getVisibility() == View.VISIBLE){
            moveBaby(babyLatlng);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        zoom = cameraPosition.zoom;
    }

    private String getNumberString(int num){
        String format = "";
        if(num < 10){
            format = "0" + num;
        }else if(num >= 10){
            format = String.valueOf(num);
        }
        return format;
    }

    private void setDateAndTime(){
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setDoneText(getResources().getString(R.string.text_ok))
                .setCancelText(getResources().getString(R.string.cancel));
        cdp.show(getActivity().getSupportFragmentManager(), "HistoryRouteFragment");
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String temp = year + "-" + getNumberString(monthOfYear+1) + "-" + getNumberString(dayOfMonth);
        if (isStart){
            startDate = temp;
        }else {
            endDate = temp;
        }
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment().setOnTimeSetListener(this).setThemeLight();
        rtpd.show(getActivity().getSupportFragmentManager(),"HistoryRouteFragment");
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String temp = getNumberString(hourOfDay) + ":" + getNumberString(minute);
        if (isStart){
            startTime = temp;
            startDateStr = startDate +" "+ startTime;
            id_tvStartDate.setText(startDate);
            id_tvStartTime.setText(startTime);
        }else {
            endTime = temp;
            endDateStr = endDate + " "+ endTime;
            id_tvEndDate.setText(endDate);
            id_tvEndTime.setText(endTime);
        }
    }
}
