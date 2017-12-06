package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


/**
 * 学校
 *
 * @author gongyong2014
 *
 */
public class SC_School implements Serializable {
	/**
	 *
	 */
	public static final long serialVersionUID = -2410134885584508769L;
	// Key
	// 编号
	public int Id;
	// / <summary>
	// / 学校名称
	// / </summary>
	// Required
	// MaxLength(100), MinLength(0)
	// 名称
	public String Name;
	// 地址
	public String Address;
	// 联系电话
	public String TelPhone;
	// 学校介绍 移动版
	public String IntroduceMobile;
	// 学校介绍
	public String Introduce;
	// 校长寄语
	public String Idx_PresidentSMSG;
	// 联系我们
	public String Idx_ContactUs;
	// 最新图片
	public String Idx_NewPhoto;
	// 备注
	// MaxLength(500), MinLength(0)
	public String Remark;
	// 添加时间
	// EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)
	public String AddDate;
	// 修改时间
	// EasyuiAttribute(IsShowAdd = false, IsShowEdit = false)
	public String UpdateDate;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getTelPhone() {
		return TelPhone;
	}

	public void setTelPhone(String telPhone) {
		TelPhone = telPhone;
	}

	public String getIntroduceMobile() {
		return IntroduceMobile;
	}

	public void setIntroduceMobile(String introduceMobile) {
		IntroduceMobile = introduceMobile;
	}

	public String getIntroduce() {
		return Introduce;
	}

	public void setIntroduce(String introduce) {
		Introduce = introduce;
	}

	public String getIdx_PresidentSMSG() {
		return Idx_PresidentSMSG;
	}

	public void setIdx_PresidentSMSG(String idx_PresidentSMSG) {
		Idx_PresidentSMSG = idx_PresidentSMSG;
	}

	public String getIdx_ContactUs() {
		return Idx_ContactUs;
	}

	public void setIdx_ContactUs(String idx_ContactUs) {
		Idx_ContactUs = idx_ContactUs;
	}

	public String getIdx_NewPhoto() {
		return Idx_NewPhoto;
	}

	public void setIdx_NewPhoto(String idx_NewPhoto) {
		Idx_NewPhoto = idx_NewPhoto;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getAddDate() {
		return AddDate;
	}

	public void setAddDate(String addDate) {
		AddDate = addDate;
	}

	public String getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(String updateDate) {
		UpdateDate = updateDate;
	}

	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}


}
