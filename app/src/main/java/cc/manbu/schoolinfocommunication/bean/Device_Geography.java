package cc.manbu.schoolinfocommunication.bean;

import android.graphics.Point;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfGeographyResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

@HttpResponse(parser = JsonCollectionsOfGeographyResonseParser.class)
public class Device_Geography implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String _id;
	private String Serialnumber;
	// 0 出栅栏报警
	// 1 进栅栏报警
	// 2进出栅栏报警
	// 如果原来在里面 则为出栅栏报警 否则就是进入栅栏报警
	private int Type;
	private Date CreateTime;
	private String geography;
	private String Name;
	private List<Point> _GetPointList;
	private boolean IsIn;
	private int Shape;
	private int Radius;
	public int type=10;
	public String get_id() {

		return _id;
	}

	public Device_Geography(int type) {
		this.type = type;
	}

	public Device_Geography() {
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSerialnumber() {
		return Serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		Serialnumber = serialnumber;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getGeography() {
		return geography;
	}

	public void setGeography(String geography) {
		this.geography = geography;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isIsIn() {
		return IsIn;
	}

	public void setIsIn(boolean isIn) {
		IsIn = isIn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Point> get_GetPointList() {
		return _GetPointList;
	}

	public void set_GetPointList(List<Point> _GetPointList) {
		this._GetPointList = _GetPointList;
	}
	@Override
	public String toString() {
		return JSONHelper.toJSON(this);
	}

	public int getShape() {
		return Shape;
	}

	public void setShape(int shape) {
		Shape = shape;
	}

	public int getRadius() {
		return Radius;
	}

	public void setRadius(int radius) {
		Radius = radius;
	}
}
