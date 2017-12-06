package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

import cc.manbu.schoolinfocommunication.tools.JSONHelper;


public class Curriculum extends SC_SchoolPar implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5159326966428073027L;

	// 周几
	private int WeekCode;

	private int WorkandrestId;
	private Workandrest Workandrest;

	private int R_DepartmentId;
	private R_Department R_Department;

	// 科目
	private int R_SubjectId;

	private R_Subject R_Subject;
	private String WorkandrestName;

	private String SubjectName;
	private String StartTime;
	private String EndTime;

	private String TeacherName;
	//班级名称
	private String DepName;



	public String getTeacherName() {
		return TeacherName;
	}

	public void setTeacherName(String teacherName) {
		TeacherName = teacherName;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getWorkandrestName() {
		return WorkandrestName;
	}

	public String getDepName() {
		if (R_Department == null) {
			return DepName;
		} else {
			return R_Department.getDepName()!=null?R_Department.getDepName():DepName!=null?DepName:"";
		}
	}

	public String getSubjectName() {
		return SubjectName;
	}

	public int getWeekCode() {
		return WeekCode;
	}

	public void setWeekCode(int weekCode) {
		WeekCode = weekCode;
	}

	public int getWorkandrestId() {
		return WorkandrestId;
	}

	public void setWorkandrestId(int workandrestId) {
		WorkandrestId = workandrestId;
	}

	public Workandrest getWorkandrest() {
		return Workandrest;
	}

	public void setWorkandrest(Workandrest workandrest) {
		Workandrest = workandrest;
	}

	public int getR_DepartmentId() {
		return R_DepartmentId;
	}

	public void setR_DepartmentId(int r_DepartmentId) {
		R_DepartmentId = r_DepartmentId;
	}

	public R_Department getR_Department() {
		return R_Department;
	}

	public void setR_Department(R_Department r_Department) {
		R_Department = r_Department;
	}

	public int getR_SubjectId() {
		return R_SubjectId;
	}

	public void setR_SubjectId(int r_SubjectId) {
		R_SubjectId = r_SubjectId;
	}

	public R_Subject getR_Subject() {
		return R_Subject;
	}

	public void setR_Subject(R_Subject r_Subject) {
		R_Subject = r_Subject;
	}

	public void setWorkandrestName(String workandrestName) {
		WorkandrestName = workandrestName;
	}

	public void setDepName(String depName) {
		DepName = depName;
	}

	public void setSubjectName(String subjectName) {
		SubjectName = subjectName;
	}

	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}


}
