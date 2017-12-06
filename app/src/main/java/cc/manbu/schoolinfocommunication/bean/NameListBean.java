package cc.manbu.schoolinfocommunication.bean;

/**
 * Created by manbuAndroid5 on 2017/4/20.
 */

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 * Created by manbuAndroid5 on 2017/3/15.
 */
@Table(name = "LoginNameList",onCreated = "")
public class NameListBean {
    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;

    public NameListBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
