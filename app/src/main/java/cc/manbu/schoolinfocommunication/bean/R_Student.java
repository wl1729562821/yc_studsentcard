package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;

/**
 * 学生
 *
 * @author gongyong2014
 *
 */
public class R_Student extends SC_SchoolPar implements Serializable {

	/**
	 *
	 */
	public static final long serialVersionUID = -6306574576547302202L;
	// ForeignKey("R_Users")
	// XmlIgnore
	// ScriptIgnoreAttribute
	public R_Users R_Users;
	public String S1_Name;
	public String S2_Name;
	public String S3_Name;

//	public int getId() {
//		toolbar_back Id;
//	}
//
//	public void setId(int id) {
//		Id = id;
//	}

	public R_Users getR_Users() {
		return R_Users;
	}

	public void setR_Users(R_Users r_Users) {
		R_Users = r_Users;
	}

	public String getS1_Name() {
		return S1_Name;
	}

	public void setS1_Name(String s1_Name) {
		S1_Name = s1_Name;
	}

	public String getS2_Name() {
		return S2_Name;
	}

	public void setS2_Name(String s2_Name) {
		S2_Name = s2_Name;
	}

	public String getS3_Name() {
		return S3_Name;
	}

	public void setS3_Name(String s3_Name) {
		S3_Name = s3_Name;
	}

	@Override
	public String toString() {
		return "R_Student [Id=" + getId() + ", R_Users=" + R_Users + ", S1_Name="
				+ S1_Name + ", S2_Name=" + S2_Name + ", S3_Name=" + S3_Name
				+ "]";
	}

}
