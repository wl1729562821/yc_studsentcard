package cc.manbu.schoolinfocommunication.push;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.ManbuApplication;
import cc.manbu.schoolinfocommunication.httputils.NetHelper;


public class PopSocket {
	static Socket client;
	private NetHelper netHelper;
	private static Context mContext;
	private String key;
	String domain;
	int port;
	private final static PopSocket popSocket = new PopSocket();
	private long lastGetData = 0;
	public static final String testStr = "test_connecting";
	public String GpsLoginName;
	public static final Map<String,String> UsrMap = new HashMap<>();
	private boolean isGpsLogined;
	private PopSocket(){
		netHelper = NetHelper.getInstance();
	}
	
	public static PopSocket getInstance(Context context){
		mContext = context;
		return  popSocket;
	}
	
	public void heartBeat(){
		long curTime = SystemClock.elapsedRealtime();
		if(lastGetData!=0 && curTime-lastGetData>PushMessageService.HeartBeatIntervalMillis){
			LogUtil.w("heartBeat() 超过"+(PushMessageService.HeartBeatIntervalMillis/60000)+"分钟没有收到数据,关闭连接!");
			Close();
		}
		LogUtil.w("开始发送心跳包...");
		test();
	}

	public boolean  isGpsUserLogined(){
		return isGpsLogined;
	}
	

	public synchronized  void Close() {
		LogUtil.w("PopSocket socket被关闭");
		isGpsLogined = false;
		try {
			if(client!=null){
				client.close();
			}
		} catch (IOException e) {
			LogUtil.e("Close()", e);
		}finally{
			client = null;
			GpsLoginName = null;
		}
	}

	 public boolean canReceive() {
		 if(isSocketConnected() && isGpsLogined){
			 return true;
		 }
			try {
				R_Users rUsers = Configs.get(Configs.Config.CurUser,R_Users.class);
				GpsLoginName = rUsers.getIsTeacher() ? rUsers.getLoginName():rUsers.getSerialnumber();
				if(TextUtils.isEmpty(GpsLoginName)){
					if(mContext!=null){
						((PushMessageService)mContext).stopPushTask();
						PushMessageService.cancelTimerAlarmTask();
					}
					return false;
				}
				if(port ==0 || TextUtils.isEmpty(key) || TextUtils.isEmpty(domain)){
					String str = netHelper.getTCPPopMsgAddress();
					LogUtil.d("canReceive() TCPPopMsgAddress="+str);
					key = str.split(",")[1];
					//key = "9D842515-CBCD-4D48-9CE7-37B0458A6F66";
					domain = str.split(":")[0];

					port = Integer.parseInt(str.substring(str.indexOf(":") + 1,
							str.indexOf(",")));
				}
				// 这里的ip 端口 以及key 调用服务器的GetTCPPopMsgAddress 获取
				client = new Socket(domain, port);
				String Token = Configs.get(Configs.Config.ManbuToken, String.class);
				Token = Token.substring(2);//token没有A_
				Locale locale = ManbuApplication.getInstance().getResources().getConfiguration().locale;
				Token = Token+"_"+ TimeZone.getDefault().getRawOffset()/(1000*60)+"_"+locale.getLanguage()+"-"+locale.getCountry();
				String Gpspwd = UsrMap.get(GpsLoginName);
				if(TextUtils.isEmpty(Gpspwd)){
					Gpspwd = rUsers.getIsTeacher()?"1234567890QWERTYUIOPASDFGHJKL":netHelper.getPwd(GpsLoginName);
					UsrMap.put(GpsLoginName,Gpspwd);
				}
				
				UserMsgPackage LoginResult = login(GpsLoginName, Gpspwd,
						key, Token);
				LogUtil.w("canReceive() LoginResult="+LoginResult);
				 if(null==LoginResult){
					 isGpsLogined = false;
					 return false;
				 }
				if (LoginResult.CMD.equals("Login")) {
					String r = LoginResult.getParameters().get(0);
					if ("1".equals(r)) {
						// Key 错误
						LogUtil.e("Gps平台登录Key 错误");
						UsrMap.put(GpsLoginName,null);
						isGpsLogined = false;
						return false;
					} else if ("2".equals(r)) {
						// 用户或者是密码错误
						LogUtil.e("Gps平台登录 用户名或者是密码错误");
						isGpsLogined = false;
						return false;
					} else {
						LogUtil.w(domain+"登录成功!  当前登录设备:"+GpsLoginName);
						//startTimer();
						isGpsLogined = true;
						lastGetData = SystemClock.elapsedRealtime();
						heartBeat();
						return true;
						// 登录成功
//							UserMsgPackage Message = RevMsg();
//							if (Message.CMD == "Push") {
//								// 服务器推过来的消息
//								String popMsg = Message.getParameters().get(0);
//								Log.i(ManbuConfig.DOMAIN,"登录成功!  "+popMsg);
//							}

						}
					}
			} catch (Exception e) {
				LogUtil.e("canReceive()", e);
			}
			return false;
		}

