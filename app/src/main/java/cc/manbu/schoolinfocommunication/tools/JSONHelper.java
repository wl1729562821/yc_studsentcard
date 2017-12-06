package cc.manbu.schoolinfocommunication.tools;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xutils.common.util.LogUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHelper {
	private static String TAG = "JSONHelper";

	/**
	 * 将对象转换成Json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJSON(Object obj) {
		JSONStringer js = new JSONStringer();
		serialize(js, obj);

		return js.toString();
	}

	/**
	 * 将对象转换成Json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toComplexJSON(Object obj) {
		// JSONStringer js = new JSONStringer();
		// serializeComplex(js, obj);
		// toolbar_back js.toString();
		Gson g = new Gson();
		return g.toJson(obj);
	}

	@SuppressLint("NewApi")
	public static String serializeCookies(CookieStore cooks) {
		List<Cookie> list = cooks.getCookies();
		// for(int i=0;i<list.size();i++){
		// list.get(i).getExpiryDate()
		// }
		// String str = cooks.toString();
		// str = str.substring(1, str.length()-1);
		// str = str.replace(": ", "\":\"");
		// str = str.replace(", ", ",");
		// String[] ss = str.split(",");
		// str = "";
		// for(int i=0;i<ss.length;i++){
		// ss[i] = ss[i].substring(1, ss[i].length()-1);
		// ss[i] = ss[i].replace("][", "\",\"");
		// ss[i] = "{\""+ss[i]+"\"}";
		// if(i<ss.length-1){
		// ss[i]+=",";
		// }
		// str+=ss[i];
		// }
		// str = "["+str+"]";
		for (Cookie c : cooks.getCookies()) {
			LogUtil.d("cookie过期时间:" + DateUtil.format("yyyy-MM-dd HH:mm:ss", c.getExpiryDate()));
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
				.create();
		return gson.toJson(list);

	}

	/**
	 * 通过反射获取对象的所有属性，包括父类的属性
	 * 
	 * @param obj
	 * @param clz
	 * @return
	 */
	public static Field[] getAllFields(Object obj, Class clz) {
		if (clz == null) {
			return null;
		}
		if (clz.getGenericSuperclass() != null) {
			List<Field> list = new ArrayList<Field>();
			Field[] fields = clz.getDeclaredFields();
			if (fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					list.add(fields[i]);
				}

			}
			Class superClz = clz.getSuperclass();
			if (superClz != null) {
				fields = getAllFields(obj, superClz);
			}
			if (obj.getClass() == clz) {
				if (fields != null && fields.length > 0) {
					list.addAll(Arrays.asList(fields));
				}
			}
			return list.toArray(new Field[] {});
		}
		return null;
	}

	public static CookieStore toCookies(String cookies) {
		if (cookies == null) {
			return null;
		}
		CookieStore cooks = new BasicCookieStore();
		BasicClientCookie cook = null;
		try {
			JSONArray ja = new JSONArray(cookies);
			JSONObject jo = null;
			for (int i = 0; i < ja.length(); i++) {
				jo = ja.optJSONObject(i);
				cook = new BasicClientCookie(jo.optString("name"),
						jo.optString("value"));
				cook.setDomain(jo.optString("cookieDomain"));
				cook.setPath(jo.optString("cookiePath"));
				cook.setVersion(jo.optInt("cookieVersion"));
				cook.setSecure(jo.optBoolean("isSecure"));
				cook.setExpiryDate(DateUtil.parse("yyyy-MM-dd HH:mm:ss:SSS",
						jo.optString("cookieExpiryDate")));
				jo = jo.optJSONObject("attribs");
				Iterator iterator = jo.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = jo.optString(key);
					if (value != null && !"".equals(value)) {
						cook.setAttribute(key, value);
					}
				}
				cooks.addCookie(cook);
			}
			return cooks;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 序列化为JSON
	 * 
	 * @param js
	 * @param o
	 * 
	 */
	private static void serializeComplex(JSONStringer js, Object o) {
		if (isNull(o)) {
			try {
				js.value(null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}

		Class<?> clazz = o.getClass();
		if (isObject(clazz)) { // 对象
			serializeComplexObject(js, o);
		} else if (isArray(clazz)) { // 数组
			serializeArray(js, o);
		} else if (isCollection(clazz)) { // 集合
			Collection<?> collection = (Collection<?>) o;
			serializeCollect(js, collection);
		} else { // 单个值
			try {
				js.value(o);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 序列化为JSON
	 * 
	 * @param js
	 * @param o
	 * 
	 */
	public static void serialize(JSONStringer js, Object o) {
		if (isNull(o)) {
			try {
				js.value(null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}

		Class<?> clazz = o.getClass();
		if (isObject(clazz)) { // 对象
			serializeObject(js, o);
		} else if (isArray(clazz)) { // 数组
			serializeArray(js, o);
		} else if (isCollection(clazz)) { // 集合
			Collection<?> collection = (Collection<?>) o;
			serializeCollect(js, collection);
		} else { // 单个值
			try {
				js.value(o);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 序列化数组
	 * 
	 * @param js
	 * @param array
	 */
	private static void serializeArray(JSONStringer js, Object array) {
		try {
			js.array();
			for (int i = 0; i < Array.getLength(array); ++i) {
				Object o = Array.get(array, i);
				serialize(js, o);
			}
			js.endArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将基本数据类型的数组转换成对应包装类型的数组，如:int[] ->  Integer[]
	 * @param primitiveArray  基本数据类型的数组
	 * @param packagedComponentType 对应包装类型
	 * @param convertByteToCByte 如果是Byte[]，并且其将用于C语言等字节类型范围在0~255的环境时，convertByteToCByte为ture
	 * @return
	 */
	public static Serializable[] primitiveTypeArrayToPackagedArray(
			Object primitiveArray,
			Class<? extends Serializable> packagedComponentType,
			boolean convertByteToCByte) {
		if (primitiveArray == null || packagedComponentType == null
				|| !isSingle(packagedComponentType)) {
			return null;
		}
		Class componentType = primitiveArray.getClass().getComponentType();
		boolean isByte = Byte.TYPE.isAssignableFrom(componentType);
		if (isSingle(componentType)) {
			int len = Array.getLength(primitiveArray);
			Serializable[] array = new Serializable[len];
			for (int i = 0; i < len; i++) {
				if (isByte && convertByteToCByte) {
					byte b = (Byte) Array.get(primitiveArray, i);
					array[i] = Integer.valueOf(b & 0xff);
				} else {
					array[i] = (Serializable) Array.get(primitiveArray, i);
				}
			}
			return array;

		}
		return null;
	}

	/**
	 * 将数组转换成可打印的字符串
	 * @param array
	 * @return
	 */
	public static String arrayToString(Object[] array) {
		if (array == null) {
			return "null";
		}
		if (array.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder(array.length * 6);
		sb.append('[');
		sb.append(array[0]);
		for (int i = 1; i < array.length; i++) {
			sb.append(",");
			sb.append(array[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * 序列化集合
	 * 
	 * @param js
	 * @param collection
	 */
	private static void serializeCollect(JSONStringer js,
			Collection<?> collection) {
		try {
			js.array();
			for (Object o : collection) {
				serialize(js, o);
			}
			js.endArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 序列化复合对象
	 * 
	 * @param js
	 * @param obj
	 */
	private static void serializeObject(JSONStringer js, Object obj) {
		try {
			js.object();
			Class<? extends Object> objClazz = obj.getClass();
			Method[] methods = objClazz.getDeclaredMethods();
			Field[] fields = getAllFields(obj, objClazz);
			for (Field field : fields) {
				try {
					Class fieldType = field.getType();
					String fieldGetName = parseMethodName(field.getName(),
							"get");
					if (!haveMethod(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = objClazz.getMethod(fieldGetName,
							new Class[] {});
					Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
					String result = null;
					if (Date.class.isAssignableFrom(fieldType)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.US);
						result = sdf.format((Date) fieldVal);

					} else {
						if (null != fieldVal) {
							result = String.valueOf(fieldVal);
						}
					}
					js.key(field.getName());
					serialize(js, result);
				} catch (Exception e) {
					continue;
				}
			}
			js.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 序列化对象
	 * 
	 * @param js
	 * @param obj
	 */
	private static void serializeComplexObject(JSONStringer js, Object obj) {
		try {
			js.object();
			Class<? extends Object> objClazz = obj.getClass();
			Method[] methods = objClazz.getDeclaredMethods();
			Field[] fields = getAllFields(obj, objClazz);
			for (Field field : fields) {
				try {
					String fieldType = field.getType().getSimpleName();
					String fieldGetName = parseMethodName(field.getName(),
							"get");
					if (!haveMethod(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = objClazz.getMethod(fieldGetName,
							new Class[] {});
					Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
					String result = null;
					if ("Date".equals(fieldType)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.US);
						result = sdf.format((Date) fieldVal);

					} else {
						if (null != fieldVal) {
							if (!isSingle(fieldVal.getClass())) {
								result = toComplexJSON(fieldVal);
								js.key(field.getName());
								js.value(new JSONObject(result));
								continue;
							} else {
								result = String.valueOf(fieldVal);

							}
						}
					}
					js.key(field.getName());
					serializeComplex(js, result);
				} catch (Exception e) {
					continue;
				}
			}
			js.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 * @param fieldMethod
	 * @return boolean
	 */
	public static boolean haveMethod(Method[] methods, String fieldMethod) {
		for (Method met : methods) {
			if (fieldMethod.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 拼接某属性的 get或者set方法
	 * 
	 * @param fieldName
	 * @param methodType
	 * @return
	 */
	public static String parseMethodName(String fieldName, String methodType) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return methodType + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	/**
	 * set属性的值到Bean
	 * 
	 * @param obj
	 * @param valMap
	 */
	public static void setFieldValue(Object obj, Map<String, String> valMap) {
		Class<?> cls = obj.getClass();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = getAllFields(obj, cls);

		for (Field field : fields) {
			try {
				String setMetodName = parseMethodName(field.getName(), "set");
				if (!haveMethod(methods, setMetodName)) {
					continue;
				}
				Method fieldMethod = cls.getMethod(setMetodName,
						field.getType());
				String value = valMap.get(field.getName());
				if (null != value && !"".equals(value)) {
					String fieldType = field.getType().getSimpleName();
					if ("String".equals(fieldType)) {
						fieldMethod.invoke(obj, StringUtil.getNoNullStr(value));
					} else if ("Date".equals(fieldType)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.US);
						Date temp = sdf.parse(value);
						fieldMethod.invoke(obj, temp);
					} else if ("Integer".equals(fieldType)
							|| "int".equals(fieldType)) {
						Integer intval = Integer.parseInt(value);
						fieldMethod.invoke(obj, intval);
					} else if ("Long".equalsIgnoreCase(fieldType)) {
						Long temp = Long.parseLong(value);
						fieldMethod.invoke(obj, temp);
					} else if ("Double".equalsIgnoreCase(fieldType)) {
						Double temp = Double.parseDouble(value);
						fieldMethod.invoke(obj, temp);
					} else if ("Boolean".equalsIgnoreCase(fieldType)) {
						Boolean temp = Boolean.parseBoolean(value);
						fieldMethod.invoke(obj, temp);
					} else {
						System.out.println("setFieldValue not supper type:"
								+ fieldType);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

	}

	/**
	 * 对象转Map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getFieldValueMap(Object obj) {
		Class<?> cls = obj.getClass();
		Map<String, String> valueMap = new HashMap<String, String>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = getAllFields(obj, cls);
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parseMethodName(field.getName(), "get");
				if (!haveMethod(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls
						.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				String result = null;
				if ("Date".equals(fieldType)) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
					result = sdf.format((Date) fieldVal);

				} else {
					if (null != fieldVal) {
						result = String.valueOf(fieldVal);
					}
				}
				valueMap.put(field.getName(), result);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;

	}

	/**
	 * 给对象的字段赋值
	 * 
	 * @param obj
	 * @param fieldSetMethod
	 * @param fieldType
	 * @param value
	 */
	public static void setFiedlValue(Object obj, Method fieldSetMethod,
									 String fieldType, Object value) {

		try {
			if (null != value && !"".equals(value)) {
				if ("String".equals(fieldType)) {
					fieldSetMethod.invoke(obj,
							StringUtil.getNoNullStr(value.toString()));
				} else if ("Date".equals(fieldType)) {
					Date temp = null;

					try {
						long mi = Long.parseLong(parseDate(value + ""));
						temp = new Date(mi);
					} catch (Exception e) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
						temp = sdf.parse(value.toString());
					}
					fieldSetMethod.invoke(obj, temp);
				} else if ("Integer".equals(fieldType)
						|| "int".equals(fieldType)) {
					Integer intval = Integer.parseInt(value.toString());
					fieldSetMethod.invoke(obj, intval);
				} else if ("short".equals(fieldType)
						|| "Short".equals(fieldType)) {
					Short intval = Short.parseShort(value.toString());
					fieldSetMethod.invoke(obj, intval);
				} else if ("byte".equals(fieldType) || "Byte".equals(fieldType)) {
					Byte intval = Byte.parseByte(value.toString());
					fieldSetMethod.invoke(obj, intval);
				} else if ("float".equals(fieldType)
						|| "Float".equals(fieldType)) {
					Float intval = Float.parseFloat(value.toString());
					fieldSetMethod.invoke(obj, intval);
				} else if ("Long".equalsIgnoreCase(fieldType)) {
					Long temp = Long.parseLong(value.toString());
					fieldSetMethod.invoke(obj, temp);
				} else if ("Double".equalsIgnoreCase(fieldType)) {
					Double temp = Double.parseDouble(value.toString());
					fieldSetMethod.invoke(obj, temp);
				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
					Boolean temp = Boolean.parseBoolean(value.toString());
					fieldSetMethod.invoke(obj, temp);
				} else {
					fieldSetMethod.invoke(obj, value);

				}
			}

		} catch (Exception e) {
			//
		}

	}

	private static String parseDate(String value) {

		value = value.replace("\\", "");
		value = value.replace("/", "");
		Pattern p = Pattern.compile("Date\\(-*[0-9]*\\)");
		Matcher m = p.matcher(value);
		while (m.find()) {
			String macth = m.group();
			String newStr = getDateStr(macth);
			value = value.replace(macth, newStr);
		}

		return value;
	}

	private static String getDateStr(String value) {

		String reslut = "";
		value = value.replace("Date(", "");
		value = value.replace(")", "");

		/*
		 * long mi = Long.parseLong(value); Date dat = new Date(mi);
		 * GregorianCalendar gc = new GregorianCalendar(); gc.setTime(dat);
		 * java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
		 * "yyyy-MM-dd hh:mm:ss"); reslut = format.format(gc.getTime()); toolbar_back
		 * reslut;
		 */
		return value;
	}

	/**
	 * 反序列化简单对象
	 * 
	 * @param jo
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(JSONObject jo, Class<T> clazz) {
		if (clazz == null || isNull(jo)) {
			return null;
		}

		T obj = newInstance(clazz);
		if (obj == null) {
			return null;
		}
		if (isMap(clazz)) {
			setField(obj, jo);
		} else {
			// 取出bean里的所有方法
			Method[] methods = clazz.getMethods();
			Field[] fields = getAllFields(obj, clazz);
			for (Field f : fields) {
				String setMetodName = parseMethodName(f.getName(), "set");
				if (!haveMethod(methods, setMetodName)) {
					continue;
				}
				try {
					Method fieldMethod = clazz.getMethod(setMetodName,
							f.getType());
					setField(obj, fieldMethod, f, jo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return obj;
	}

	/**
	 * 反序列化简单对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String jsonString, Class<T> clazz) {
		if (clazz == null || jsonString == null || jsonString.length() == 0) {
			return null;
		}

		JSONObject jo = null;
		try {
			jo = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (isNull(jo)) {
			return null;
		}

		return parseObject(jo, clazz);
	}

	/**
	 * 反序列化数组对象
	 * 
	 * @param ja
	 * @param clazz
	 * @return
	 */
	public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
		if (clazz == null || isNull(ja)) {
			return null;
		}

		int len = ja.length();

		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, len);

		for (int i = 0; i < len; ++i) {
			try {
				JSONObject jo = ja.getJSONObject(i);
				T o = parseObject(jo, clazz);
				array[i] = o;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return array;
	}

	/**
	 * 反序列化数组对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T[] parseArray(String jsonString, Class<T> clazz) {
		if (clazz == null || jsonString == null || jsonString.length() == 0) {
			return null;
		}
		JSONArray jo = null;
		try {
			jo = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (isNull(jo)) {
			return null;
		}

		return parseArray(jo, clazz);
	}

	/**
	 * 反序列化泛型集合
	 * 
	 * @param jsonString
	 * @param collectionClazz
	 * @param genericType
	 * @return
	 */
	public static <T> Collection<T> parseCollection(String jsonString,
													Class<?> collectionClazz, Class<T> genericType) {
		if (collectionClazz == null || genericType == null
				|| jsonString == null || jsonString.length() == 0) {
			return null;
		}
		JSONArray jo = null;
		try {
			jo = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (isNull(jo)) {
			return null;
		}

		return parseCollection(jo, collectionClazz, genericType);
	}

	/**
	 * 根据类型创建对象
	 * 
	 * @param clazz
	 * @return
	 */
	private static <T> T newInstance(Class<T> clazz) {
		if (clazz == null)
			return null;
		T obj = null;
		try {

			obj = clazz.newInstance();
		} catch (Exception e) {
			// e.printStackTrace();
			if (clazz.toString().equals("java.util.List")
					|| "interface java.util.List".equals(clazz.toString())) {
				return (T) new ArrayList();
			}

		}
		return obj;
	}

	public static <K, V> Map<K, V> parseMap(String jsonStr,
											Class<? extends Map> mapClass, Class<K> keyClass,
											Class<V> valueClass) {
		try {
			JSONObject jo = new JSONObject(jsonStr);
			return parseMap(jo, mapClass, keyClass, valueClass);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <K, V> Map<K, V> parseMap(JSONObject jo,
											Class<? extends Map> mapClass, Class<K> keyClass,
											Class<V> valueClass) {
		Map<K, V> map = newInstance(mapClass);
		Iterator<String> keyIter = jo.keys();
		K k;
		V v;
		try {
			Method method = null;
			if (String.class != keyClass) {
				method = keyClass.getMethod("valueOf", String.class);
			}
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				k = (K) (String.class != keyClass ? method.invoke(null, key) : key);
				v = parseObject(jo.getJSONObject(key), valueClass);
				map.put(k, v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 设定Map的值
	 * 
	 * @param obj
	 * @param jo
	 */
	private static void setField(Object obj, JSONObject jo) {
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = jo.keys();
			String key;
			Object value;
			@SuppressWarnings("unchecked")
			Map<String, Object> valueMap = (Map<String, Object>) obj;
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jo.get(key);
				valueMap.put(key, value);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 反序列化泛型集合
	 * 
	 * @param ja
	 * @param collectionClazz
	 * @param genericType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> parseCollection(JSONArray ja,
													Class<?> collectionClazz, Class<T> genericType) {

		if (collectionClazz == null || genericType == null || isNull(ja)) {
			return null;
		}

		Collection<T> collection = (Collection<T>) newInstance(collectionClazz);

		for (int i = 0; i < ja.length(); ++i) {
			try {
				Object obj = ja.get(i);
				T o = null;
				if (obj instanceof JSONObject) {
					JSONObject jo = ja.getJSONObject(i);
					o = parseObject(jo, genericType);
				} else {
					o = (T) obj;
				}

				collection.add(o);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return collection;
	}

	/**
	 * 设定字段的值
	 * 
	 * @param obj
	 * @param fieldSetMethod
	 * @param f
	 * @param jo
	 */
	private static void setField(Object obj, Method fieldSetMethod, Field f,
								 JSONObject jo) {
		String name = f.getName();
		Class<?> clazz = f.getType();
		try {
			if (isArray(clazz)) { // 数组
				Class<?> c = clazz.getComponentType();
				JSONArray ja = jo.optJSONArray(name);
				if (!isNull(ja)) {
					Object array = parseArray(ja, c);
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(),
							array);
				}
			} else if (isDate(clazz)) {
				Object o = jo.opt(name);
				if (o != null) {
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
				}
			}

			else if (isCollection(clazz)) { // 泛型集合
				// 获取定义的泛型类型
				Class<?> c = null;
				Type gType = f.getGenericType();
				if (gType instanceof ParameterizedType) {
					ParameterizedType ptype = (ParameterizedType) gType;
					Type[] targs = ptype.getActualTypeArguments();
					if (targs != null && targs.length > 0) {
						Type t = targs[0];
						c = (Class<?>) t;
					}
				}

				JSONArray ja = jo.optJSONArray(name);
				if (!isNull(ja)) {
					Object o = parseCollection(ja, clazz, c);
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
				}
			} else if (isSingle(clazz)) { // 值类型
				Object o = jo.opt(name);
				if (o != null) {
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
				}
			} else if (isEnum(clazz)) {
				Method valueof = clazz.getMethod("valueOf", String.class);
				String str = jo.optString(name);
				Object o = valueof.invoke(null, str);
				if (o != null) {
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
				}
			} else if (isObject(clazz)) { // 对象
				JSONObject j = jo.optJSONObject(name);
				if (!isNull(j)) {
					Object o = parseObject(j, clazz);
					setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
				}
			} else if (isList(clazz)) { // 列表
				// JSONObject j = jo.optJSONObject(name);
				// if (!isNull(j)) {
				// Object o = parseObject(j, clazz);
				// f.set(obj, o);
				// }
			} else {
				throw new Exception("unknow type!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设定字段的值
	 * 
	 * @param obj
	 * @param f
	 * @param jo
	 */
	private static void setField(Object obj, Field f, JSONObject jo) {
		String name = f.getName();
		Class<?> clazz = f.getType();
		try {
			if (isArray(clazz)) { // 数组
				Class<?> c = clazz.getComponentType();
				JSONArray ja = jo.optJSONArray(name);
				if (!isNull(ja)) {
					Object array = parseArray(ja, c);
					f.set(obj, array);
				}
			} else if (isCollection(clazz)) { // 泛型集合
				// 获取定义的泛型类型
				Class<?> c = null;
				Type gType = f.getGenericType();
				if (gType instanceof ParameterizedType) {
					ParameterizedType ptype = (ParameterizedType) gType;
					Type[] targs = ptype.getActualTypeArguments();
					if (targs != null && targs.length > 0) {
						Type t = targs[0];
						c = (Class<?>) t;
					}
				}

				JSONArray ja = jo.optJSONArray(name);
				if (!isNull(ja)) {
					Object o = parseCollection(ja, clazz, c);
					f.set(obj, o);
				}
			} else if (isSingle(clazz)) { // 值类型
				Object o = jo.opt(name);
				if (o != null) {
					f.set(obj, o);
				}
			} else if (isObject(clazz)) { // 对象
				JSONObject j = jo.optJSONObject(name);
				if (!isNull(j)) {
					Object o = parseObject(j, clazz);
					f.set(obj, o);
				}
			} else if (isList(clazz)) { // 列表
				JSONObject j = jo.optJSONObject(name);
				if (!isNull(j)) {
					Object o = parseObject(j, clazz);
					f.set(obj, o);
				}
			} else {
				throw new Exception("unknow type!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isNull(Object obj) {
		if (obj instanceof JSONObject) {
			return JSONObject.NULL.equals(obj);
		}
		return obj == null;
	}

	/**
	 * 判断是否是值类型
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isSingle(Class<?> clazz) {
		return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
	}

	public static boolean isEnum(Class<?> clazz) {
		return clazz.isEnum();
	}

	/**
	 * 是否布尔值
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return (clazz != null)
				&& ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
						.isAssignableFrom(clazz)));
	}

	/**
	 * 是否数值
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isNumber(Class<?> clazz) {
		return (clazz != null)
				&& ((Byte.TYPE.isAssignableFrom(clazz))
						|| (Short.TYPE.isAssignableFrom(clazz))
						|| (Integer.TYPE.isAssignableFrom(clazz))
						|| (Long.TYPE.isAssignableFrom(clazz))
						|| (Float.TYPE.isAssignableFrom(clazz))
						|| (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
							.isAssignableFrom(clazz)));
	}

	/**
	 * 判断是否是字符串
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isString(Class<?> clazz) {
		return (clazz != null)
				&& ((String.class.isAssignableFrom(clazz))
						|| (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
							.isAssignableFrom(clazz)));
	}

	public static boolean isDate(Class<?> clazz) {

		return (clazz != null) && ((Date.class.isAssignableFrom(clazz)));
	}

	/**
	 * 判断是否是对象
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isObject(Class<?> clazz) {
		return clazz != null && !isSingle(clazz) && !isArray(clazz)
				&& !isCollection(clazz);
	}

	/**
	 * 判断是否是数组
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isArray(Class<?> clazz) {
		return clazz != null && clazz.isArray();
	}

	/**
	 * 判断是否是集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isCollection(Class<?> clazz) {
		return clazz != null && Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否是Map
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isMap(Class<?> clazz) {
		return clazz != null && Map.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否是列表
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isList(Class<?> clazz) {
		return clazz != null && List.class.isAssignableFrom(clazz);
	}

}
