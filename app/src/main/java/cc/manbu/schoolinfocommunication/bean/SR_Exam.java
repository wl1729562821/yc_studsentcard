package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfExamResonseParser;

/**
 * 考试管理
 * @author gongyong2014
 *
 */
@HttpResponse(parser = JsonCollectionsOfExamResonseParser.class)
public class SR_Exam extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = 4083940403225557618L;
	//考试名称
	public String Name;
	//考试时间
	public Date ExamTime;

	//考场地址
	public String Address;

	//参与班级
	public R_Department R_Department;

	public List<R_Subject> R_Subject;




	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}



	public Date getExamTime() {
		return ExamTime;
	}

	public void setExamTime(Date examTime) {
		ExamTime = examTime;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public R_Department getR_Department() {
		return R_Department;
	}

	public void setR_Department(R_Department r_Department) {
		R_Department = r_Department;
	}

	public List<R_Subject> getR_Subject() {
		return R_Subject;
	}

	public void setR_Subject(List<R_Subject> r_Subject) {
		R_Subject = r_Subject;
	}


}
