package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfHomeWorkResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;


/**
 * 家庭作业
 *
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfHomeWorkResonseParser.class)
public class Hw_Homework extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -2606923165071320179L;

	// 学科id
	public int R_SubjectId;

	// 学科
	public R_Subject R_Subject;

	public String SubjectName;

	public String DeptName;

	public int R_DepartmentId;

	// 班级
	public R_Department R_Department;

	// 作业标题
	public String Title;

	// 作业内容
	public String Context;



	public String getSubjectName() {
		return SubjectName;
	}

	public void setSubjectName(String subjectName) {
		SubjectName = subjectName;
	}

	public String getDeptName() {
		return DeptName;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
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

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}

	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}

}
