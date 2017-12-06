package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfDepartmentResonseParser;

/**
 * 部门
 *
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfDepartmentResonseParser.class)
public class R_Department extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -4040218681989001685L;
	// 部门名称
	public String DepName;
	public int Par_R_DepartmentId;
	// XmlIgnore
	// ScriptIgnoreAttribute
	public R_Department Par_R_Department;
	public List<R_Department> CH_R_Department;
	public List<R_Subject> R_Subject;
	public List<SR_Exam> SR_Exam;

	public String getDepName() {
		return DepName;
	}

	public void setDepName(String depName) {
		DepName = depName;
	}

	public int getPar_R_DepartmentId() {
		return Par_R_DepartmentId;
	}

	public void setPar_R_DepartmentId(int par_R_DepartmentId) {
		Par_R_DepartmentId = par_R_DepartmentId;
	}

	public R_Department getPar_R_Department() {
		return Par_R_Department;
	}

	public void setPar_R_Department(R_Department par_R_Department) {
		Par_R_Department = par_R_Department;
	}

	public List<R_Department> getCH_R_Department() {
		return CH_R_Department;
	}

	public void setCH_R_Department(List<R_Department> cH_R_Department) {
		CH_R_Department = cH_R_Department;
	}

	public List<R_Subject> getR_Subject() {
		return R_Subject;
	}

	public void setR_Subject(List<R_Subject> r_Subject) {
		R_Subject = r_Subject;
	}

	public List<SR_Exam> getSR_Exam() {
		return SR_Exam;
	}

	public void setSR_Exam(List<SR_Exam> sR_Exam) {
		SR_Exam = sR_Exam;
	}

	@Override
	public String toString() {
		return "R_Department [DepName=" + DepName + ", Par_R_DepartmentId="
				+ Par_R_DepartmentId + ", Par_R_Department=" + Par_R_Department
				+ ", CH_R_Department=" + CH_R_Department + ", R_Subject="
				+ R_Subject + ", SR_Exam=" + SR_Exam + "]";
	}


}
