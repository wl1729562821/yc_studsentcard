package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.List;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;

public class SHX520SearchCOW implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 506602884136469378L;
	private String Serialnumber;
	private int PageSize;
	private int PageIndex;
	public List<String> SerialnumberList ;
	private java.util.Date StartDate;
	private java.util.Date EndDate;
	private int DeptId;
	private int Month;
	private java.util.Date Date;
	public String getSerialnumber() {
		return Serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		Serialnumber = serialnumber;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}
	public List<String> getSerialnumberList() {
		return SerialnumberList;
	}
	public void setSerialnumberList(List<String> serialnumberList) {
		SerialnumberList = serialnumberList;
	}
	public java.util.Date getStartDate() {
		return StartDate;
	}
	public void setStartDate(java.util.Date startDate) {
		StartDate = startDate;
	}
	public java.util.Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}
	public int getDeptId() {
		return DeptId;
	}
	public void setDeptId(int deptId) {
		DeptId = deptId;
	}
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public class SerialnumberList{
		String string;

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}
		
	}
	
	public String toString(){
		return JSONHelper.toJSON(this);
	}
}
