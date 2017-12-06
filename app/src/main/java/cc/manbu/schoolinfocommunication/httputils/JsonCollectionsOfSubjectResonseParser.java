package cc.manbu.schoolinfocommunication.httputils;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cc.manbu.schoolinfocommunication.bean.R_Subject;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;
/**
 * Created by manbuAndroid5 on 2017/2/6.
 */

public class JsonCollectionsOfSubjectResonseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        LogUtil.e("JsonCollectionsOfSubjectResonseParser===="+result);
        JSONObject resultJSON = new JSONObject(result);// 获取ModelUser类型的JSON对象
        JSONArray jArray = resultJSON.optJSONArray("d");
        return JSONHelper.parseCollection(jArray, ArrayList.class, R_Subject.class);
    }
}
