package cc.manbu.schoolinfocommunication.push;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


public class EasyUIDataGrid<T> implements Serializable {

	public int total;
	
	public List<T> rows;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public void setValue(JSONObject jo, Class<T> clazz){
	//	if(jo!=null && ("EasyUIDataGrid".equals(jo.optString("__type")) || jo.optString("__type").endsWith(clazz.getSimpleName()))){
			this.setTotal(jo.optInt("total"));
			JSONArray jArray;
			try {
				jArray = jo.getJSONArray("rows");
				this.setRows((List<T>) JSONHelper.parseCollection(jArray, ArrayList.class,clazz));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	//}
}
