package cc.manbu.schoolinfocommunication.bean;


import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class MobileDevicAndLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3835606627561688979L;
	
	/**
	 *宿主账号
	 */
	public String hostAccount;
	// / <summary>
	// / 设备图标
	// / </summary>
	private String DeviceIcon;
	// / <summary>
	// / 设备序列号
	// / </summary>
	private String Serialnumber;

	// / <summary>
	// / 设备名称
	// / </summary>
	private String DeviecName;
	// / <summary>
	// / 设备地址
	// / </summary>
	private String Address;
	// / <summary>
	// / 设备定位时间
	// / </summary>
	private String GpsTime;

	// / <summary>
	// / 最后激活时间
	// / </summary>
	private String LastAcitivtyTime;

	// / <summary>
	// / 经过纠偏处理之后的纬度
	// / </summary>
	private Double OffsetLat;
	// / <summary>
	// / 经过纠偏处理之后的经度
	// / </summary>
	private Double OffsetLng;
	// / <summary>
	// / 设备的运行速度
	// / </summary>
	private Double Speed;
	// / <summary>
	// / 设备的运行角度
	// / </summary>
	private Double Course;
	// / <summary>
	// / 设备的状态
	// / </summary>
	private String AlarmStr;
	// / <summary>
	// / 电量
	// / </summary>
	private short Electricity;
	// / <summary>
	// / 设备的信号量
	// / </summary>
	private short Signal;
	//红外人体感应开关
	private boolean Infrared_State;
	private int DeviceOnLineState;
	private String OutTime;
	// 设备类型ID
	private int DeviceTypeID;

	private String TelPhoneNum;

	private int DeviceState;
	
	private int Lty;
	
	

	public int getLty() {
		return Lty;
	}

	public void setLty(int lty) {
		Lty = lty;
	}

	public String getOutTime() {
		return OutTime;
	}

	public void setOutTime(String outTime) {
		OutTime = outTime;
	}

	public int getDeviceOnLineState() {
		return DeviceOnLineState;
	}

	public void setDeviceOnLineState(int deviceOnLineState) {
		DeviceOnLineState = deviceOnLineState;
	}

	public int getDeviceState() {
		return DeviceState;
	}

	public void setDeviceState(int deviceState) {
		DeviceState = deviceState;
	}

	public String getTelPhoneNum() {
		return TelPhoneNum;
	}

	public void setTelPhoneNum(String telPhoneNum) {
		TelPhoneNum = telPhoneNum;
	}

	public String getDeviceIcon() {
		return DeviceIcon;
	}

	public void setDeviceIcon(String DeviceIcon) {
		this.DeviceIcon = DeviceIcon;
	}

	public String getSerialnumber() {
		return Serialnumber;
	}

	public void setSerialnumber(String Serialnumber) {
		this.Serialnumber = Serialnumber;
	}

	public String getDeviecName() {
		return DeviecName;
	}

	public void setDeviecName(String DeviecName) {
		this.DeviecName = DeviecName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getGpsTime() {
		return GpsTime;
	}

	public void setGpsTime(String GpsTime) {
		this.GpsTime = GpsTime;
	}

	public String getLastAcitivtyTime() {
		return LastAcitivtyTime;
	}

	public void setLastAcitivtyTime(String LastAcitivtyTime) {
		this.LastAcitivtyTime = LastAcitivtyTime;
	}

	public Double getOffsetLat() {
		return OffsetLat;
	}

	public void setOffsetLat(Double OffsetLat) {
		this.OffsetLat = OffsetLat;
	}

	public Double getOffsetLng() {
		return OffsetLng;
	}

	public void setOffsetLng(Double OffsetLng) {
		this.OffsetLng = OffsetLng;
	}

	public Double getSpeed() {
		return Speed;
	}

	public void setSpeed(Double Speed) {
		this.Speed = Speed;
	}

	public Double getCourse() {
		return Course;
	}

	public void setCourse(Double Course) {
		this.Course = Course;
	}

	public String getAlarmStr() {
		return AlarmStr;
	}

	public void setAlarmStr(String AlarmStr) {
		this.AlarmStr = AlarmStr;
	}

	public short getElectricity() {
		return Electricity;
	}

	public void setElectricity(short Electricity) {
		this.Electricity = Electricity;
	}

	public short getSignal() {
		return Signal;
	}

	public void setSignal(short Signal) {
		this.Signal = Signal;
	}

	public int getDeviceTypeID() {
		return DeviceTypeID;
	}

	public void setDeviceTypeID(int deviceTypeID) {
		DeviceTypeID = deviceTypeID;
	}

	public String getHostAccount() {
		return hostAccount;
	}

	public void setHostAccount(String hostAccount) {
		this.hostAccount = hostAccount;
	}

	public boolean getInfrared_State() {
		return Infrared_State;
	}

	public void setInfrared_State(boolean infrared_State) {
		this.Infrared_State = infrared_State;
	}

	
}
