package cc.manbu.schoolinfocommunication.bean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class R_Users extends SC_SchoolPar implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2512134996715806968L;

	private String icon;
	//头像url
	private String U_ImageUrl;
	//头像图片唯一标识符
	private String U_Image;
	private int R_RoleId;
	private String _id;
	private Boolean IsOpen;
	// 拥有的角色
	private R_Role R_Role;

	private int R_DepartmentId;

	private R_Department R_Department;

	// 登录名
	private String LoginName;

	// 用户名
	private String UserName;

	// 曾用名
	private String UserName2;

	// 密码
	private String PassWord;
	// 出生年月
	private Date Birthday;

	// 联系电话
	private String Tel;

	// 户籍地址
	private String Address;

	// 籍贯
	private String Placeoforigin;

	//签名
	private String Signature;

	// 民族
	private String Nation;

	// 身份证号码
	private String CardId;

	// / <summary>
	// / 如果为 true 代表男性 否则代表女性
	// / </summary>
	// 性别
	private String Sex;

	// 政治面貌
	private String PoliticalAffiliation;

	// 入校时间
	private Date JoinIime;

	// 教师扩展信息
	private R_Teacher R_Teacher;

	// 学生扩展信息
	private R_Student R_Student;

	public String Icon;

	private String Serialnumber;

	private String DepName;

	private boolean IsTeacher;
	private List<R_Department> R_DepartmentList;
	private List<R_Subject> R_SubjectList;
	private boolean isLogined;
	public long lastLoginedTime;

	public boolean isLogined() {
		return isLogined;
	}

	public long getLastLoginedTime() {
		return lastLoginedTime;
	}

	public void setLastLoginedTime(long lastLoginedTime) {
		this.lastLoginedTime = lastLoginedTime;
	}

	public void setLogined(boolean logined) {
		isLogined = logined;
	}

	public List<R_Department> getR_DepartmentList() {
		return R_DepartmentList;
	}

	public void setR_DepartmentList(List<R_Department> r_DepartmentList) {
		R_DepartmentList = r_DepartmentList;
	}

	public List<R_Subject> getR_SubjectList() {
		return R_SubjectList;
	}

	public void setR_SubjectList(List<R_Subject> r_SubjectList) {
		R_SubjectList = r_SubjectList;
	}

	public boolean getIsTeacher() {
		return IsTeacher;
	}

	public void setIsTeacher(boolean isTeacher) {
		IsTeacher = isTeacher;
	}

	public String getSerialnumber() {
		return IsTeacher?LoginName:Serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		Serialnumber = serialnumber;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getR_RoleId() {
		return R_RoleId;
	}

	public void setR_RoleId(int r_RoleId) {
		R_RoleId = r_RoleId;
	}

	public R_Role getR_Role() {
		return R_Role;
	}

	public void setR_Role(R_Role r_Role) {
		R_Role = r_Role;
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

	public String getLoginName() {
		return LoginName;
	}

	public void setLoginName(String loginName) {
		LoginName = loginName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserName2() {
		return UserName2;
	}

	public void setUserName2(String userName2) {
		UserName2 = userName2;
	}

	public String getPassWord() {
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}



	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPlaceoforigin() {
		return Placeoforigin;
	}

	public void setPlaceoforigin(String placeoforigin) {
		Placeoforigin = placeoforigin;
	}

	public String getNation() {
		return Nation;
	}

	public void setNation(String nation) {
		Nation = nation;
	}

	public String getCardId() {
		return CardId;
	}

	public void setCardId(String cardId) {
		CardId = cardId;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getPoliticalAffiliation() {
		return PoliticalAffiliation;
	}

	public void setPoliticalAffiliation(String politicalAffiliation) {
		PoliticalAffiliation = politicalAffiliation;
	}



	public Date getBirthday() {
		return Birthday;
	}

	public void setBirthday(Date birthday) {
		Birthday = birthday;
	}

	public Date getJoinIime() {
		return JoinIime;
	}

	public void setJoinIime(Date joinIime) {
		JoinIime = joinIime;
	}

	public R_Teacher getR_Teacher() {
		return R_Teacher;
	}

	public void setR_Teacher(R_Teacher r_Teacher) {
		R_Teacher = r_Teacher;
	}

	public R_Student getR_Student() {
		return R_Student;
	}

	public void setR_Student(R_Student r_Student) {
		R_Student = r_Student;
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

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

	public String getU_ImageUrl() {
		return U_ImageUrl;
	}

	public void setU_ImageUrl(String u_ImageUrl) {
		U_ImageUrl = u_ImageUrl;
	}

	public String getU_Image() {
		return U_Image;
	}

	public void setU_Image(String u_Image) {
		U_Image = u_Image;
	}

	public String get_id() {
		return getId()+"";
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Boolean getIsOpen() {
		return IsOpen;
	}

	public void setIsOpen(Boolean IsOpen) {
		this.IsOpen = IsOpen;
	}

	public static JSONObject generateSimpleUserInfoWithJson(String loginName, String PassWord){
		JSONObject obj = new JSONObject();
		try {
			obj.put("LoginName", loginName);
			obj.put("PassWord", PassWord);
		} catch (JSONException e) {
			obj = null;
			e.printStackTrace();
		}
		return obj;
	}

	public static R_Users parseSimpleUserInfo(JSONObject userInfo){
		R_Users user = null;
		try {
			if(userInfo==null){
				return null;
			}
			String loginName = userInfo.getString("LoginName");
			String PassWord = userInfo.getString("PassWord");
			user = new R_Users();
			user.setLoginName(loginName);
			user.setPassWord(PassWord);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

}
