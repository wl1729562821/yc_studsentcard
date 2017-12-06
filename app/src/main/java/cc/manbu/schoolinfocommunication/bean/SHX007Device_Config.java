package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


public class SHX007Device_Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Serialnumber;
	private String SendSMSTelNo;
	private String KeySOSCallNo;
	private String KeySOSSMSNo;
	private String Key1CallNo;
	private String Key1SMSNo;
	private String Key2CallNo;
	private String Key2SMSNo;
	private String Key3CallNo;
	private String Key3SMSNo;
	private String KeyAlarmSOS;
	private String KeyAlarm1;
	private String KeyAlarm2;
	private String KeyAlarm3;
	private String OverSpeed;
	private String MoveAlarm;
	private String intervalTime;
	private String ListenNum;
	private String TimeZone;
	private String Ans_Model;
	private boolean GPSState;

	private String Fanghuoqiang;
	private boolean IsClassTimeOpen;
	public boolean getGPSState() {
		return GPSState;
	}

	public void setGPSState(boolean gPSState) {
		GPSState = gPSState;
	}

	public String getFanghuoqiang() {
		return Fanghuoqiang;
	}

	public void setFanghuoqiang(String fanghuoqiang) {
		Fanghuoqiang = fanghuoqiang;
	}

	public String getSerialnumber() {
		return Serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		Serialnumber = serialnumber;
	}

	public String getSendSMSTelNo() {
		return SendSMSTelNo;
	}

	public void setSendSMSTelNo(String sendSMSTelNo) {
		SendSMSTelNo = sendSMSTelNo;
	}

	public String getKeySOSCallNo() {
		return KeySOSCallNo;
	}

	public void setKeySOSCallNo(String keySOSCallNo) {
		KeySOSCallNo = keySOSCallNo;
	}

	public String getKeySOSSMSNo() {
		return KeySOSSMSNo;
	}

	public void setKeySOSSMSNo(String keySOSSMSNo) {
		KeySOSSMSNo = keySOSSMSNo;
	}

	public String getKey1CallNo() {
		return Key1CallNo;
	}

	public void setKey1CallNo(String key1CallNo) {
		Key1CallNo = key1CallNo;
	}

	public String getKey1SMSNo() {
		return Key1SMSNo;
	}

	public void setKey1SMSNo(String key1smsNo) {
		Key1SMSNo = key1smsNo;
	}

	public String getKey2CallNo() {
		return Key2CallNo;
	}

	public void setKey2CallNo(String key2CallNo) {
		Key2CallNo = key2CallNo;
	}

	public String getKey2SMSNo() {
		return Key2SMSNo;
	}

	public void setKey2SMSNo(String key2smsNo) {
		Key2SMSNo = key2smsNo;
	}

	public String getKey3CallNo() {
		return Key3CallNo;
	}

	public void setKey3CallNo(String key3CallNo) {
		Key3CallNo = key3CallNo;
	}

	public String getKey3SMSNo() {
		return Key3SMSNo;
	}

	public void setKey3SMSNo(String key3smsNo) {
		Key3SMSNo = key3smsNo;
	}

	public String getKeyAlarmSOS() {
		return KeyAlarmSOS;
	}

	public void setKeyAlarmSOS(String keyAlarmSOS) {
		KeyAlarmSOS = keyAlarmSOS;
	}

	public String getKeyAlarm1() {
		return KeyAlarm1;
	}

	public void setKeyAlarm1(String keyAlarm1) {
		KeyAlarm1 = keyAlarm1;
	}

	public String getKeyAlarm2() {
		return KeyAlarm2;
	}

	public void setKeyAlarm2(String keyAlarm2) {
		KeyAlarm2 = keyAlarm2;
	}

	public String getKeyAlarm3() {
		return KeyAlarm3;
	}

	public void setKeyAlarm3(String keyAlarm3) {
		KeyAlarm3 = keyAlarm3;
	}

	public String getOverSpeed() {
		return OverSpeed;
	}

	public void setOverSpeed(String overSpeed) {
		OverSpeed = overSpeed;
	}

	public String getMoveAlarm() {
		return MoveAlarm;
	}

	public void setMoveAlarm(String moveAlarm) {
		MoveAlarm = moveAlarm;
	}

	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getListenNum() {
		return ListenNum;
	}

	public void setListenNum(String listenNum) {
		ListenNum = listenNum;
	}

	public String getTimeZone() {
		return TimeZone;
	}

	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}

	public String getAns_Model() {
		return Ans_Model;
	}

	public void setAns_Model(String ans_Model) {
		Ans_Model = ans_Model;
	}

	@Override
	public String toString() {
		return JSONHelper.toJSON(this);
	}

	public boolean getIsClassTimeOpen() {
		return IsClassTimeOpen;
	}

	public void setIsClassTimeOpen(boolean isClassTimeOpen) {
		IsClassTimeOpen = isClassTimeOpen;
	}
}