	 UserMsgPackage login(String loginname, String password, String key,
                          String Token) {
		 LogUtil.d("login() loginName="+loginname+",password="+password+",key="+key+",Token="+Token);
		UserMsgPackage pg = new UserMsgPackage();
		pg.setCMD("Login");
		ArrayList<String> par = new ArrayList<String>();
		par.add(loginname);
		par.add(password);
		par.add(key);
		par.add(Token);
		pg.setParameters(par);
		sendMsg(pg);
		return RevMsg();
	}
	 
	public boolean isSocketConnected(){
		if(null==client){
			return false;
		}
		if(client.isClosed() || client.isInputShutdown() || client.isOutputShutdown()){
			return false;
		}
		return client.isConnected();
	}
	
	public UserMsgPackage RevMsg() {
		LogUtil.d("RevMsg() 开始获取推送消息");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			//client.setSoTimeout(1000);
			if(!isSocketConnected()){
				LogUtil.e("RevMsg() socket连接断开!");
				Close();
				return null;
			}
			String str = in.readLine();
			if(null==str){
				LogUtil.e("RevMsg() in.readLine() == null");
				Close();
				return null;
			}
			lastGetData = SystemClock.elapsedRealtime();
			LogUtil.e("RevMsg() str="+str);
			String hexString = str.split(" ")[1];
			if(testStr.equals(hexString)){
				return null;
			}
			byte[] Bye = hexStringToBytes(hexString);
			String jsonStr = new String(Bye, 0, Bye.length, "utf-8");
			LogUtil.e("RevMsg() 接收到的推送消息:"+str);
			UserMsgPackage pg = new UserMsgPackage();
			pg.Parameters = new ArrayList<String>();
			JSONObject jf = new JSONObject(jsonStr);
			pg.setCMD(jf.getString("CMD"));
			JSONArray ar = jf.getJSONArray("Parameters");
			for (int i = 0; i < ar.length(); i++) {
				pg.Parameters.add(ar.getString(i));
			}
			return pg;
		} catch (Exception e) {
			LogUtil.e("RevMsg()", e);
			if(!ArrayIndexOutOfBoundsException.class.isInstance(e)){
				Close();
			}
		}
		return null;
	}

	private void test(){
		if(client!=null){
			try {
				String msg = "notih "+testStr + "\r\n";
				sendData(msg);
			} catch (IOException e) {
				Close();
				LogUtil.e("test()", e);
			}
		}
		
	}
	 UserMsgPackage sendMsg(UserMsgPackage pg) {
		 LogUtil.d("sendMsg() UserMsgPackage="+pg);
		try {
			String entitystr = "{CMD:\"" + pg.CMD + "\",Parameters:[";
			for (int i = 0; i < pg.getParameters().size(); i++) {
				if (i != 0) {
					entitystr += ",";
				}
				entitystr += ("\"" + pg.getParameters().get(i) + "\"");
			}

			entitystr += "]}";
			String msg = "notiv "
					+ bytesToHexString(entitystr.getBytes("utf-8")) + "\r\n";

			sendData(msg);
			LogUtil.d("sendMsg() Send message success!");
			return null;
		} catch (IOException e) {
			LogUtil.e("sendMsg()", e);
		}
		return null;
	}

	private synchronized void sendData(String msg) throws IOException {
		PrintWriter out = new PrintWriter(client.getOutputStream());
		out.println(msg);
		out.flush();
		LogUtil.w("sendData() 数据发送成功!");
	}

	 String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	 byte[] hexStringToBytes(String hexString) {
		if (TextUtils.isEmpty(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
