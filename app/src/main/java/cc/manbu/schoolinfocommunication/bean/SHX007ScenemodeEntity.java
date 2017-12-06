package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

public class SHX007ScenemodeEntity implements Serializable {
	/// <summary>
	/// 来电声音级别
	/// </summary>
	public int CV;

	/// <summary>
	/// 短信声音级别
	/// </summary>
	public int SV;
	/// <summary>
	/// 语音播报声音级别
	/// </summary>
	public int PV;
	public int getCV() {
		return CV;
	}
	public void setCV(int cV) {
		CV = cV;
	}
	public int getSV() {
		return SV;
	}
	public void setSV(int sV) {
		SV = sV;
	}
	public int getPV() {
		return PV;
	}
	public void setPV(int pV) {
		PV = pV;
	}



}
