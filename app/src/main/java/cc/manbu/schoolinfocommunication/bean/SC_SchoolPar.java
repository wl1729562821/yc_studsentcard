package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.Date;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;

public class SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = -6458491261273861327L;
	// 编号
	// [EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)]
	private int Id;
	// 所属学校
	// [EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)]
	private int SchoolId;
	// 所属学校
	// [EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)]
	private SC_School School;
	// 备注
	// [EasyuiAttribute(IsShowList = false)]
	private String Remark;
	// 添加时间
	// [EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)]
	private Date AddDate;
	// 修改时间
	// [EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)]
	private Date UpdateDate;
	// 是否系统数据
	private boolean IsSys;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getSchoolId() {
		return SchoolId;
	}

	public void setSchoolId(int schoolId) {
		SchoolId = schoolId;
	}

	public SC_School getSchool() {
		return School;
	}

	public void setSchool(SC_School school) {
		School = school;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}



	public Date getAddDate() {
		return AddDate;
	}

	public void setAddDate(Date addDate) {
		AddDate = addDate;
	}

	public Date getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(Date updateDate) {
		UpdateDate = updateDate;
	}

	public boolean isIsSys() {
		return IsSys;
	}

	public void setIsSys(boolean isSys) {
		IsSys = isSys;
	}


	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}


}
