package cc.manbu.schoolinfocommunication.push;

import java.io.Serializable;

/**
 * 可以被保存在sqlite数据库的实体类
 * @author gongyong2014
 *
 */
public interface SqliteEntity extends Serializable {

	public String get_id();
	public void set_id(String _id);
	
	
}
