package cc.manbu.schoolinfocommunication.httputils;


import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;

/**
 * Created by manbuAndroid5 on 2017/2/6.
 */

public class JsonObjectResonseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        JSONObject resultJSON = new JSONObject(result);// 获取ModelUser类型的JSON对象
        JSONObject json_d = resultJSON.optJSONObject("d");
        LogUtil.e("result from JsonObjectResonseParser===>"+result);
        return JSONHelper.parseObject(json_d, resultClass);
    }
}
