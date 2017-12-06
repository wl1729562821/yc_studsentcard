package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.Date;

public class SHX520Device_Config implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7995208141835333238L;
	private boolean IsFang;
    private boolean IsBattery;
    private boolean GPSState;
    private String Ip;
    private Date EnterCampusTime;
    private Date LeaveCampusTime;
    private String ButtonNo1 = "";
    private String ButtonNo2 = "";
    private String ButtonNo3 = "";
    private String SOSNo = "";
    private String ListenNo = "";
    private String TimeZone;
    private String Interval;
    private String SendSMSTelNo;
    private String LastSendVoiceDt;
    private int WorkMode;
    private String Fanghuoqiang;
    private String FRIDState;
    private String CowNoti;
    private String Version;
    private int TargetStepCount;
    private int FinishStepCount;
    private long CumulativeStepCount;
    private String BluetoothState;
    private String APN;
    private int Perdometer;
    private int Temperature;
    private boolean IsPerdometerFinish;
    private String Reward;
    private boolean IsPerdometerRunning;
    private String ParentChildInteractionId;
    private String PowerOnTime;
    private String PowerOffTime;
    private int PowerState;
	private int KeyLockState;


	public int getKeyLockState() {
		return KeyLockState;
	}

	public void setKeyLockState(int keyLockState) {
		KeyLockState = keyLockState;
	}

	public String getPowerOnTime() {
		return PowerOnTime;
	}
	public void setPowerOnTime(String powerOnTime) {
		PowerOnTime = powerOnTime;
	}
	public String getPowerOffTime() {
		return PowerOffTime;
	}
	public void setPowerOffTime(String powerOffTime) {
		PowerOffTime = powerOffTime;
	}
	public int getPowerState() {
		return PowerState;
	}
	public void setPowerState(int powerState) {
		PowerState = powerState;
	}
	public boolean getIsFang() {
		return IsFang;
	}
	public void setIsFang(boolean isFang) {
		IsFang = isFang;
	}
	public boolean getIsBattery() {
		return IsBattery;
	}
	public void setIsBattery(boolean isBattery) {
		IsBattery = isBattery;
	}
	public boolean isGPSState() {
		return GPSState;
	}
	public void setGPSState(boolean gPSState) {
		GPSState = gPSState;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public Date getEnterCampusTime() {
		return EnterCampusTime;
	}
	public void setEnterCampusTime(Date enterCampusTime) {
		EnterCampusTime = enterCampusTime;
	}
	public Date getLeaveCampusTime() {
		return LeaveCampusTime;
	}
	public void setLeaveCampusTime(Date leaveCampusTime) {
		LeaveCampusTime = leaveCampusTime;
	}
	public String getButtonNo1() {
		return ButtonNo1;
	}
	public void setButtonNo1(String buttonNo1) {
		ButtonNo1 = buttonNo1;
	}
	public String getButtonNo2() {
		return ButtonNo2;
	}
	public void setButtonNo2(String buttonNo2) {
		ButtonNo2 = buttonNo2;
	}
	public String getButtonNo3() {
		return ButtonNo3;
	}
	public void setButtonNo3(String buttonNo3) {
		ButtonNo3 = buttonNo3;
	}
	public String getSOSNo() {
		return SOSNo;
	}
	public void setSOSNo(String sOSNo) {
		SOSNo = sOSNo;
	}
	public String getListenNo() {
		return ListenNo;
	}
	public void setListenNo(String listenNo) {
		ListenNo = listenNo;
	}
	public String getTimeZone() {
		return TimeZone;
	}
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	public String getInterval() {
		return Interval;
	}
	public void setInterval(String interval) {
		Interval = interval;
	}
	public String getSendSMSTelNo() {
		return SendSMSTelNo;
	}
	public void setSendSMSTelNo(String sendSMSTelNo) {
		SendSMSTelNo = sendSMSTelNo;
	}
	public String getLastSendVoiceDt() {
		return LastSendVoiceDt;
	}
	public void setLastSendVoiceDt(String lastSendVoiceDt) {
		LastSendVoiceDt = lastSendVoiceDt;
	}
	public int getWorkMode() {
		return WorkMode;
	}
	public void setWorkMode(int workMode) {
		WorkMode = workMode;
	}
	public String getFanghuoqiang() {
		return Fanghuoqiang;
	}
	public void setFanghuoqiang(String fanghuoqiang) {
		Fanghuoqiang = fanghuoqiang;
	}
	public String getFRIDState() {
		return FRIDState;
	}
	public void setFRIDState(String fRIDState) {
		FRIDState = fRIDState;
	}
	public String getCowNoti() {
		return CowNoti;
	}
	public void setCowNoti(String cowNoti) {
		CowNoti = cowNoti;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public int getTargetStepCount() {
		return TargetStepCount;
	}
	public void setTargetStepCount(int targetStepCount) {
		TargetStepCount = targetStepCount;
	}
	public int getFinishStepCount() {
		return FinishStepCount;
	}
	public void setFinishStepCount(int finishStepCount) {
		FinishStepCount = finishStepCount;
	}
	public long getCumulativeStepCount() {
		return CumulativeStepCount;
	}
	public void setCumulativeStepCount(long cumulativeStepCount) {
		CumulativeStepCount = cumulativeStepCount;
	}
	public String getBluetoothState() {
		return BluetoothState;
	}
	public void setBluetoothState(String bluetoothState) {
		BluetoothState = bluetoothState;
	}
	public String getAPN() {
		return APN;
	}
	public void setAPN(String aPN) {
		APN = aPN;
	}
	public int getPerdometer() {
		return Perdometer;
	}
	public void setPerdometer(int perdometer) {
		Perdometer = perdometer;
	}
	public int getTemperature() {
		return Temperature;
	}
	public void setTemperature(int temperature) {
		Temperature = temperature;
	}
	public boolean getIsPerdometerFinish() {
		return IsPerdometerFinish;
	}
	public void setIsPerdometerFinish(boolean isPerdometerFinish) {
		IsPerdometerFinish = isPerdometerFinish;
	}
	public String getReward() {
		return Reward;
	}
	public void setReward(String reward) {
		Reward = reward;
	}
	public boolean getIsPerdometerRunning() {
		return IsPerdometerRunning;
	}
	public void setIsPerdometerRunning(boolean isPerdometerRunning) {
		IsPerdometerRunning = isPerdometerRunning;
	}
	public String getParentChildInteractionId() {
		return ParentChildInteractionId;
	}
	public void setParentChildInteractionId(String parentChildInteractionId) {
		ParentChildInteractionId = parentChildInteractionId;
	}
   
    
}
