package cc.manbu.schoolinfocommunication.tools;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import cc.manbu.schoolinfocommunication.config.Configs;


/**
 * Created by manbuAndroid5 on 2017/1/22.
 */

public class XUtil {
    /**
     * 发送get请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        Configs.lastOperateTime = System.currentTimeMillis();
        return cancelable;
    }

    /**
     * 发送post请求，通过map字符串专递参数
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        Configs.lastOperateTime = System.currentTimeMillis();
        return cancelable;
    }

    /**
     * 发送post请求,通过json字符串专递参数
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, String json, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        Configs.lastOperateTime = System.currentTimeMillis();
        return cancelable;
    }

    /**
     * 发送post请求,同步，通过json字符串专递参数
     */
    public static Object postSync(String url,String json,Class<?> clazz) throws Throwable {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        Configs.lastOperateTime = System.currentTimeMillis();
        return x.http().postSync(params,clazz);
    }
    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable upLoadFile(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable downLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
}
