package cc.manbu.schoolinfocommunication.push;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.bean.PushUserMsgM;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.ManbuApplication;
import cc.manbu.schoolinfocommunication.view.activity.MessageActivity;

public class PushMessageService extends Service {
	public static final String Action_MessageUpdated = "com.manbu.smartrobot.Action.MessageUpated";
	private static final Object Lock = new String("Lock");
	private static final String TAG = "PushMessageService";
	public static final long HeartBeatIntervalMillis = 120000L;
	private NotificationManager messageNotificatioManager = null;
	boolean isRunning = false;
	PopSocket mPopSocket;
	private Thread AcceptThread,SendThread;
	static PushMessageService INSTANCE;
	private Handler mHandler;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	 /**
     * 取消系统定时任务
     */
     public static void cancelTimerAlarmTask(){
    	Context context = ManbuApplication.getInstance();
     	PendingIntent operation = PendingIntent.getBroadcast(context, 0, new Intent(PushHeartKeepingBroadcastReceiver.Action), PendingIntent.FLAG_UPDATE_CURRENT);
     	AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     	alarm.cancel(operation);
     }
     
     /**
      * 启动系统定时任务
      * @param intervalMillis
      */
      void runTimerAlarmTask(long intervalMillis){
      	PendingIntent operation = PendingIntent.getBroadcast(this, 0, new Intent(PushHeartKeepingBroadcastReceiver.Action), PendingIntent.FLAG_UPDATE_CURRENT);
      	AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
      	alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intervalMillis, operation);
	    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
		  alarm.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),operation);
	    }
      }
	
	@Override
	public void onCreate() {
		LogUtil.w("PushMessageService创建成功");
		super.onCreate();
		INSTANCE = this;
		mHandler = new Handler();
		messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.w("PushMessageService开始启动...");
		Boolean isPushEnable = Configs.get(Configs.Config.PushEnable, Boolean.TYPE);
		isPushEnable = isPushEnable==null?true:isPushEnable;
		if(isPushEnable){
			if(mPopSocket==null || mPopSocket.isSocketConnected()){
				stopPushTask();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mPopSocket = PopSocket.getInstance(PushMessageService.this);
						startPushTask();
					}
				}, 300);
			}
		}else{
			stopSelf();
		}
		return START_REDELIVER_INTENT;
	}
	
	public Notification getNotification(String title){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		return builder.setLights(0xFF00FF00, 300, 1000)
		.setSmallIcon(R.mipmap.ic_launcher)
		.setContentTitle(title)
		.setAutoCancel(true)
		.setOngoing(true)
		.build();
	}
	
	@Override
	public void onDestroy() {
		LogUtil.e("PushMessageService被销毁");
		stopPushTask();
		super.onDestroy();
	}
	private long lastReceiveTime;
	void startPushTask(){
		isRunning = true;
		AcceptThread = new Thread(){
			@Override
			public void run() {
				while(isRunning && mPopSocket!=null && mPopSocket.canReceive()){
					try {
						LogUtil.w("PushMessageService正在等待云端推送消息...");
						UserMsgPackage ump = mPopSocket.RevMsg();
						if(ump!=null){
							LogUtil.w("PushMessageService "+"获取完毕...");
							EasyUIDataGrid<PushUserMsgM> eudg = new EasyUIDataGrid<>();
							if("Push".equals(ump.CMD)){
								eudg.setValue(new JSONObject(ump.getParameters().get(0)),PushUserMsgM.class);
								List<PushUserMsgM> data = eudg.getRows();
								LogUtil.w("EasyUIDataGrid="+data);
								if(data!=null && data.size()>0){
//									DBManager dm =new DBManager(PushMessageService.this, DataBaseHelper.dataBaseName, null, DataBaseHelper.VERSION, DataBaseHelper.CREATE_TABLE_MG_UserMsgM);
									DbManager db = x.getDb(ManbuApplication.getInstance().getDaoConfig());
									LogUtil.w("当前Gps用户:"+mPopSocket.GpsLoginName);
									for(int i=0;i<data.size();i++){
										PushUserMsgM umm = data.get(i);
										umm.setCreateTime(new Date(System.currentTimeMillis()));
										umm.setUserId(mPopSocket.GpsLoginName);
									}
									LogUtil.w("保存到数据库...");
									//dm.add(data);
									db.save(data);
									LogUtil.w("保存数据完毕!");
									final PushUserMsgM msg = data.get(data.size()-1);
									if(SystemClock.elapsedRealtime()-lastReceiveTime>500){
										lastReceiveTime = SystemClock.elapsedRealtime();
										mHandler.postDelayed(new Runnable(){

											@Override
											public void run() {
												Intent i = new Intent(Action_MessageUpdated);
												i.putExtra("Receiver", msg.getTo());
												sendBroadcast(i);
												Notification notification = getNotification(msg.getContext());
												notification.defaults = Notification.DEFAULT_ALL;
												Intent intent = new Intent(PushMessageService.this,MessageActivity.class);
												intent.putExtra("Receiver", msg.getTo());
												intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
												PendingIntent messagePendingIntent = PendingIntent.getActivity(PushMessageService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
												notification.contentIntent = messagePendingIntent;
												LogUtil.w("通知推送的消息:"+ msg+"");
												messageNotificatioManager.notify(0xFFAACC88, notification);
											}}, 600);
									}
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(isRunning){
					new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							Intent intent = new Intent(PushHeartKeepingBroadcastReceiver.Action);
							intent.putExtra(PushHeartKeepingBroadcastReceiver.Extra_IsBootUp, true);
							sendBroadcast(intent);
						}
					}, 5000);
				}
			}
		};
		AcceptThread.start();
		SendThread = new Thread(){
			@Override
			public void run() {
				synchronized (Lock) {
					while(isRunning){
						try {
							Lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(mPopSocket!=null && mPopSocket.isSocketConnected()){
							mPopSocket.heartBeat();
						}
					}
				}
			}
		};
		SendThread.start();
		runTimerAlarmTask(HeartBeatIntervalMillis);
	}

	void stopPushTask() {
		try {
			if(mPopSocket!=null){
				mPopSocket.Close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isRunning = false;
		try {
			if(AcceptThread!=null){
				AcceptThread.interrupt();
				AcceptThread = null;
			}
		}catch (Exception e){

		}
		try {
			if(SendThread!=null){
				SendThread.interrupt();
				SendThread = null;
			}
		}catch (Exception e){

		}
	}
	
	public static boolean ping(String ip) {
		if(ip==null){
			return false;
		}
		String result = null;
		try {
			Log.w("------ping-----","开始Ping:"+ip+" ...");
			//String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.d("------ping-----",
					"result content : " + stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.w("----ping---", "result = " + result);
		}
		return false;
	}
	
	public static NetworkInfo getActiveNetwork(Context context){
        if (context == null)
            return null;
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
            return null;
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
        return aActiveInfo;
    }
	
	public static class PushHeartKeepingBroadcastReceiver extends BroadcastReceiver {
		public final static String Action = "com.manbu.smartrobot.ACTION.PushHeartKeeping";
		public final static String Extra_IsBootUp = "IsBootUp";
		public PushHeartKeepingBroadcastReceiver(){}
		@Override
		public synchronized void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e(TAG,"PushHeartKeepingBroadcastReceiver接收到广播:"+action);
			Boolean isPushEnable = Configs.get(Configs.Config.PushEnable, Boolean.TYPE);
			boolean isUserLogined = Configs.isUserLogined();
			if(!isUserLogined){
				Log.e(TAG,"PushHeartKeepingBroadcastReceiver 用户没有登录，不处理");
			}
			if(isUserLogined && (isPushEnable==null?true:isPushEnable)){
				if (Action.equals(action)) {
					boolean isBootup = intent.getBooleanExtra(Extra_IsBootUp, false);
					if(isBootup){
						Log.e(TAG,"PushHeartKeepingBroadcastReceiver 尝试重新启动PushMessageService...");
						tryBootUp(context);
					}else{
						Log.e(TAG,"PushHeartKeepingBroadcastReceiver 执行定时任务,发送心跳包...");
						//IotManager.doHeartAction();
						synchronized (Lock) {
							Lock.notify();
						}
					}
				} else{
					if(ManbuApplication.getInstance().isAppRunningInAPI21AndUp()){
						if(ManbuApplication.Action_NetworkChanaged.equals(action)){
							//				Bundle extras = intent.getExtras();
							//				boolean notConnected = extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
							tryBootUp(context);
						}
					}else {
						if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
							tryBootUp(context);
						}
					}
				}
			}
		}
		
		 /**
	     * 判断给定服务是否正在运行
	     * @param context
	     * @param cls
	     * @return
	     */
	    public static boolean isServiceRun(Context context, Class<? extends Service> cls){
	        ActivityManager am = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
	        List<RunningServiceInfo> list = am.getRunningServices(1000);
	        for(RunningServiceInfo info : list){
	            if(info.service.getClassName().equals(cls.getName())){
	                     return true;
	            }
	       }
	       return false;
	   }
		
		private void tryBootUp(final Context context){
			final NetworkInfo ni = getActiveNetwork(context);
			Log.e(TAG,"PushHeartKeepingBroadcastReceiver 网络监听:"+ni);
			R_Users user = Configs.get(Configs.Config.CurUser, R_Users.class);
			if(user!=null && user.isLogined()){
				long now = System.currentTimeMillis();
				if(now-user.lastLoginedTime<=20*24*60*60*1000){
					String curDeviceSerialnumber = Configs.getCurDeviceSerialnumber();
					if(!TextUtils.isEmpty(curDeviceSerialnumber)){
						new Thread(){
							public void run() {
								boolean isCanBootUp = false;
								if(ni!=null && ni.isConnected()){
									String[] addrs = new String[]{(PushMessageService.INSTANCE!=null && PushMessageService.INSTANCE.mPopSocket!=null?PushMessageService.INSTANCE.mPopSocket.domain:null),"www.baidu.com","www.cnnic.net.cn","www.pbc.gov.cn","www.gov.cn"};
									for(String ip:addrs){
										if(ping(ip)){
											isCanBootUp = true;
											break;
										}
									}
								}
								boolean isServiceRunning = isServiceRun(context,PushMessageService.class) && PushMessageService.INSTANCE!=null && PushMessageService.INSTANCE.mPopSocket!=null;
								if(isCanBootUp){
									if(isServiceRunning){
										if(!PushMessageService.INSTANCE.mPopSocket.isSocketConnected() || !PushMessageService.INSTANCE.mPopSocket.isGpsUserLogined()){
											Log.e(TAG,"PushHeartKeepingBroadcastReceiver *****************启动PushMessageService***************");
											PushMessageService.INSTANCE.stopPushTask();
											PushMessageService.INSTANCE.startPushTask();
										}
									}else{
										context.startService(new Intent(context,PushMessageService.class));
									}
								}else{
									if(isServiceRunning && PushMessageService.INSTANCE.mPopSocket.isSocketConnected()){
										Log.e(TAG,"PushHeartKeepingBroadcastReceiver *****************关闭PushMessageService***************");
										PushMessageService.INSTANCE.stopPushTask();
									}
								}
							}
						}.start();
					}
				}
			}
		}

		
		
	}


}
