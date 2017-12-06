package cc.manbu.schoolinfocommunication.httputils;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cc.manbu.schoolinfocommunication.bean.SHX002COW;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

/**
 * Created by manbuAndroid5 on 2017/2/6.
 */

public class JsonCollectionsOfCheckWorksResonseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        LogUtil.e("JsonCollectionsOfCheckWorksResonseParser===="+result);
        JSONObject resultJSON = new JSONObject(result);// 获取ModelUser类型的JSON对象
        JSONObject json_d = resultJSON.optJSONObject("d");
        JSONArray jArray = json_d.optJSONArray("rows");
        return JSONHelper.parseCollection(jArray, ArrayList.class, SHX002COW.class);
    }
}
