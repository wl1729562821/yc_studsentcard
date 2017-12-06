package cc.manbu.schoolinfocommunication.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

import cc.manbu.schoolinfocommunication.push.Lo_Location;

/**onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 * Created by manbuAndroid5 on 2017/3/15.
 */
@Table(name = "MG_UserMsgM",onCreated = "")
public class PushUserMsgM {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    /// <summary>
    /// 唯一ID
    /// </summary>
    @Column(name = "id",isId = true,autoGen = true,property = "NOT NULL")
    private String id;
    // <summary>
    /// 谁发送的
    /// </summary>
    @Column(name = "From")
    private String From;
    /// <summary>
    /// 发给谁的
    /// </summary>
    @Column(name = "To")
    private String To;
    @Column(name = "From_Id")
    private int From_Id;
    @Column(name = "From_U_Image")
    private String From_U_Image;
    @Column(name = "FromName")
    private String FromName;
    /// <summary>
    /// 设备的消息类型
    /// 1.设备的报警消息
    /// 2.设备的回复消息
    /// 3.用户的文本消息
    /// 4.用户的图片消息
    /// 5.用户的语音消息
    /// 6.用户的文件消息
    /// 7.用户的位置消息
    /// 8.用户的表情消息
    /// 9 请假通知
    /// 10 作业通知
    ///
    /// </summary>
    @Column(name = "MsgType")
    private int MsgType;
    /// <summary>
    /// 当消息类型 包含文件的时候 这个就是文件的路径
    /// </summary>
    @Column(name = "Url")
    private String Url;
    /// <summary>
    /// 消息的创建时间
    /// </summary>
    @Column(name = "CreateTime")
    private Date CreateTime;

    /// <summary>
    /// 当包含位置信息的时候 有这个对象
    /// </summary>
    private Lo_Location local;

    /// <summary>
    /// 消息标题
    /// </summary>
    @Column(name = "Title")
    private String Title;
    /// <summary>
    /// 消息内容
    /// </summary>
    @Column(name = "Context")
    private String Context;

    /// <summary>[SHX520_004_Location]
    /// 设备回复的命令号码   暂且不需使用
    /// </summary>
    /**
     * 设备回复的命令号码 ,守护星520推送类型
     * [SHX520_004_Location]            宝贝定位
     [SHX520_0081_StepData]           计步器数据
     [SHX520_0083_Temperature]        体温数据上报
     [SHX520_0084_Cow]                考勤上报
     [SHX520_434B_Voice]              录音推送
     [SHX520_434C_TextPush]           文本推送
     */
    @Column(name = "Desc")
    private String Desc;
    /// <summary>
    /// 设备回复的数据信息
    /// 暂且不需使用
    /// </summary>
    @Column(name = "ContextData")
    private String ContextData;
    @Column(name = "UserId")
    private String UserId="-10";
    /**
     * 是否已读(本地属性)   1已读  0 未读
     */
    @Column(name = "IsRead")
    private int IsRead=0;
    @Column(name = "IIsSender")
    private boolean IIsSender = false;
    @Column(name = "IsTODevice")
    private String IsTODevice = "false";
    /**
     * 发送状态(本地属性) 
     * -1 发送失败的消息  (STATE_SEND_FAILURE)
     * 0 不是要发送的消息  (STATE_NOT_TO_SEND)
     * 1 即将发送的消息  (STATE_WILL_TO_SEND)
     * 2 正在发送的消息(STATE_ON_SENDING)
     * 3 发送成功的消息(STATE_SEND_SUCCESS)
     */
    @Column(name = "SendState")
    private int SendState=0;
    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public PushUserMsgM() {
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public int getFrom_Id() {
        return From_Id;
    }

    public void setFrom_Id(int from_Id) {
        From_Id = from_Id;
    }

    public String getFrom_U_Image() {
        return From_U_Image;
    }

    public void setFrom_U_Image(String from_U_Image) {
        From_U_Image = from_U_Image;
    }

    public String getFromName() {
        return FromName;
    }

    public void setFromName(String fromName) {
        FromName = fromName;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }

    public Lo_Location getLocal() {
        return local;
    }

    public void setLocal(Lo_Location local) {
        this.local = local;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getContextData() {
        return ContextData;
    }

    public void setContextData(String contextData) {
        ContextData = contextData;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }

    public boolean isIIsSender() {
        return IIsSender;
    }

    public void setIIsSender(boolean IIsSender) {
        this.IIsSender = IIsSender;
    }

    public String getIsTODevice() {
        return IsTODevice;
    }

    public void setIsTODevice(String isTODevice) {
        IsTODevice = isTODevice;
    }

    public int getSendState() {
        return SendState;
    }

    public void setSendState(int sendState) {
        SendState = sendState;
    }
}
