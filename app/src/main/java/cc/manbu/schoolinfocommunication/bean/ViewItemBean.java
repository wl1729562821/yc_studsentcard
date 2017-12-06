package cc.manbu.schoolinfocommunication.bean;

/**
 * Created by manbuAndroid5 on 2017/4/20.
 */

public class ViewItemBean {
    private String name;
    private String value;
    private int id;
    private int state;
    private int stateClassTime;
    private int stateGPS;
    private int stateKeyLock;

    public int type=10;
    public String uri;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ViewItemBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", id=" + id +
                ", state=" + state +
                ", stateClassTime=" + stateClassTime +
                ", stateGPS=" + stateGPS +
                ", stateKeyLock=" + stateKeyLock +
                ", type=" + type +
                ", uri='" + uri + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStateClassTime() {
        return stateClassTime;
    }

    public void setStateClassTime(int stateClassTime) {
        this.stateClassTime = stateClassTime;
    }

    public int getStateGPS() {
        return stateGPS;
    }

    public void setStateGPS(int stateGPS) {
        this.stateGPS = stateGPS;
    }

    public int getStateKeyLock() {
        return stateKeyLock;
    }

    public void setStateKeyLock(int stateKeyLock) {
        this.stateKeyLock = stateKeyLock;
    }
}
