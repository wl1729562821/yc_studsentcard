package cc.manbu.schoolinfocommunication.events;

import java.util.List;

import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.Device_Geography;
import cc.manbu.schoolinfocommunication.bean.Hw_Homework;
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation;
import cc.manbu.schoolinfocommunication.bean.R_Department;
import cc.manbu.schoolinfocommunication.bean.R_Subject;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.bean.SHX002COW;
import cc.manbu.schoolinfocommunication.bean.SHX007AlarmClock;
import cc.manbu.schoolinfocommunication.bean.SHX007Scenemode;
import cc.manbu.schoolinfocommunication.bean.SHX520Alarmclock;
import cc.manbu.schoolinfocommunication.bean.SR_Exam;
import cc.manbu.schoolinfocommunication.bean.SR_Score;
import cc.manbu.schoolinfocommunication.bean.Sleave;

/**
 * Created by manbuAndroid5 on 2017/4/18.
 */

public class ResposeEvent {
    private String message;
    private String content;
    private int flg;
    private R_Users rUsers;
    private MobileDevicAndLocation devicAndLocation;
    private List<Device_Geography> geographies;
    private List<SHX002COW> checkWorksList;
    private SHX007AlarmClock alarmClock;
    private Device device;
    private List<Hw_Homework>hw_homeworks;
    private List<SR_Exam> sr_exams;
    private List<SR_Score> sr_scores;
    private List<Sleave> sleaves;
    private List<R_Department> departments;
    private List<R_Subject> subjects;
    private List<R_Users> rUsersList;
    private SHX007Scenemode scenemode;
    private SHX520Alarmclock shx520Alarmclock;
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

    public R_Users getrUsers() {
        return rUsers;
    }

    public void setrUsers(R_Users rUsers) {
        this.rUsers = rUsers;
    }

    public MobileDevicAndLocation getDevicAndLocation() {
        return devicAndLocation;
    }

    public void setDevicAndLocation(MobileDevicAndLocation devicAndLocation) {
        this.devicAndLocation = devicAndLocation;
    }

    public List<Device_Geography> getGeographies() {
        return geographies;
    }

    public void setGeographies(List<Device_Geography> geographies) {
        this.geographies = geographies;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SHX002COW> getCheckWorksList() {
        return checkWorksList;
    }

    public void setCheckWorksList(List<SHX002COW> checkWorksList) {
        this.checkWorksList = checkWorksList;
    }

    public SHX007AlarmClock getAlarmClock() {
        return alarmClock;
    }

    public void setAlarmClock(SHX007AlarmClock alarmClock) {
        this.alarmClock = alarmClock;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<Hw_Homework> getHw_homeworks() {
        return hw_homeworks;
    }

    public void setHw_homeworks(List<Hw_Homework> hw_homeworks) {
        this.hw_homeworks = hw_homeworks;
    }

    public List<SR_Exam> getSr_exams() {
        return sr_exams;
    }

    public void setSr_exams(List<SR_Exam> sr_exams) {
        this.sr_exams = sr_exams;
    }

    public List<SR_Score> getSr_scores() {
        return sr_scores;
    }

    public void setSr_scores(List<SR_Score> sr_scores) {
        this.sr_scores = sr_scores;
    }

    public List<Sleave> getSleaves() {
        return sleaves;
    }

    public void setSleaves(List<Sleave> sleaves) {
        this.sleaves = sleaves;
    }

    public List<R_Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<R_Department> departments) {
        this.departments = departments;
    }

    public List<R_Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<R_Subject> subjects) {
        this.subjects = subjects;
    }

    public List<R_Users> getrUsersList() {
        return rUsersList;
    }

    public void setrUsersList(List<R_Users> rUsersList) {
        this.rUsersList = rUsersList;
    }

    public SHX007Scenemode getScenemode() {
        return scenemode;
    }

    public void setScenemode(SHX007Scenemode scenemode) {
        this.scenemode = scenemode;
    }

    public SHX520Alarmclock getShx520Alarmclock() {
        return shx520Alarmclock;
    }

    public void setShx520Alarmclock(SHX520Alarmclock shx520Alarmclock) {
        this.shx520Alarmclock = shx520Alarmclock;
    }
}
