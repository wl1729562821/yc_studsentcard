package cc.manbu.schoolinfocommunication.bean;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.Date;

import cc.manbu.schoolinfocommunication.httputils.JsonCollectionsOfCheckWorksResonseParser;

/**
 * Created by manbuAndroid5 on 2017/4/25.
 */
@HttpResponse(parser = JsonCollectionsOfCheckWorksResonseParser.class)
public class SHX002COW implements Serializable {
    private static final long serialVersionUID = -7655872787972390544L;
    private String _id;
    private String Serialnumber;
    private Date Time;
    //正常 迟到  早退
    private String State;
    private String Code;
    private String Ty;
    //1 进校   2 离校  3 上校车  4 下校车
    private int Type ;

    public String get_id() {
        return _id;
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

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getTy() {
        return Ty;
    }

    public void setTy(String ty) {
        Ty = ty;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
