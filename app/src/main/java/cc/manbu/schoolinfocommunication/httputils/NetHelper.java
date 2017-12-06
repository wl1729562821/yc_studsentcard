package cc.manbu.schoolinfocommunication.httputils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.Device_Geography;
import cc.manbu.schoolinfocommunication.bean.Device_GeographySearchOpt;
import cc.manbu.schoolinfocommunication.bean.Hw_Homework;
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation;
import cc.manbu.schoolinfocommunication.bean.Page;
import cc.manbu.schoolinfocommunication.bean.R_Department;
import cc.manbu.schoolinfocommunication.bean.R_Subject;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.bean.SHX002COW;
import cc.manbu.schoolinfocommunication.bean.SHX007AlarmClock;
import cc.manbu.schoolinfocommunication.bean.SHX007Scenemode;
import cc.manbu.schoolinfocommunication.bean.SHX520Alarmclock;
import cc.manbu.schoolinfocommunication.bean.SHX520SearchCOW;
import cc.manbu.schoolinfocommunication.bean.SR_Exam;
import cc.manbu.schoolinfocommunication.bean.SR_Score;
import cc.manbu.schoolinfocommunication.bean.Sleave;
import cc.manbu.schoolinfocommunication.config.API;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.listener.HttpRespnse;
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener;
import cc.manbu.schoolinfocommunication.tools.DateUtil;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;
import cc.manbu.schoolinfocommunication.tools.XUtil;

/**
 * Created by manbuAndroid5 on 2017/4/18.
 */

public class NetHelper {
    private static NetHelper netHelper;
    private NetHelper() {}

    public static boolean mLogin=false;
    public static NetHelper getInstance() {
        if (netHelper == null) {
            synchronized (NetHelper.class) {
                netHelper = new NetHelper();
            }
        }
        return netHelper;
    }

    private void sendErrorEvent(String err){
        ViewEvent event = new ViewEvent();
        event.setMessage(Constant.EVENT_ON_ERROR);
        event.setContent(err);
        EventBus.getDefault().post(event);
    }

    public void login(String loginName,String pwd,final int flgs){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.Login);
        String jsonStr = "{'LoginName':'%s','Password':'%s','Key':'%s'}";

