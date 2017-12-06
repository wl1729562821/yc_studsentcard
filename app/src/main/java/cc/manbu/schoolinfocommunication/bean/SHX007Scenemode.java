package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class SHX007Scenemode implements Serializable {
	public String _id;
	/// <summary>
	/// 当前的情景模式
	/// 一般模式(0x00);
	/// 静音模式(0x01);
	/// 校内模式(会议)(0x02)
	/// 校外模式(户外)(0x03)
	/// </summary>
	public int CModel;


	/// <summary>
	/// 一般模式(0x00);
	/// </summary>
	public SHX007ScenemodeEntity M0;

	/// <summary>
	/// 静音模式(0x01);
	/// </summary>
	public SHX007ScenemodeEntity M1;

	/// <summary>
	/// 校内模式(会议)(0x02)
	/// </summary>
	public SHX007ScenemodeEntity M2;

	/// <summary>
	/// 校外模式(户外)(0x03)
	/// </summary>
	public SHX007ScenemodeEntity M3;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getCModel() {
		return CModel;
	}

	public void setCModel(int cModel) {
		CModel = cModel;
	}

	public SHX007ScenemodeEntity getM0() {
		return M0;
	}

	public void setM0(SHX007ScenemodeEntity m0) {
		M0 = m0;
	}

	public SHX007ScenemodeEntity getM1() {
		return M1;
	}

	public void setM1(SHX007ScenemodeEntity m1) {
		M1 = m1;
	}

	public SHX007ScenemodeEntity getM2() {
		return M2;
	}

	public void setM2(SHX007ScenemodeEntity m2) {
		M2 = m2;
	}

	public SHX007ScenemodeEntity getM3() {
		return M3;
	}

	public void setM3(SHX007ScenemodeEntity m3) {
		M3 = m3;
	}



	@Override
	public String toString() {
		return JSONHelper.toComplexJSON(this);
	}

}
