package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.Date;

public class LastLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1135162543657804315L;
	private String SerialNumber;
	private int LocationType;
	private Date LastActivityTime;
	private Date GpsTime;

	private double Lat;
	private double Lng;
	private double OffsetLat;
	private double OffsetLng;
	private double Blat;
	private double BLng;

	private double Speed;
	private double Source;
	private int Electricity;

	private int Signal;
	private int State;
	private int AlarmType;
	private double Altitude;
	private double HDOP;
	private double Mileage;

	public String getSerialNumber() {
		return SerialNumber;
	}

	public int getLocationType() {
		return LocationType;
	}

	public Date getLastActivityTime() {
		return LastActivityTime;
	}

	public Date getGpsTime() {
		return GpsTime;
	}

	public double getOffsetLat() {
		return OffsetLat;
	}

	public double getOffsetLng() {
		return OffsetLng;
	}

	public double getBlat() {
		return Blat;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLng() {
		return Lng;
	}

	public void setLng(double lng) {
		Lng = lng;
	}

	public double getSpeed() {
		return Speed;
	}

	public void setSpeed(double speed) {
		Speed = speed;
	}

	public double getSource() {
		return Source;
	}

	public void setSource(double source) {
		Source = source;
	}

	public double getAltitude() {
		return Altitude;
	}

	public void setAltitude(double altitude) {
		Altitude = altitude;
	}

	public double getHDOP() {
		return HDOP;
	}

	public void setHDOP(double hDOP) {
		HDOP = hDOP;
	}

	public double getMileage() {
		return Mileage;
	}

	public void setMileage(double mileage) {
		Mileage = mileage;
	}

	public double getBLng() {
		return BLng;
	}

	public int getElectricity() {
		return Electricity;
	}

	public void setElectricity(int electricity) {
		Electricity = electricity;
	}

	public int getSignal() {
		return Signal;
	}

	public void setSignal(int signal) {
		Signal = signal;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public int getAlarmType() {
		return AlarmType;
	}

	public void setAlarmType(int alarmType) {
		AlarmType = alarmType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}

	public void setLocationType(int locationType) {
		LocationType = locationType;
	}

	public void setLastActivityTime(Date lastActivityTime) {
		LastActivityTime = lastActivityTime;
	}

	public void setGpsTime(Date gpsTime) {
		GpsTime = gpsTime;
	}

	public void setOffsetLat(double offsetLat) {
		OffsetLat = offsetLat;
	}

	public void setOffsetLng(double offsetLng) {
		OffsetLng = offsetLng;
	}

	public void setBlat(double blat) {
		Blat = blat;
	}

	public void setBLng(double bLng) {
		BLng = bLng;
	}

	public void setElectricity(short electricity) {
		Electricity = electricity;
	}

	public void setSignal(short signal) {
		Signal = signal;
	}

	public void setState(short state) {
		State = state;
	}

	public void setAlarmType(short alarmType) {
		AlarmType = alarmType;
	}

	@Override
	public String toString() {
		return "LastLocation [SerialNumber=" + SerialNumber + ", LocationType="
				+ LocationType + ", LastActivityTime=" + LastActivityTime
				+ ", GpsTime=" + GpsTime + ", Lat=" + Lat + ", Lng=" + Lng
				+ ", OffsetLat=" + OffsetLat + ", OffsetLng=" + OffsetLng
				+ ", Blat=" + Blat + ", BLng=" + BLng + ", Speed=" + Speed
				+ ", Source=" + Source + ", Electricity=" + Electricity
				+ ", Signal=" + Signal + ", State=" + State + ", AlarmType="
				+ AlarmType + ", Altitude=" + Altitude + ", HDOP=" + HDOP
				+ ", Mileage=" + Mileage + "]";
	}
	
	
}
