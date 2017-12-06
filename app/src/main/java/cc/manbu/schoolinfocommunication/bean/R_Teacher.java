package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.List;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


/**
 * 老师
 *
 * @author gongyong2014
 *
 */
public class R_Teacher extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -5959446026650734429L;
	// ForeignKey("R_Users")
	// XmlIgnore
	// ScriptIgnoreAttribute
	public R_Users R_Users;
	// XmlIgnore
	// ScriptIgnoreAttribute
	public List<R_Subject> R_Subject;
	// System.ComponentModel.DataAnnotations.Schema.NotMapped
	public String TeacherName;





	public R_Users getR_Users() {
		return R_Users;
	}

	public void setR_Users(R_Users r_Users) {
		R_Users = r_Users;
	}

	public List<R_Subject> getR_Subject() {
		return R_Subject;
	}

	public void setR_Subject(List<R_Subject> r_Subject) {
		R_Subject = r_Subject;
	}

	public String getTeacherName() {
		if (R_Users != null) {
			return R_Users.getUserName();
		}
		return "";
	}

	public void setTeacherName(String teacherName) {
		TeacherName = teacherName;
	}

	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}


}
