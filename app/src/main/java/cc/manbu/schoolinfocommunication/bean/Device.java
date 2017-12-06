package cc.manbu.schoolinfocommunication.bean;


import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1048554353073865111L;
	private String SerialNumber;
	private String DeviceName;
	private String DeviceTypeStr;
	private String UseLoginNamer;
	private int DeviceState = -1;
	private String DeviceIcon;
	private boolean privateLocation;
	private String Path;
	private int DeviceTypeID;
	private int GeofenceCount;
	private boolean FilterLbs;
	private String DeviceImage;
	private String TelPhoneNum;
	private String SOSPhoneNum;
	//
	private Date CreateTime;
	private Date OutDatetime;
	private Date ActivityTime;
	private String DescContet;
	private MobileCart MobileCart;
	private LastLocation LastLocation;
	private SHX007Device_Config SHX007Device_Config;
	private SHX520Device_Config SHX520Device_Config;
	private int _DBIndex = -1;

	// / <summary>
	// / 电子栅栏有效距离 单位(米) 如果为0 那么就表示撤防 否则就表示设防
	// / </summary>
	private int GF_Width;

	private double GF_Lat;
	private double GF_Lng;

	private double GF_B_Lat;
	private double GF_B_Lng;

	private double GF_Offset_Lat;
	private double GF_Offset_Lng;
	
	public int getGF_Width() {
		return GF_Width;
	}

	public void setGF_Width(int gF_Width) {
		GF_Width = gF_Width;
	}

	public double getGF_Lat() {
		return GF_Lat;
	}

	public void setGF_Lat(double gF_Lat) {
		GF_Lat = gF_Lat;
	}

	public double getGF_Lng() {
		return GF_Lng;
	}

	public void setGF_Lng(double gF_Lng) {
		GF_Lng = gF_Lng;
	}

	public double getGF_B_Lat() {
		return GF_B_Lat;
	}

	public String getDescContet() {
		return DescContet;
	}

	public void setDescContet(String descContet) {
		DescContet = descContet;
	}

	public int get_DBIndex() {
		return _DBIndex;
	}

	public void setGF_B_Lat(double gF_B_Lat) {
		GF_B_Lat = gF_B_Lat;
	}

	public double getGF_B_Lng() {
		return GF_B_Lng;
	}

	public void setGF_B_Lng(double gF_B_Lng) {
		GF_B_Lng = gF_B_Lng;
	}

	public double getGF_Offset_Lat() {
		return GF_Offset_Lat;
	}

	public void setGF_Offset_Lat(double gF_Offset_Lat) {
		GF_Offset_Lat = gF_Offset_Lat;
	}

	public double getGF_Offset_Lng() {
		return GF_Offset_Lng;
	}

	public void setGF_Offset_Lng(double gF_Offset_Lng) {
		GF_Offset_Lng = gF_Offset_Lng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSerialNumber() {
		return SerialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getDeviceTypeStr() {
		return DeviceTypeStr;
	}

	public void setDeviceTypeStr(String deviceTypeStr) {
		DeviceTypeStr = deviceTypeStr;
	}

	public String getUseLoginNamer() {
		return UseLoginNamer;
	}

	public void setUseLoginNamer(String useLoginNamer) {
		UseLoginNamer = useLoginNamer;
	}

	public int getDeviceState() {
		return DeviceState;
	}

	public void setDeviceState(int deviceState) {
		DeviceState = deviceState;
	}

	public String getDeviceIcon() {
		return DeviceIcon;
	}

	public void setDeviceIcon(String deviceIcon) {
		DeviceIcon = deviceIcon;
	}

	public boolean isPrivateLocation() {
		return privateLocation;
	}

	public void setPrivateLocation(boolean privateLocation) {
		this.privateLocation = privateLocation;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	public int getDeviceTypeID() {
		return DeviceTypeID;
	}

	public void setDeviceTypeID(int deviceTypeID) {
		DeviceTypeID = deviceTypeID;
	}

	public int getGeofenceCount() {
		return GeofenceCount;
	}

	public void setGeofenceCount(int geofenceCount) {
		GeofenceCount = geofenceCount;
	}

	public boolean isFilterLbs() {
		return FilterLbs;
	}

	public void setFilterLbs(boolean filterLbs) {
		FilterLbs = filterLbs;
	}

	public String getDeviceImage() {
		return DeviceImage;
	}

	public void setDeviceImage(String deviceImage) {
		DeviceImage = deviceImage;
	}

	public String getTelPhoneNum() {
		return TelPhoneNum;
	}

	public void setTelPhoneNum(String telPhoneNum) {
		TelPhoneNum = telPhoneNum;
	}

	public String getSOSPhoneNum() {
		return SOSPhoneNum;
	}

	public void setSOSPhoneNum(String sOSPhoneNum) {
		SOSPhoneNum = sOSPhoneNum;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public Date getOutDatetime() {
		return OutDatetime;
	}

	public void setOutDatetime(Date outDatetime) {
		OutDatetime = outDatetime;
	}

	public Date getActivityTime() {
		return ActivityTime;
	}

	public void setActivityTime(Date activityTime) {
		ActivityTime = activityTime;
	}

	public MobileCart getMobileCart() {
		return MobileCart;
	}

	public void setMobileCart(MobileCart mobileCart) {
		MobileCart = mobileCart;
	}

	public LastLocation getLastLocation() {
		return LastLocation;
	}

	public void setLastLocation(LastLocation lastLocation) {
		LastLocation = lastLocation;
	}

	public SHX007Device_Config getSHX007Device_Config() {
		return SHX007Device_Config;
	}

	public void setSHX007Device_Config(SHX007Device_Config sHX007Device_Config) {
		SHX007Device_Config = sHX007Device_Config;
	}

	public void set_DBIndex(int _DBIndex) {
		this._DBIndex = _DBIndex;
	}

	
	public SHX520Device_Config getSHX520Device_Config() {
		return SHX520Device_Config;
	}

	public void setSHX520Device_Config(SHX520Device_Config sHX520Device_Config) {
		SHX520Device_Config = sHX520Device_Config;
	}

	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}
	
}
