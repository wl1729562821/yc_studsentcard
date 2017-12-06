package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfDayOffResonseParser;

/**
 * 请假管理
 *
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfDayOffResonseParser.class)
public class Sleave extends SC_SchoolPar implements Serializable {

	/**
	 *
	 */
	public static final long serialVersionUID = -4217607696733746325L;

	public int R_StudentId;

	// 请假学生
	public R_Student R_Student;

	// 开始时间
	public Date StartTime;

	// 截至时间
	public Date EndTime;
	// 原因
	public String Reason;

	public String StuName;

	public String DeptName;

	public String title;

	public int State;

	public String StateText;

	public int getR_StudentId() {
		return R_StudentId;
	}

	public void setR_StudentId(int r_StudentId) {
		R_StudentId = r_StudentId;
	}


	public String getStuName() {
		return StuName;
	}

	public void setStuName(String stuName) {
		StuName = stuName;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public R_Student getR_Student() {
		return R_Student;
	}

	public void setR_Student(R_Student r_Student) {
		R_Student = r_Student;
	}
	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}

	public Date getEndTime() {
		return EndTime;
	}

	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public String getStateText() {
		StateText = "";
		switch (State)
		{
			case 0:
				StateText = "未审核";
				break;
			case 1:
				StateText = "未通过";
				break;
			case 2:
				StateText = "通过";
				break;
		}
		return StateText;
	}

	public void setStateText(String stateText) {
		StateText = stateText;
	}


	public static class PageSHelp implements Serializable
	{

		public int PageSize;
		public int PageIndex;

		public int PageCount;

		public int DeptId;


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

		public int getPageCount() {
			return PageCount;
		}

		public void setPageCount(int pageCount) {
			PageCount = pageCount;
		}

		public int getDeptId() {
			return DeptId;
		}

		public void setDeptId(int deptId) {
			DeptId = deptId;
		}

//		public int getTotal() {
//			toolbar_back Total;
//		}
//
//		public void setTotal(int total) {
//			Total = total;
//		}


	}
}
