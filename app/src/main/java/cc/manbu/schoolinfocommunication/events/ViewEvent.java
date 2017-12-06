package cc.manbu.schoolinfocommunication.events;

import java.util.List;

import cc.manbu.schoolinfocommunication.bean.SHX007AlarmClock;
import cc.manbu.schoolinfocommunication.bean.SHX520Alarmclock;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.bean.Sleave;

/**
 * Created by manbuAndroid5 on 2017/4/18.
 */

public class ViewEvent {

    public int type=1000;

    private String message;
    private int flg;
    private String content;
    private SHX007AlarmClock.SHX007AlarmClockEntity entity;
    private SHX520Alarmclock.SHX520AlarmClockEntity clockEntity;
    private SHX520Device_Config config;
    private Sleave sleave;
    private List<Sleave>sleaveList;
    private boolean isCheck;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SHX007AlarmClock.SHX007AlarmClockEntity getEntity() {
        return entity;
    }

    public void setEntity(SHX007AlarmClock.SHX007AlarmClockEntity entity) {
        this.entity = entity;
    }

    public SHX520Device_Config getConfig() {
        return config;
    }

    public void setConfig(SHX520Device_Config config) {
        this.config = config;
    }

    public Sleave getSleave() {
        return sleave;
    }

    public void setSleave(Sleave sleave) {
        this.sleave = sleave;
    }

    public List<Sleave> getSleaveList() {
        return sleaveList;
    }

    public void setSleaveList(List<Sleave> sleaveList) {
        this.sleaveList = sleaveList;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public SHX520Alarmclock.SHX520AlarmClockEntity getClockEntity() {
        return clockEntity;
    }

    public void setClockEntity(SHX520Alarmclock.SHX520AlarmClockEntity clockEntity) {
        this.clockEntity = clockEntity;
    }
}
