package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfSubjectResonseParser;

/**
 * 科目
 *
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfSubjectResonseParser.class)
public class R_Subject extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -5778945217420887666L;
	// 科目名称
	public String Name;
	// 相关班级
	public int R_DepartmentId;
	// XmlIgnore
	// ScriptIgnoreAttribute
	// 相关班级
	public R_Department R_Department;
	// 执教教师
	public int R_TeacherId;
	// ScriptIgnoreAttribute
	// 执教教师
	public R_Teacher R_Teacher;
	public List<SR_Exam> SR_Exam;
	// System.ComponentModel.DataAnnotations.Schema.NotMapped
	public String DepName;
	// System.ComponentModel.DataAnnotations.Schema.NotMapped
	public String R_TeacherName;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public int getR_TeacherId() {
		return R_TeacherId;
	}

	public void setR_TeacherId(int r_TeacherId) {
		R_TeacherId = r_TeacherId;
	}

	public R_Teacher getR_Teacher() {
		return R_Teacher;
	}

	public void setR_Teacher(R_Teacher r_Teacher) {
		R_Teacher = r_Teacher;
	}

	public List<SR_Exam> getSR_Exam() {
		return SR_Exam;
	}

	public void setSR_Exam(List<SR_Exam> sR_Exam) {
		SR_Exam = sR_Exam;
	}

	public String getDepName() {
		if (R_Department == null) {
			return "";
		}
		return R_Department.getDepName();
	}

	public void setDepName(String depName) {
		DepName = depName;
	}

	public String getR_TeacherName() {
		if (R_Teacher != null && R_Teacher.getR_Users() != null) {
			return R_Teacher.getR_Users().getUserName();
		}
		return "";
	}

	public void setR_TeacherName(String r_TeacherName) {
		R_TeacherName = r_TeacherName;
	}

	@Override
	public String toString() {
		return "R_Subject [Name=" + Name + ", R_DepartmentId=" + R_DepartmentId
				+ ", R_Department=" + R_Department + ", R_TeacherId="
				+ R_TeacherId + ", R_Teacher=" + R_Teacher + ", SR_Exam="
				+ SR_Exam + ", DepName=" + DepName + ", R_TeacherName="
				+ R_TeacherName + "]";
	}


}
