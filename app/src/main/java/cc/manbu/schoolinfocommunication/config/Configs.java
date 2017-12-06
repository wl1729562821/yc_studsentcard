package cc.manbu.schoolinfocommunication.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
;import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

/**
 * Created by manbuAndroid5 on 2017/4/18.
 */

public class Configs {
    public static long lastOperateTime = 0L;
    private static String key;
    private static String domain;
    public static int whichRole = 0;//0,學生，1，老師
    public static int curDeviceType = -1;

    public static final String APP_PACKAGE_NAME = "cc.manbu.schoolinfocommunication";
    //longteng:BE64D5B1-140A-43B6-BE15-FB0D4F884224 //manbu:834C7533-39CB-41FD-B9ED-13321185C88F
    public static final String SERVER_KEY = "834C7533-39CB-41FD-B9ED-13321185C88F";
    public final static HashSet<Integer> allowLoadDeviceStore = new HashSet<>();
    public static final String ADDRESS_A = "/server/mobileserver.asmx/";
    public static final String ADDRESS_B = "/server/RobotService.asmx/";
    private static final String CHINA_KEY = SERVER_KEY;
    private static final String UNCHINA_KEY = "EF39B444-B5E3-4ADE-927C-D8273077C668";
    static {
        key = CHINA_KEY;
        domain = "http://www.longtengcard.com";
        domain = "http://school.manbu.cc";
        //学生卡(002)
        allowLoadDeviceStore.add(22);
        //app支持520设备(520一代) DDType=82
        allowLoadDeviceStore.add(27);
        //app支持520设备（520第二版wifi版本，同时529也一样支持) DDType=83
        allowLoadDeviceStore.add(36);
        //app支持520a设备(520二代，不带wifi) DDType=82
        allowLoadDeviceStore.add(40);
        //定位鞋，宠物 206设备 DDType=82
        allowLoadDeviceStore.add(41);
        //手表智能机学生版582设备 DDType=88
        allowLoadDeviceStore.add(43);
        //手表智能机成人版521设备 DDType=0
        allowLoadDeviceStore.add(44);
        //机器人小E
        allowLoadDeviceStore.add(49);
        //守护星025(学生卡管理系统+520设备功能)
        allowLoadDeviceStore.add(50);
        //机器人曼迪
        allowLoadDeviceStore.add(51);
        //机器人米咔
        allowLoadDeviceStore.add(52);
    }
    public static String STUDENT_KEY = key;
    public static String STUDENT_DOMAIN = domain;
    public static String Photo_RCV = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + APP_PACKAGE_NAME
            + File.separator
            + "photopath" + File.separator;
    //獲取當前設備序列號
    public static String getCurDeviceSerialnumber(){
        R_Users user = get(Config.CurUser,R_Users.class);
        if (user != null){
            return user.getSerialnumber();
        }
        return null;
    }
    public static boolean isUserLogined(){
        R_Users user = get(Config.CurUser,R_Users.class);
        return user!=null && user.isLogined();
    }
    /**
     * 存储全局变量，注意该方法子进程与主进程不能保持数据同步，如要保持同步，请调用putInConfig()保存数据，调用getFromConfig()获取数据!
     * @param key
     * @param value
     */
    public static void put(Config key, Object value) {
        ManbuApplication.staticStore.put(key, value);
        if (key.isSerializable) {
            putInConfig(ManbuApplication.getInstance(),key.name(), value);
        }
    }
    public static void putInConfig(Context context, String key, Object value) {
        SharedPreferences spf = null;
        String currentProcess = context.getApplicationInfo().processName;
        try {
            Context main = context.createPackageContext(ManbuApplication
                            .getInstance().getPackageName(),
                    Context.CONTEXT_IGNORE_SECURITY);
            spf = main.getSharedPreferences("ManbuConfig", Context.MODE_PRIVATE);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assert spf != null;
        SharedPreferences.Editor editor = spf.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            Class clazz = value.getClass();
            if ("cookies".equals(key) && clazz == BasicCookieStore.class) {
                editor.putString("cookies",
                        JSONHelper.serializeCookies((CookieStore) value));
            } else
            if (JSONHelper.isSingle(clazz)) {
                if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                } else if (value instanceof String) {
                    editor.putString(key, (String) value);
                }
            } else {
                editor.putString(key, JSONHelper.toComplexJSON(value));
            }
        }
        editor.apply();
    }
    public static <T> T get(Config key, Class<T> cls) {
        T obj = (T) ManbuApplication.staticStore.get(key);
        if(obj==null && key.isSerializable){
            obj = getFromConfig(ManbuApplication.getInstance(),key.name(), cls);
            ManbuApplication.staticStore.put(key, obj);
        }
        return obj;
    }
    public static <T> T getFromConfig(Context context, String key,
                                      Class<T> clazz) {
        SharedPreferences spf = null;
        try {
            Context main = context.createPackageContext(ManbuApplication
                            .getInstance().getPackageName(),
                    Context.CONTEXT_IGNORE_SECURITY);
            spf = main.getSharedPreferences("ManbuConfig", Context.MODE_PRIVATE);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if("cookies".equals(key) && clazz==BasicCookieStore.class){
            return (T) JSONHelper.toCookies(spf.getString("cookies", null));
        }
        if (JSONHelper.isSingle(clazz)) {
            assert spf != null;
            return (T) spf.getAll().get(key);
        } else {
            assert spf != null;
            String json = spf.getString(key, null);
            if (json != null) {
                Gson gson = new Gson();
                return gson.fromJson(json, clazz);
            }
        }
        return null;
    }
    public static <T> ArrayList<T> getList(Config key, Class<T> cls) {
        ArrayList<T> list = (ArrayList<T>) ManbuApplication.staticStore.get(key);
        if(list==null && key.isSerializable){
            list = getFromConfigReturnList(ManbuApplication.getInstance(),key.name(), cls);
        }
        return list;
    }
    public static <T> ArrayList<T> getFromConfigReturnList(Context context, String key,
                                                           final Class<T> clazz){
        SharedPreferences spf = null;
        try {
            Context main = context.createPackageContext(ManbuApplication
                            .getInstance().getPackageName(),
                    Context.CONTEXT_IGNORE_SECURITY);
            spf = main.getSharedPreferences("ManbuConfig", Context.MODE_PRIVATE);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assert spf != null;
        String json = spf.getString(key, null);
        if (json != null) {
            Gson gson = new Gson();
            if(Config.isArrayType(key)){
                Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
                ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);
                if(jsonObjs!=null && jsonObjs.size()>0){
                    ArrayList<T> data = new ArrayList<T>();
                    for(JsonObject jo:jsonObjs){
                        data.add(gson.fromJson(jo,clazz));
                    }
                    return data;
                }
                return null;
            }
        }
        return null;
    }
    /**
     *全局变量
     *
     * @author Carlos
     */
    public static enum Config {
        DeviceIEMI(true),
        DeviceBinds(true,true),
        CurDeviceBind(true),
        AutoLogin(true),
        RememberPassword(true),
        CurMainIotConfig(true),
        SteeringEngineAngleScope(true),
        PushEnable(true),
        IsHistoryRoutePlayingShowLine(true),
        IsHistoryRouteManualZoomMap(true),
        IsHistoryRouteRectifyTrack(true),
        /*
         *
         */
        Cookies(true),
        /**
         * 屏幕高度
         */
        ScreenHeight(true),
        /**
         * 屏幕宽度
         */
        ScreenWidth(true),
        ToolbarHeight(true),
        ManbuToken(true),
        CurClientUser(true),
        CurUser(true),
        CurDevice(true),
        InfraredDeviceList(true,true),
        CurAllDevices(true,true);
        /**
         * 是否保存在本地
         */
        public boolean isSerializable;
        public boolean isArrayType;
        private static final HashSet<String> ArrayTypeKeys = new HashSet<>();
        static {
            Config[] allKeys = Config.values();
            for(Config config:allKeys){
                if(config.isArrayType){
                    ArrayTypeKeys.add(config.name());
                }
            }
        }
        private Config(boolean isSerializable){
            this.isSerializable = isSerializable;
        }
        private Config(boolean isSerializable,boolean isArrayType){
            this.isSerializable = isSerializable;
            this.isArrayType = isArrayType;
        }
        public static boolean isArrayType(String key){
            return ArrayTypeKeys.contains(key);
        }
    }
}
