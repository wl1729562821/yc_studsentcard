package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;

public class Device_GeographySearchOpt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Serialnumber;
	public String GeofenceName;

	public int PageIndex;

	public int PageSize;

	public String getSerialnumber() {
		return Serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		Serialnumber = serialnumber;
	}

	public String getGeofenceName() {
		return GeofenceName;
	}

	public void setGeofenceName(String geofenceName) {
		GeofenceName = geofenceName;
	}

	public int getPageIndex() {
		return PageIndex;
	}

	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}

	public int getPageSize() {
		return PageSize;
	}

	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}

	@Override
	public String toString() {
		return JSONHelper.toJSON(this);
	}
	
}
