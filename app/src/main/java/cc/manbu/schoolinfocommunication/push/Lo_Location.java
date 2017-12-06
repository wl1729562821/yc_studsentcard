package cc.manbu.schoolinfocommunication.push;

import java.io.Serializable;
import java.util.Date;

public class Lo_Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5422185044022047782L;
	
	public String SerialNumber;
	public int LocationType;
	public Date GpsTime;
	public float Lat;
	public float Lng;
	public double OffsetLat;
	public double OffsetLng;
	public double Blat;
	public double BLng;
	public float Speed;
	public float Source;
	public int Electricity;
	public short Signal;
	public short State;
	public int AlarmType;
	public float Altitude;
	public float HDOP;
	public float Mileage;
	public int Lac;
	public int Cid;
	public Date LastActivityTime;
	public String C_LastIp;
	public String C_LastSIp;
	public boolean C_Online;
	public Date C_AccpTime;
	public Date C_LastSendTime;
	public long C_SendDataCount;
	public int C_RevDataCount;
	
	
	
	public String getSerialNumber() {
		return SerialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}
	public int getLocationType() {
		return LocationType;
	}
	public void setLocationType(int locationType) {
		LocationType = locationType;
	}
	public Date getGpsTime() {
		return GpsTime;
	}
	public void setGpsTime(Date gpsTime) {
		GpsTime = gpsTime;
	}
	public float getLat() {
		return Lat;
	}
	public void setLat(float lat) {
		Lat = lat;
	}
	public float getLng() {
		return Lng;
	}
	public void setLng(float lng) {
		Lng = lng;
	}
	public double getOffsetLat() {
		return OffsetLat;
	}
	public void setOffsetLat(double offsetLat) {
		OffsetLat = offsetLat;
	}
	public double getOffsetLng() {
		return OffsetLng;
	}
	public void setOffsetLng(double offsetLng) {
		OffsetLng = offsetLng;
	}
	public double getBlat() {
		return Blat;
	}
	public void setBlat(double blat) {
		Blat = blat;
	}
	public double getBLng() {
		return BLng;
	}
	public void setBLng(double bLng) {
		BLng = bLng;
	}
	public float getSpeed() {
		return Speed;
	}
	public void setSpeed(float speed) {
		Speed = speed;
	}
	public float getSource() {
		return Source;
	}
	public void setSource(float source) {
		Source = source;
	}
	public int getElectricity() {
		return Electricity;
	}
	public void setElectricity(int electricity) {
		Electricity = electricity;
	}
	public short getSignal() {
		return Signal;
	}
	public void setSignal(short signal) {
		Signal = signal;
	}
	public short getState() {
		return State;
	}
	public void setState(short state) {
		State = state;
	}
	public int getAlarmType() {
		return AlarmType;
	}
	public void setAlarmType(int alarmType) {
		AlarmType = alarmType;
	}
	public float getAltitude() {
		return Altitude;
	}
	public void setAltitude(float altitude) {
		Altitude = altitude;
	}
	public float getHDOP() {
		return HDOP;
	}
	public void setHDOP(float hDOP) {
		HDOP = hDOP;
	}
	public float getMileage() {
		return Mileage;
	}
	public void setMileage(float mileage) {
		Mileage = mileage;
	}
	public int getLac() {
		return Lac;
	}
	public void setLac(int lac) {
		Lac = lac;
	}
	public int getCid() {
		return Cid;
	}
	public void setCid(int cid) {
		Cid = cid;
	}
	public Date getLastActivityTime() {
		return LastActivityTime;
	}
	public void setLastActivityTime(Date lastActivityTime) {
		LastActivityTime = lastActivityTime;
	}
	public String getC_LastIp() {
		return C_LastIp;
	}
	public void setC_LastIp(String c_LastIp) {
		C_LastIp = c_LastIp;
	}
	public String getC_LastSIp() {
		return C_LastSIp;
	}
	public void setC_LastSIp(String c_LastSIp) {
		C_LastSIp = c_LastSIp;
	}
	public boolean isC_Online() {
		return C_Online;
	}
	public void setC_Online(boolean c_Online) {
		C_Online = c_Online;
	}
	public Date getC_AccpTime() {
		return C_AccpTime;
	}
	public void setC_AccpTime(Date c_AccpTime) {
		C_AccpTime = c_AccpTime;
	}
	public Date getC_LastSendTime() {
		return C_LastSendTime;
	}
	public void setC_LastSendTime(Date c_LastSendTime) {
		C_LastSendTime = c_LastSendTime;
	}
	public long getC_SendDataCount() {
		return C_SendDataCount;
	}
	public void setC_SendDataCount(long c_SendDataCount) {
		C_SendDataCount = c_SendDataCount;
	}
	public int getC_RevDataCount() {
		return C_RevDataCount;
	}
	public void setC_RevDataCount(int c_RevDataCount) {
		C_RevDataCount = c_RevDataCount;
	}
	
	
	
}
