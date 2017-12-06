package cc.manbu.schoolinfocommunication.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;

import java.util.ArrayList;
import java.util.List;

import cc.manbu.schoolinfocommunication.config.ManbuApplication;

/**
 * Created by gongyong2014 on 2016/11/28.
 * 高德地图轨迹纠偏
 */

public class GaodeTraceUtil {
    private static  MyLBSTraceClient mLBSTraceClient = null;
    private static final List<List<TraceLocation>> mTraceLocationSet = new ArrayList<List<TraceLocation>>();
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    public static void reset(){
        mTraceLocationSet.clear();
        curTraceLocationList = null;
        if(mLBSTraceClient!=null){
            //停止之前的纠结纠偏
            mLBSTraceClient.isRunning = false;
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //必须在主线程中初始化LBSTraceClient，不能在同一子线程中同时初始化LBSTraceClient与调用queryProcessedTrace()
                mLBSTraceClient = new MyLBSTraceClient(ManbuApplication.getInstance().getApplicationContext()){
                    @Override
                    public void queryProcessedTrace(int i, List<TraceLocation> list, int i1, TraceListener traceListener) {
                        if(isRunning){
                            super.queryProcessedTrace(i, list, i1, traceListener);
                        }else {
                            Log.w("MyLBSTraceClient","停止轨迹纠偏");
                        }
                    }
                };
            }
        });
    }


    private static List<TraceLocation> curTraceLocationList;
    public static void addLatlng(double lat,double lng,long time){
        TraceLocation tl = new TraceLocation();
        tl.setLatitude(lat);
        tl.setLongitude(lng);
        tl.setTime(time);
        if(curTraceLocationList == null){
            curTraceLocationList = new ArrayList<>();
            mTraceLocationSet.add(curTraceLocationList);
        }
        curTraceLocationList.add(tl);
        if(curTraceLocationList.size()==100){
            curTraceLocationList = new ArrayList<>();
            mTraceLocationSet.add(curTraceLocationList);
        }
    }

    public static boolean isTraceProcessing(){
        return mLBSTraceClient!=null && mLBSTraceClient.isRunning;
    }


    public static void queryProcessedTrace(final int mCoordinateType,final MyTraceListener mMyTraceListener){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mTraceLocationSet.size()>1){
                    List<TraceLocation> last = mTraceLocationSet.get(mTraceLocationSet.size()-1);
                    if(last.size()<5){
                        mTraceLocationSet.remove(mTraceLocationSet.size()-1);
                        mTraceLocationSet.get(mTraceLocationSet.size()-1).addAll(last);
                    }
                }
                final int size = mTraceLocationSet.size();
                Log.w("GaodeTraceUtil","开始处理轨迹数据，总共有"+size+"条轨迹要纠偏。。。");
                mLBSTraceClient.isRunning = true;
                mLBSTraceClient.queryProcessedTrace(mLBSTraceClient.curLineId, mTraceLocationSet.get(mLBSTraceClient.curLineId), mCoordinateType,new TraceListener(){
                    private MyLBSTraceClient mMyLBSTraceClient;
                    @Override
                    public void onRequestFailed(int lineID, String errorInfo) {
                        if(mMyLBSTraceClient==null){
                            mMyLBSTraceClient = mLBSTraceClient;
                        }
                        Log.w("GaodeTraceUtil","第"+lineID+"条轨迹纠偏处理失败："+errorInfo);
                        List<TraceLocation> data = mTraceLocationSet.get(mLBSTraceClient.curLineId);
                        handleTraceResult(lineID,data);
                    }

                    private void handleTraceResult(int lineID,List<TraceLocation> data){
                        if(mMyLBSTraceClient.isRunning){
                            if(mMyLBSTraceClient.curLineId<size-1){
                                mMyLBSTraceClient.curLineId++;
                                mMyLBSTraceClient.queryProcessedTrace(mMyLBSTraceClient.curLineId, mTraceLocationSet.get(mLBSTraceClient.curLineId),mCoordinateType,this);
                            }else {
                                mMyLBSTraceClient.isRunning = false;
                            }
                            if(mMyTraceListener!=null){
                                mMyTraceListener.onTraceHandled(!mLBSTraceClient.isRunning,data);
                                if(lineID==0){
                                    mMyTraceListener.onTraceFirstQueryFinished();
                                }
                            }
                        }
                    }

                    @Override
                    public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
                        if(mMyLBSTraceClient==null){
                            mMyLBSTraceClient = mLBSTraceClient;
                        }
                        if(index==0){
                            Log.w("GaodeTraceUtil","开始纠偏第"+lineID+"条轨迹");
                        }

                    }

                    @Override
                    public void onFinished(int lineID, List<LatLng> linepoints, int distance, int waitingtime) {
                        Log.w("GaodeTraceUtil","第"+lineID+"条轨迹纠偏处理完成");
                        List<TraceLocation> data = new ArrayList<TraceLocation>();
                        for(LatLng p:linepoints){
                            TraceLocation tl = new TraceLocation();
                            tl.setLatitude(p.latitude);
                            tl.setLongitude(p.longitude);
                            data.add(tl);
                        }
                        handleTraceResult(lineID,data);
                    }
                });
            }
        });
    }

    private static class MyLBSTraceClient extends LBSTraceClient {
        public boolean isRunning;
        public int curLineId;
        public MyLBSTraceClient(Context context){
            super(context);
            isRunning = true;
        }
    }

    public static interface MyTraceListener{
        /**
         * 处理轨迹纠偏结果
         * @param isFinished 判断所有轨迹是否已经处理完毕
         * @param data 经过纠偏后的一段轨迹数据
         */
        void onTraceHandled(boolean isFinished, List<TraceLocation> data);

        /**
         * 第一次轨迹处理完成时回掉方法
         */
        void onTraceFirstQueryFinished();

    }
}
