package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 权限
 *
 * @author gongyong2014
 *
 */
public class R_Rights extends SC_SchoolPar implements Serializable {

	/**
	 *
	 */
	public static final long serialVersionUID = -5347579099055031363L;

	// / <summary>
	// / 编号
	// / </summary>
	// Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)
	// 编号
	//public int Id;

	// / <summary>
	// / 权限名称
	// / </summary>
	// 权限名称
	public String RightName;
	// / <summary>
	// / 权限类型
	// / 1.父节点
	// / 2.页面权限
	// / 3.其他权限
	// / </summary>
	// 权限类型
	public int RightType;
	// / <summary>
	// / 地址
	// / </summary>
	// 地址
	// EasyuiAttribute(IsShowList = false)
	public String Url;
	// / <summary>
	// / 图标
	// / </summary>
	// 图标
	public String Icon;

	// XmlIgnore
	// ScriptIgnoreAttribute
	public List<R_Role> R_Role;
	// / <summary>
	// / 外键ID
	// / </summary>
	// EasyuiAttribute(IsShowList = false)
	public int Par_R_RightsId;
	// / <summary>
	// / 外键实体
	// / </summary>
	// XmlIgnore
	// ScriptIgnoreAttribute
	public R_Rights Par_R_Rights;
	// / <summary>
	// / 子类
	// / </summary>
	public List<R_Rights> Ch_R_Rights;



	public String getRightName() {
		return RightName;
	}

	public void setRightName(String rightName) {
		RightName = rightName;
	}

	public int getRightType() {
		return RightType;
	}

	public void setRightType(int rightType) {
		RightType = rightType;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getIcon() {
		return Icon;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	public List<R_Role> getR_Role() {
		return R_Role;
	}

	public void setR_Role(List<R_Role> r_Role) {
		R_Role = r_Role;
	}

	public int getPar_R_RightsId() {
		return Par_R_RightsId;
	}

	public void setPar_R_RightsId(int par_R_RightsId) {
		Par_R_RightsId = par_R_RightsId;
	}

	public R_Rights getPar_R_Rights() {
		return Par_R_Rights;
	}

	public void setPar_R_Rights(R_Rights par_R_Rights) {
		Par_R_Rights = par_R_Rights;
	}

	public List<R_Rights> getCh_R_Rights() {
		return Ch_R_Rights;
	}

	public void setCh_R_Rights(List<R_Rights> ch_R_Rights) {
		Ch_R_Rights = ch_R_Rights;
	}

	@Override
	public String toString() {
		return "R_Rights [Id=" + getId() + ", RightName=" + RightName
				+ ", RightType=" + RightType + ", Url=" + Url + ", Icon="
				+ Icon + ", R_Role=" + R_Role + ", Par_R_RightsId="
				+ Par_R_RightsId + ", Par_R_Rights=" + Par_R_Rights
				+ ", Ch_R_Rights=" + Ch_R_Rights + "]";
	}


}
