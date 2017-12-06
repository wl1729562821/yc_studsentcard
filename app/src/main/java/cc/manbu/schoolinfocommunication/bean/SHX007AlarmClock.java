package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class SHX007AlarmClock implements Serializable {
	 public String _id ;

     public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public List<SHX007AlarmClockEntity> getSHX007AlarmClockEntity() {
		return SHX007AlarmClockEntity;
	}
	
	

	public void setSHX007AlarmClockEntity(
			List<SHX007AlarmClockEntity> sHX007AlarmClockEntity) {
		SHX007AlarmClockEntity = sHX007AlarmClockEntity;
	}
	
	@Override
	public String toString() {
		return JSONHelper.toJSON(this);
	}

	public List<SHX007AlarmClockEntity> SHX007AlarmClockEntity;

	public static class SHX007AlarmClockEntity implements Serializable
	{
	    public boolean Isopen;

	    public int H;

	    public int S;

	    public int type=1000;

		public boolean getIsopen() {
			return Isopen;
		}

		public void setIsopen(boolean isopen) {
			Isopen = isopen;
		}

		public int getH() {
			return H;
		}

		public void setH(int h) {
			H = h;
		}

		public int getS() {
			return S;
		}

		public void setS(int s) {
			S = s;
		}
	    

		@Override
		public String toString() {
			return JSONHelper.toJSON(this);
		}
	}

}
 