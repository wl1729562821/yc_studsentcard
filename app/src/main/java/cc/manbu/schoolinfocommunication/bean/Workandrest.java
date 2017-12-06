package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 作息时间
 * @author gongyong2014
 *
 */
public class Workandrest extends SC_SchoolPar implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4368369673780061954L;
	public String Name;
	//开始时间
	public Date StartTime;
	//接收
	public Date StopTime;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getStartTime() {
		return StartTime;
	}
	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}
	public Date getStopTime() {
		return StopTime;
	}
	public void setStopTime(Date stopTime) {
		StopTime = stopTime;
	}

}
