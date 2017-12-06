package cc.manbu.schoolinfocommunication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 *
 * @author gongyong2014
 *
 */
public class R_Role extends SC_SchoolPar implements Serializable {

	public static final long serialVersionUID = 9158285188233204954L;

	// 角色名称
	public String RoleName;

	// 权限
	public List<R_Rights> Rights;

	// XmlIgnore
	// ScriptIgnoreAttribute
	public List<R_Users> R_Users;

	public String getRoleName() {
		return RoleName;
	}

	public void setRoleName(String roleName) {
		RoleName = roleName;
	}

	public List<R_Rights> getRights() {
		return Rights;
	}

	public void setRights(List<R_Rights> rights) {
		Rights = rights;
	}

	public List<R_Users> getR_Users() {
		return R_Users;
	}

	public void setR_Users(List<R_Users> r_Users) {
		R_Users = r_Users;
	}

	@Override
	public String toString() {
		return "R_Role [RoleName=" + RoleName + ", Rights=" + Rights
				+ ", R_Users=" + R_Users + "]";
	}


}
