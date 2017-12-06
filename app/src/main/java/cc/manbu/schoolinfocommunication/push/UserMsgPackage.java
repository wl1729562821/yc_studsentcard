package cc.manbu.schoolinfocommunication.push;



import java.util.List;

public class UserMsgPackage {
	public String CMD;
	public List<String> Parameters;

	public List<String> getParameters() {
		return Parameters;
	}

	public void setParameters(List<String> parameters) {
		Parameters = parameters;
	}

	public String getCMD() {
		return CMD;
	}

	public void setCMD(String cMD) {
		CMD = cMD;
	}

	@Override
	public String toString() {
		return "UserMsgPackage [CMD=" + CMD + ", Parameters=" + Parameters
				+ "]";
	}

	
	 
}
