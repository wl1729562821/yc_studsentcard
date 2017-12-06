package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

import cc.manbu.schoolinfocommunication.httputils.JsonObjectResonseParser;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;

@HttpResponse(parser = JsonObjectResonseParser.class)
public class SHX520Alarmclock implements Serializable {
	private String _id ;
	private List<SHX520AlarmClockEntity> SHX520AlarmClockEntity;
	
	
	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public List<SHX520AlarmClockEntity> getSHX520AlarmClockEntity() {
		return SHX520AlarmClockEntity;
	}


	public void setSHX520AlarmClockEntity(
			List<SHX520AlarmClockEntity> sHX520AlarmClockEntity) {
		SHX520AlarmClockEntity = sHX520AlarmClockEntity;
	}


	public static class SHX520AlarmClockEntity implements Serializable {
		private int H;
		private int S;
		private int Index;
		public int type=1000;
		private int Mode;
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
		public int getIndex() {
			return Index;
		}
		public void setIndex(int index) {
			Index = index;
		}
		public int getMode() {
			return Mode;
		}
		public void setMode(int mode) {
			Mode = mode;
		}
		@Override
		public String toString() {
			return JSONHelper.toJSON(this);
		}
		
		
		
	}

}
