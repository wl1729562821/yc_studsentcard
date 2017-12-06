package cc.manbu.schoolinfocommunication.bean;


import java.io.Serializable;
import java.util.Date;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


public class MobileCart implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	// / <summary>
	// / 电话号码
	// / </summary>
	private String TelNo;
	// / <summary>
	// / 当前余额
	// / </summary>
	private double Money;
	// / <summary>
	// / 出账时间
	// / </summary>
	private Date LastUpdateTime;
	//号码主人
	private String Name;

	public String getTelNo() {
		return TelNo;
	}

	public void setTelNo(String telNo) {
		TelNo = telNo;
	}

	public double getMoney() {
		return Money;
	}

	public void setMoney(double money) {
		Money = money;
	}

	public Date getLastUpdateTime() {
		return LastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		LastUpdateTime = lastUpdateTime;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public String toString() {
		return JSONHelper.toJSON(this);
	}
	
	
	
}