        jsonStr = String.format(jsonStr, loginName,pwd,Configs.STUDENT_KEY);
        Log.e("NetHelper","login "+jsonStr +";"+url);
        XUtil.Post(url,jsonStr,new MyCallBack<R_Users>(){
            @Override
            public void onSuccess(R_Users result) {
                super.onSuccess(result);
                Log.e("NetHelper","login onSuccess "+result);
                if(result==null){
                    return;
                }
                Log.e("NetHelper","login onSuccess "+result+";"+result.toString());
                ResposeEvent event = new ResposeEvent();
                event.setMessage("Login");
                event.setrUsers(result);
                event.setFlg(flgs);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("NetHelper","login onError"+ex.getMessage());
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void login(String loginName, String pwd, final int flgs, final HttpCallListener callListener){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.Login);
        String jsonStr = "{'LoginName':'%s','Password':'%s','Key':'%s'}";

        jsonStr = String.format(jsonStr, loginName,pwd,Configs.STUDENT_KEY);
        Log.e("NetHelper","login "+jsonStr +";"+url);
        XUtil.Post(url,jsonStr,new MyCallBack<R_Users>(){
            @Override
            public void onSuccess(R_Users result) {
                super.onSuccess(result);
                HttpRespnse<R_Users> respnse=new HttpRespnse<R_Users>();
                Log.e("NetHelper","login onSuccess "+result);
                if(result!=null){
                    respnse.setData(result);
                    callListener.onNext(respnse);
                }else {
                    callListener.onError(0,"登录失败，请稍后在试");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("NetHelper","login onError"+ex.getMessage());
                callListener.onError(0,"登录失败，请稍后在试");
                //sendErrorEvent(ex.getMessage());
            }
        });
    }

//    {"d":"True"}
    public void saveToken(Context context, final R_Users user){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SaveAndroidToken);
        String jsonStr = "{'LoginName':%s,'Token':'%s'}";
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        final R_Users users = user;
        try {
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mTelephonyMgr.getDeviceId();
            String Token = imei + "_XSK_cityeasy_" + pi.versionCode+"_pro";
            Configs.put(Configs.Config.ManbuToken, Token);
            jsonStr = String.format(jsonStr, user.getId(),Token);
            XUtil.Post(url,jsonStr,new MyCallBack<String>(){
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    LogUtil.e("saveToken result===>"+result);
                    LogUtil.e("saveToken result===>"+user.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String d = jsonObject.getString("d");
                        int flg = 0;
                        if ("True".equals(d)){
                            flg = 1;
                        }
                        ResposeEvent event = new ResposeEvent();
                        event.setMessage("SaveAndroidToken");
                        event.setFlg(flg);
                        event.setrUsers(users);
                        EventBus.getDefault().post(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    sendErrorEvent(ex.getMessage());
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveToken(Context context, final R_Users user, final HttpCallListener callListener){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SaveAndroidToken);
        String jsonStr = "{'LoginName':%s,'Token':'%s'}";
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        final R_Users users = user;
        try {
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mTelephonyMgr.getDeviceId();
            String Token = imei + "_XSK_cityeasy_" + pi.versionCode+"_pro";
            Configs.put(Configs.Config.ManbuToken, Token);
            jsonStr = String.format(jsonStr, user.getId(),Token);
            XUtil.Post(url,jsonStr,new MyCallBack<String>(){
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    LogUtil.e("saveToken result===>"+result);
                    LogUtil.e("saveToken result===>"+user.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String d = jsonObject.getString("d");
                        int flg = 0;
                        if ("True".equals(d)){
                            flg = 1;
                        }
                        ResposeEvent event = new ResposeEvent();
                        event.setMessage("SaveAndroidToken");
                        event.setFlg(flg);
                        event.setrUsers(users);
                        HttpRespnse<R_Users> respnse=new HttpRespnse<>();
                        respnse.setData(users);
                        callListener.onNext(respnse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callListener.onError(0,"登录失败，请稍后再试");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    callListener.onError(0,"登录失败，请稍后再试");
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            callListener.onError(0,"登录失败，请稍后再试");
        }
    }

    public void accessMobileDeviceAndLocation(String serialNumber){
        String jsonStr = "{'Serialnumber':'%s'}";
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetMobileDevicAndLocation);
        jsonStr = String.format(jsonStr,serialNumber);
        XUtil.Post(url,jsonStr,new MyCallBack<MobileDevicAndLocation>(){
            @Override
            public void onSuccess(MobileDevicAndLocation result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetMobileDevicAndLocation");
                event.setDevicAndLocation(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void accessMobileDeviceAndLocation(final String serialNumber, final HttpCallListener listener){
        String jsonStr = "{'Serialnumber':'%s'}";
        final String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetMobileDevicAndLocation);
        jsonStr = String.format(jsonStr,serialNumber);
        XUtil.Post(url,jsonStr,new MyCallBack<MobileDevicAndLocation>(){
            @Override
            public void onSuccess(MobileDevicAndLocation result) {
                super.onSuccess(result);
                Log.e(NetHelper.class.getSimpleName(),"accessMobileDeviceAndLocation onSuccess");
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetMobileDevicAndLocation");
                event.setDevicAndLocation(result);
                HttpRespnse<ResposeEvent> httpRespnse=new HttpRespnse<>();
                httpRespnse.setData(event);
                listener.onNext(httpRespnse);
                //EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e(NetHelper.class.getSimpleName(),"accessMobileDeviceAndLocation onerror"+ex.getMessage());
                Log.e(NetHelper.class.getSimpleName(),"accessMobileDeviceAndLocation["+serialNumber+"];"+url);
                listener.onError(1,"网络异常,请稍后再试");
                //sendErrorEvent(ex.getMessage());
            }
        });
    }

    public MobileDevicAndLocation getMobileDevicAndLocationBySync(String serialnumber){
        if (serialnumber == null)return null;
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetMobileDevicAndLocation);
        String jsonStr = "{'Serialnumber':'%s'}";
        jsonStr = String.format(jsonStr,serialnumber);
        try {
            MobileDevicAndLocation mobileDevicAndLocation= (MobileDevicAndLocation)XUtil.postSync(url,jsonStr, MobileDevicAndLocation.class);
            if (mobileDevicAndLocation != null){
                LogUtil.e("getMobileDevicAndLocation sync==="+mobileDevicAndLocation.getSerialnumber());
            }
            return mobileDevicAndLocation;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
 /*{"d":{"__type":"EasyUIDataGrid","total":1,"rows":[{"__type":"SchoolClient.CityGPS.Device_Geography","_id":"b559178c-bd7c-4874-8144-b7a520a0e6ec","Serialnumber":"52909697467356","Type":2,"Shape":0,
 "Radius":0,"CreateTime":"\/Date(1492440901785)\/","geography":"22.538324,114.026173;22.531441,
 114.024990;22.529656,114.032562","Name":"规划","IsIn":false,"Period":[]}]}}*/

    public void accessE_ZoneList(){
        final String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SearchDevice_Geography_V2);
        R_Users users = Configs.get(Configs.Config.CurUser,R_Users.class);
        if (users == null)return;
        Device_GeographySearchOpt opt = new Device_GeographySearchOpt();
        opt.setSerialnumber(users.getSerialnumber());
        opt.setPageIndex(1);
        opt.setPageSize(20);
        String str = "{\"" + "opt" + "\":" + JSONHelper.toJSON(opt) + "}";
        Log.e("NetHelper","accessE_ZoneList "+str);
        XUtil.Post(url,str,new MyCallBack<List<Device_Geography>>(){
            @Override
            public void onSuccess(List<Device_Geography> result) {
                super.onSuccess(result);
                Log.e("NetHelper","onSuccess:"+result.size()+";"+url);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SearchDevice_Geography_V2");
                event.setGeographies(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":0}
    public void saveGeography(Device_Geography geography,boolean isAdd){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SaveOrUpdateGeography);
        String str = "{\"entity\":" + geography + ",\"IsAdd\":\"" + isAdd + "\"}";
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SaveOrUpdateGeography");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":""}
    public void deleteGeography(Device_Geography geography, final int pos){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.DeleteGeography);
        HashMap<String, Object> map = new HashMap<>();
        String idString = geography.get_id();
        map.put("Id", idString);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("DeleteGeography");
                event.setFlg(pos);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void accessPhoneList(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX520GetPhoneBook_V2);
        String jsonStr = "{'Serialnumber':'%s'}";
        jsonStr = String.format(jsonStr,Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("SHX520GetPhoneBook_V2 res===>"+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520GetPhoneBook_V2");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void setPhoneList(String params,final int flg){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX520SetPhoneBook_V2);
        XUtil.Post(url,params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520SetPhoneBook_V2");
                event.setContent(result);
                event.setFlg(flg);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    /*{"d":{"__type":"EasyUIDataGrid","total":12,"rows":[{"_id":"e6b5b876-817e-4532-b9ef-0c9c498a5b65","Serialnumber":"30020000000009","Type":1,"Time":"\/Date(1403694709000)\/","State":"异常","Code":"0001","Ty":"进入"},{"_id":"4a79c863-dbb3-422a-8f82-cd786d38ec45","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403694661000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"388a9a20-d6b4-4d38-aef8-088aae9442ad","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"9c9bb885-0522-4bb3-8073-d665d900e2a2","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"94b30aa0-dae7-4faa-87b5-c499035060b0","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"a3dd2d55-23fc-4f86-89f1-26ed75b4ef55","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"e5473d40-4060-4c55-b1f3-db50759bbaae","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"dbf48766-ff92-4d98-90f6-5fc64b6f25ba","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"b9e3bda6-7f2e-47e5-b9b3-1e8a16078207","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"41de0cac-d0e8-4cbb-8009-126c8575fa2e","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"9808bfe8-5713-4114-b4c2-0c9bb9f3ccfc","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"},{"_id":"9837ff1b-ea83-4911-8d76-c7b36ac8458e","Serialnumber":"30020000000009","Type":2,"Time":"\/Date(1403606818000)\/","State":"异常","Code":"0002","Ty":"离开"}]}}*/

    public void accessCheckRecords(int size,int index,int month){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SearchSHX002COW);
        SHX520SearchCOW opt = new SHX520SearchCOW();
        opt.setSerialnumber(Configs.getCurDeviceSerialnumber());
        opt.setPageSize(size);
        opt.setPageIndex(index);
        opt.setMonth(month);
        String str = "{\"" + "opt" + "\":" + JSONHelper.toJSON(opt) + "}";
        XUtil.Post(url,str,new MyCallBack<List<SHX002COW>>(){
            @Override
            public void onSuccess(List<SHX002COW> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setCheckWorksList(result);
                event.setMessage("SearchSHX002COW");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void accessClocks(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX007Getalarmclock);
        String jsonStr = "{'Serialnumber':'%s'}";
        jsonStr = String.format(jsonStr, Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,jsonStr,new MyCallBack<SHX007AlarmClock>(){
            @Override
            public void onSuccess(SHX007AlarmClock result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007Getalarmclock");
                event.setAlarmClock(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void access520Clocks(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX520Getalarmclock);
        String jsonStr = "{'Serialnumber':'%s'}";
        jsonStr = String.format(jsonStr, Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,jsonStr,new MyCallBack<SHX520Alarmclock>(){
            @Override
            public void onSuccess(SHX520Alarmclock result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520Getalarmclock");
                event.setShx520Alarmclock(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void setClocks(SHX007AlarmClock alarmClock){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX007Setalarmclock);
        String str = "{\"" + "SHX007AlarmClock" + "\":" + JSONHelper.toComplexJSON(alarmClock) + "}";
        LogUtil.e("str==="+str);
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("res==="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007Setalarmclock");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void set520Clocks(SHX520Alarmclock alarmclock){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX5200SetAlarmclock);
        String str = "{\"" + "SHX520AlarmClock" + "\":" + JSONHelper.toComplexJSON(alarmclock) + "}";
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX5200SetAlarmclock");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"http://school.manbu.cc:80/Mobile/About.aspx?S=1"}
    public void accessUrl(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetAboutUrl);
        String josnstr = "{\"GetAboutUrl\":\"\"}";
        XUtil.Post(url,josnstr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("ressss==="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetAboutUrl");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessAnnouncementUrl(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetAnnouncementUrl);
        String josnstr = "{\"GetAnnouncementUrl\":\"\"}";
        XUtil.Post(url,josnstr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetAnnouncementUrl");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessNewsUrl(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetNewsUrl);
        String josnstr = "{\"GetNewsUrl\":\"\"}";
        XUtil.Post(url,josnstr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetNewsUrl");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessActiviyUrl(){
        String url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.GetActivityUrl);
        String josnstr = "{\"GetActivityUrl\":\"\"}";
        XUtil.Post(url,josnstr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetActivityUrl");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void getDeviceDetial(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetDeviceDetial);
        String jsonParams = "{'SerialNumber':'%s'}";
        jsonParams = String.format(jsonParams, Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,jsonParams,new MyCallBack<Device>(){
            @Override
            public void onSuccess(Device result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetDeviceDetial");
                event.setDevice(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void setSleepTime(int powerState,int[] openTime,int[]offTime){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520SetSleepTime);
        Map<String,Object> map = new HashMap<>();
        map.put("Serialnumber", Configs.getCurDeviceSerialnumber());
        map.put("flag", powerState);
        map.put("OpenHour", openTime[0]);
        map.put("OpenMin", openTime[1]);
        map.put("CloseHour", offTime[0]);
        map.put("CloseMin", offTime[1]);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("resutl===="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520SetSleepTime");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void setFairWall(boolean open){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.FireWall);
        String jsonStr  = "{'Serialnumber':'%s','type':%s}";
        jsonStr = String.format(jsonStr, Configs.getCurDeviceSerialnumber(),open);
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("setFairWall==="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("FireWall");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void findDevice(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520FindDevice);
        Map<String, Object> map = new HashMap<>();
        map.put("Serialnumber", Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("findDevice"+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520FindDevice");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void serachLocate(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520SigleAddressQuery);
        Map<String, Object> map = new HashMap<>();
        map.put("Serialnumber", Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520SigleAddressQuery");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void setMode(int mode){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520SetWorkMode);
        Map<String, Object> map = new HashMap<>();
        map.put("Serialnumber", Configs.getCurDeviceSerialnumber());
        map.put("Mode", mode);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520SetWorkMode");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void reomtePower(int power){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520RemotePowerSet);
        String str = "{'Serialnumber':'%s','power':%s}";
        str = String.format(str,Configs.getCurDeviceSerialnumber(),power);
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520RemotePowerSet");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void resetDevice(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520Factory);
        String str = "{'Serialnumber':'%s'}";
        str = String.format(str,Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520Factory");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void homeWorkNotice(int index,int size){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetUserHw_Homework);
        Page page = new Page();
        page.setPageIndex(index);
        page.setPageCount(size);
        page.setPageSize(size);
        page.setTotal(size);
        String str = "{\"" + "pg" + "\":" + JSONHelper.toJSON(page) + "}";
        XUtil.Post(url,str,new MyCallBack<List<Hw_Homework>>(){
            @Override
            public void onSuccess(List<Hw_Homework> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetUserHw_Homework");
                event.setHw_homeworks(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void getExam(int index,int size){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetCurSR_Exam);
        Page page = new Page();
        page.setPageIndex(index);
        page.setPageCount(size);
        page.setPageSize(size);
        page.setTotal(size);
        String str = "{\"" + "pg" + "\":" + JSONHelper.toJSON(page) + "}";
        XUtil.Post(url,str,new MyCallBack<List<SR_Exam>>(){
            @Override
            public void onSuccess(List<SR_Exam> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetCurSR_Exam");
                event.setSr_exams(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void getScores(SR_Exam exam){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SearchScore);
        Map<String,Object> parms = new HashMap<>();
        parms.put("SR_ExamId", exam.getId());
        parms.put("R_StudentId", Configs.get(Configs.Config.CurUser,R_Users.class).getId());
        parms.put("R_SubjectId",-1);
        parms.put("minScore", 0);
        parms.put("maxScore", 100);
        XUtil.Post(url,parms,new MyCallBack<List<SR_Score>>(){
            @Override
            public void onSuccess(List<SR_Score> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SearchScore");
                event.setSr_scores(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"1"}
    public void setTaskOfSteps(int steps,String reward){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520ParentChildInteraction);
        Map<String,Object> map = new HashMap<>();
        map.put("Serialnumber", Configs.getCurDeviceSerialnumber());
        map.put("Value", steps);
        map.put("reward", reward);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520ParentChildInteraction");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void cancelTask(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520CancelParentChildInteraction);
        String jsonStr = "{'Serialnumber':'%s'}";
        jsonStr = String.format(jsonStr,Configs.getCurDeviceSerialnumber());
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("cancelTask====="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520CancelParentChildInteraction");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void getDayOffList(int index,int size){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetUserSleave);
        Page page = new Page();
        page.setPageIndex(index);
        page.setPageCount(size);
        page.setPageSize(size);
        page.setTotal(size);
        String str = "{\"" + "pg" + "\":" + JSONHelper.toJSON(page) + "}";
        XUtil.Post(url,str,new MyCallBack<List<Sleave>>(){
            @Override
            public void onSuccess(List<Sleave> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetUserSleave");
                event.setSleaves(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":""}
    public void addOffDay(String sTime, String eTime, String reason, String title){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.AddSleave);
        Sleave sleave = new Sleave();
        sleave.setR_StudentId(Configs.get(Configs.Config.CurUser,R_Users.class).getId());
        sleave.setStartTime(DateUtil.parse("yyyy-MM-dd HH:mm", sTime));
        sleave.setEndTime(DateUtil.parse("yyyy-MM-dd HH:mm", eTime));
        sleave.setReason(reason);
        sleave.setTitle(title);
        sleave.setState(0);
        String str = "{\"" + "entity" + "\":" + JSONHelper.toJSON(sleave) + "}";
        /*String str = "{\"entity\":{\"StartTime\":\"%s\",\"Reason\":\"%s\",\"State\":0,\"R_Student\":null," +
                "\"EndTime\":\"%s\",\"title\":\"%s\",\"R_StudentId\":null}}";
        str = String.format(str,sTime,reason,eTime,title);
        LogUtil.e("str==="+str);*/
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("ressss==="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("AddSleave");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessCurriculums(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SearchCurriculumlis);
        String str = "{'pg':{'PageIndex':0,'PageCount':10,'PageSize':10,'Total':10}}";
        XUtil.Post(url,str,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SearchCurriculumlis");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessHomeWorkByTeacher(int index,int size){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetHomeworkByTeacher);
        Sleave.PageSHelp page = new Sleave.PageSHelp();
        page.setPageIndex(index);
        page.setPageSize(size);
        String str = "{\"" + "pg" + "\":" + JSONHelper.toJSON(page) + "}";
        XUtil.Post(url,str,new MyCallBack<List<Hw_Homework>>(){
            @Override
            public void onSuccess(List<Hw_Homework> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetHomeworkByTeacher");
                event.setHw_homeworks(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessDepartmentList(int id){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetR_DepartmentListByTeacherId);
        String jsonStr = "{'TeacherId':'%s'}";
        jsonStr = String.format(jsonStr,id);
        XUtil.Post(url,jsonStr,new MyCallBack<List<R_Department>>(){
            @Override
            public void onSuccess(List<R_Department> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetR_DepartmentListByTeacherId");
                event.setDepartments(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":""}
    public void addHomeWork(int curDepartmentId,int curSubjectId,String str,String title){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.UpdateOrAddHomework);
        Hw_Homework hw = new Hw_Homework();
        hw.setR_DepartmentId(curDepartmentId);
        hw.setR_SubjectId(curSubjectId);
        hw.setContext(str);
        hw.setTitle(title);
        String jsonStr = "{\"" + "entity" + "\":" + JSONHelper.toComplexJSON(hw) + "}";
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("UpdateOrAddHomework");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessSleaves(int index,int size){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SearchSleave);
        Sleave.PageSHelp opt = new Sleave.PageSHelp();
        opt.PageIndex = index;
        opt.PageSize = size;
        String str = "{\"" + "opt" + "\":" + JSONHelper.toJSON(opt) + "}";
        XUtil.Post(url,str,new MyCallBack<List<Sleave>>(){
            @Override
            public void onSuccess(List<Sleave> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SearchSleave");
                event.setSleaves(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"True"}
    public void allowSleave(int id, String remark){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.AgreeSleave);
        Map<String,Object> parms = new HashMap<>();
        parms.put("SleaveId", id);
        parms.put("remark", remark);
        XUtil.Post(url,parms,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("reslut==="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("AgreeSleave");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void refuseSleave(int id, String remark){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.DisAgreeSleave);
        Map<String,Object> parms = new HashMap<>();
        parms.put("SleaveId", id);
        parms.put("remark", remark);
        XUtil.Post(url,parms,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("DisAgreeSleave");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessSubjects(int id){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetR_SubjectListByTeacherId);
        String jsonStr = String.format("{'TeacherId':%s}",id);
        XUtil.Post(url,jsonStr,new MyCallBack<List<R_Subject>>(){
            @Override
            public void onSuccess(List<R_Subject> result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetR_SubjectListByTeacherId");
                event.setSubjects(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessStudents(int DeptId){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetR_StudentListByDeptId);
        String jsonStr = String.format("{'DeptId':%s}", DeptId);
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray array = obj.optJSONArray("d");
                    List<R_Users> list = (List<R_Users>) JSONHelper.parseCollection(array, ArrayList.class, R_Users.class);
                    ResposeEvent event = new ResposeEvent();
                    event.setMessage("GetR_StudentListByDeptId");
                    event.setrUsersList(list);
                    EventBus.getDefault().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void accessTeacherClass(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetTeacherCurriculum);
        String s = "{'GetTeacherCurriculum':''}";
        XUtil.Post(url,s,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("GetTeacherCurriculum");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"修改成功"}
    public void undatePassword(String oldPwd, final String newPwd, String loginName){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.UpdatePwd);
        HashMap<String, Object> map = new HashMap<>();
        map.put("old_pwd", oldPwd);
        map.put("new_pwd", newPwd);
        map.put("LoginName", loginName);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("UpdatePwd");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public String getTCPPopMsgAddress(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetTCPPopMsgAddress);
        String jsonStr = "{'GetTCPPopMsgAddressResult':''}";
        String result = null;
        try {
            String string = (String) XUtil.postSync(url,jsonStr,String.class);
            LogUtil.e("getTCPPopMsgAddress====="+string);
            JSONObject resultJSON = new JSONObject(string);
            result = (String) resultJSON.opt("d");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
    public String getPwd(String loginName){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.GetPwd);
        String jsonStr = "{'Serialnumber':'"+loginName+"'}";
        String result = null;
        try {
            String string = (String) XUtil.postSync(url,jsonStr,String.class);
            LogUtil.e("getPwd====="+string);
            JSONObject resultJSON = new JSONObject(string);
            result = (String) resultJSON.opt("d");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
//    {"d":"命令已经加入队列"}
    public void setUploadInterval(String imei,String interval){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX007SetTCITYEASYIntervalforContinuousTracking);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Serialnumber", imei);
        map.put("ConTimeSpan", interval);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setContent(result);
                event.setMessage("SHX007SetTCITYEASYIntervalforContinuousTracking");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void set520UploadInterval(String imei,String interval){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX520SetInterval);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Serialnumber", imei);
        map.put("value", interval);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("rrrrr=="+result);
                ResposeEvent event = new ResposeEvent();
                event.setContent(result);
                event.setMessage("SHX520SetInterval");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void accessSceneMode(String imei){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX007GetScenemode);
        String jsonStr = String.format("{'Serialnumber':'%s'}", imei);
        XUtil.Post(url,jsonStr,new MyCallBack<SHX007Scenemode>(){
            @Override
            public void onSuccess(SHX007Scenemode result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007GetScenemode");
                event.setScenemode(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"命令已经加入队列"}
    public void setSceneMode(Map<String,Object> map){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX007SetScenemode);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007SetScenemode");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void setClassTime(String imei,boolean state){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX002ClassTimeControl);
        String jsonStr = "{'Serialnumber':'%s','IsOpen':%s}";
        jsonStr = String.format(jsonStr,imei,state);
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX002ClassTimeControl");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void setGPS(String imei,boolean open){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX007SetGPSOpen);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Serialnumber", imei);
        map.put("IsOpen", open);
        XUtil.Post(url,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("remmmm=="+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007SetGPSOpen");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
//    {"d":"命令提交成功"}
    public void setFiarwall(String imei,boolean open){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX520FireWallSetting);
        String jsonStr  = "{'Serialnumber':'%s','Flag':%s}";
        jsonStr = String.format(jsonStr,imei,open);
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520FireWallSetting");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void setKeyLock(String imei,boolean open){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A + API.getApi(API.SHX520KeylockSet);
        String jsonstr = "{'Serialnumber':'%s','klock':%s}";
        jsonstr = String.format(jsonstr,imei,open ? 1 : 0);
        XUtil.Post(url,jsonstr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520KeylockSet");
                event.setContent(result);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public String getDeviceTraceDataStr(String serialnumber,String startTime,String endTime){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.GetDeviceTraceDataStr_V2);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Sreialnumber", serialnumber);
            jsonParams.put("StartTime", startTime);
            jsonParams.put("EndTime", endTime);
            String result = (String) XUtil.postSync(url,jsonParams.toString(),String.class);
            JSONObject resultJSON = new JSONObject(result);// 获取ModelUser类型的JSON对象
            return resultJSON.getString("d");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
    public void setListenPhone(final String listenPhone){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX007ListenPhone);
        String jsonParam = "{'Serialnumber':'%s','ListenPhone':'%s'}";
        jsonParam = String.format(jsonParam,Configs.getCurDeviceSerialnumber(),listenPhone);
        XUtil.Post(url,jsonParam,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("res===?"+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX007ListenPhone");
                event.setContent(listenPhone);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }
    public void setListenNo(final String listenNo){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SHX520SetListenNo);
        String json = "{'Serialnumber':'%s','ListenNo':'%s'}";
        json = String.format(json,Configs.getCurDeviceSerialnumber(),listenNo);
        XUtil.Post(url,json,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.e("res===>"+result);
                ResposeEvent event = new ResposeEvent();
                event.setMessage("SHX520SetListenNo");
                event.setContent(listenNo);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                sendErrorEvent(ex.getMessage());
            }
        });
    }

    public void deleteE_Zone(String id, final HttpCallListener listener){
        String url = "http://school.manbu.cc/server/mobileserver.asmx/DeleteGeography";
        String json = "{'Id':'"+id+"'}";
        //json = String.format(json,Configs.getCurDeviceSerialnumber(),id);
        Log.e("NetHelper","deleteE_Zone"+id+";"+json);
        XUtil.Post(url,json,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                listener.onNext(new HttpRespnse<String>());
                Log.e("NetHelper","deleteE_Zone onSuccess "+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                listener.onError(500,"删除失败，请稍后再试");
                Log.e("NetHelper","deleteE_Zone onError"+ex.getMessage());
            }
        });
    }
}
