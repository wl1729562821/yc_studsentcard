package cc.manbu.schoolinfocommunication.push;


import java.util.Date;

;import cc.manbu.schoolinfocommunication.tools.JSONHelper;


public class MG_UserMsgM implements SqliteEntity{
	public static final int STATE_NOT_TO_SEND=0;
	public static final int STATE_WILL_TO_SEND=1;
	public static final int STATE_ON_SENDING=2;
	public static final int STATE_SEND_SUCCESS=3;
	public static final int STATE_SEND_FAILURE=-1;
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 7136942525570280308L;
//	/// <summary>
    /// 唯一ID
    /// </summary>
    public String _id;
    // <summary>
    /// 谁发送的
    /// </summary>
    public String From;
    /// <summary>
    /// 发给谁的
    /// </summary>
    public String To;
    
    public int From_Id;
    public String From_U_Image;
    public String FromName;
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
    public int MsgType;
    /// <summary>
    /// 当消息类型 包含文件的时候 这个就是文件的路径
    /// </summary>
    public String Url;
    /// <summary>
    /// 消息的创建时间
    /// </summary>
    public Date CreateTime;

    /// <summary>
    /// 当包含位置信息的时候 有这个对象
    /// </summary>
    public Lo_Location local;

    /// <summary>
    /// 消息标题
    /// </summary>
    public String Title;
    /// <summary>
    /// 消息内容
    /// </summary>
    public String Context;

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
    public String Desc;
    /// <summary>
    /// 设备回复的数据信息
    /// 暂且不需使用
    /// </summary>
    public String ContextData;
    
    public String UserId="-10";
    /**
     * 是否已读(本地属性)   1已读  0 未读
     */
    public int IsRead=0;
    
    private boolean IIsSender = false;
    
    private String IsTODevice = "false";
    
    
    
    public boolean getIIsSender() {
		return IIsSender;
	}
	public void setIIsSender(boolean iIsSender) {
		IIsSender = iIsSender;
	}
	/**
     * 发送状态(本地属性) 
     * -1 发送失败的消息  (STATE_SEND_FAILURE)
     * 0 不是要发送的消息  (STATE_NOT_TO_SEND)
     * 1 即将发送的消息  (STATE_WILL_TO_SEND)
     * 2 正在发送的消息(STATE_ON_SENDING)
     * 3 发送成功的消息(STATE_SEND_SUCCESS)
     */
    private int SendState=0;
    
	public int getSendState() {
		return SendState;
	}
	public void setSendState(int sendState) {
		SendState = sendState;
	}
	
	
	public String getIsTODevice() {
		return IsTODevice;
	}
	public void setIsTODevice(String IsTODevice) {
		this.IsTODevice = IsTODevice;
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
	
	public String getFromName() {
		return FromName;
	}
	public void setFromName(String fromName) {
		FromName = fromName;
	}
	public void setFrom_U_Image(String from_U_Image) {
		From_U_Image = from_U_Image;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
	public int getIsRead() {
		return IsRead;
	}
	public void setIsRead(int isRead) {
		IsRead = isRead;
	}
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	@Override
    public String toString() {
    	return JSONHelper.toComplexJSON(this);
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Context == null) ? 0 : Context.hashCode());
		result = prime * result
				+ ((ContextData == null) ? 0 : ContextData.hashCode());
		result = prime * result
				+ ((CreateTime == null) ? 0 : CreateTime.hashCode());
		result = prime * result + ((Desc == null) ? 0 : Desc.hashCode());
		result = prime * result + ((From == null) ? 0 : From.hashCode());
		result = prime * result
				+ ((FromName == null) ? 0 : FromName.hashCode());
		result = prime * result + From_Id;
		result = prime * result
				+ ((From_U_Image == null) ? 0 : From_U_Image.hashCode());
		result = prime * result + IsRead;
		result = prime * result + MsgType;
		result = prime * result + SendState;
		result = prime * result + ((Title == null) ? 0 : Title.hashCode());
		result = prime * result + ((To == null) ? 0 : To.hashCode());
		result = prime * result + ((Url == null) ? 0 : Url.hashCode());
		result = prime * result + ((UserId == null) ? 0 : UserId.hashCode());;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MG_UserMsgM other = (MG_UserMsgM) obj;
		if (Context == null) {
			if (other.Context != null)
				return false;
		} else if (!Context.equals(other.Context))
			return false;
		if (ContextData == null) {
			if (other.ContextData != null)
				return false;
		} else if (!ContextData.equals(other.ContextData))
			return false;
		if (CreateTime == null) {
			if (other.CreateTime != null)
				return false;
		} else if (!CreateTime.equals(other.CreateTime))
			return false;
		if (Desc == null) {
			if (other.Desc != null)
				return false;
		} else if (!Desc.equals(other.Desc))
			return false;
		if (From == null) {
			if (other.From != null)
				return false;
		} else if (!From.equals(other.From))
			return false;
		if (FromName == null) {
			if (other.FromName != null)
				return false;
		} else if (!FromName.equals(other.FromName))
			return false;
		if (From_Id != other.From_Id)
			return false;
		if (From_U_Image == null) {
			if (other.From_U_Image != null)
				return false;
		} else if (!From_U_Image.equals(other.From_U_Image))
			return false;
		if (IsRead != other.IsRead)
			return false;
		if (MsgType != other.MsgType)
			return false;
		if (SendState != other.SendState)
			return false;
		if (Title == null) {
			if (other.Title != null)
				return false;
		} else if (!Title.equals(other.Title))
			return false;
		if (To == null) {
			if (other.To != null)
				return false;
		} else if (!To.equals(other.To))
			return false;
		if (Url == null) {
			if (other.Url != null)
				return false;
		} else if (!Url.equals(other.Url))
			return false;
		if (UserId != other.UserId)
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		return true;
	}
    

}
