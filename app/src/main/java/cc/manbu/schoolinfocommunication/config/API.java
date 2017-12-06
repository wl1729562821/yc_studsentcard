package cc.manbu.schoolinfocommunication.config;

import android.util.SparseArray;

/**
 * Created by manbuAndroid5 on 2017/4/18.
 */

public class API {
    private static final SparseArray<String> store = new SparseArray<>();
    public static final int Login  = generateApiId("Login");
    public static final int SaveAndroidToken  = generateApiId("SaveAndroidToken");
    public static final int GetMobileDevicAndLocation  = generateApiId("GetMobileDevicAndLocation");
    public static final int SearchDevice_Geography_V2  = generateApiId("SearchDevice_Geography_V2");
    public static final int SaveOrUpdateGeography  = generateApiId("SaveOrUpdateGeography");
    public static final int DeleteGeography  = generateApiId("DeleteGeography");
    public static final int SHX520GetPhoneBook_V2  = generateApiId("SHX520GetPhoneBook_V2");
    public static final int SHX520SetPhoneBook_V2  = generateApiId("SHX520SetPhoneBook_V2");
    public static final int SearchSHX002COW  = generateApiId("SearchSHX002COW");
    public static final int SHX007Getalarmclock  = generateApiId("SHX007Getalarmclock");
    public static final int SHX007Setalarmclock  = generateApiId("SHX007Setalarmclock");
    public static final int GetAboutUrl  = generateApiId("GetAboutUrl");
    public static final int GetAnnouncementUrl  = generateApiId("GetAnnouncementUrl");
    public static final int GetNewsUrl  = generateApiId("GetNewsUrl");
    public static final int GetActivityUrl  = generateApiId("GetActivityUrl");
    public static final int SHX520SetManyPhoneConfig  = generateApiId("SHX520SetManyPhoneConfig");
    public static final int GetDeviceDetial  = generateApiId("GetDeviceDetial");
    public static final int SHX520SetSleepTime  = generateApiId("SHX520SetSleepTime");
    public static final int FireWall  = generateApiId("FireWall");
    public static final int SHX520FindDevice  = generateApiId("SHX520FindDevice");
    public static final int SHX520SigleAddressQuery  = generateApiId("SHX520SigleAddressQuery");
    public static final int SHX520SetWorkMode  = generateApiId("SHX520SetWorkMode");
    public static final int SHX520RemotePowerSet  = generateApiId("SHX520RemotePowerSet");
    public static final int SHX520Factory  = generateApiId("SHX520Factory");
    public static final int GetUserHw_Homework  = generateApiId("GetUserHw_Homework");
    public static final int GetCurSR_Exam  = generateApiId("GetCurSR_Exam");
    public static final int SearchScore  = generateApiId("SearchScore");
    public static final int SHX520ParentChildInteraction  = generateApiId("SHX520ParentChildInteraction");
    public static final int SHX520CancelParentChildInteraction  = generateApiId("SHX520CancelParentChildInteraction");
    public static final int GetUserSleave  = generateApiId("GetUserSleave");
    public static final int AddSleave  = generateApiId("AddSleave");
    public static final int SearchCurriculumlis  = generateApiId("SearchCurriculumlis");
    public static final int GetHomeworkByTeacher  = generateApiId("GetHomeworkByTeacher");
    public static final int GetR_DepartmentListByTeacherId  = generateApiId("GetR_DepartmentListByTeacherId");
    public static final int UpdateOrAddHomework  = generateApiId("UpdateOrAddHomework");
    public static final int SearchSleave  = generateApiId("SearchSleave");
    public static final int AgreeSleave  = generateApiId("AgreeSleave");
    public static final int DisAgreeSleave  = generateApiId("DisAgreeSleave");
    public static final int GetR_SubjectListByTeacherId  = generateApiId("GetR_SubjectListByTeacherId");
    public static final int GetR_StudentListByDeptId  = generateApiId("GetR_StudentListByDeptId");
    public static final int GetTeacherCurriculum  = generateApiId("GetTeacherCurriculum");
    public static final int UpdatePwd  = generateApiId("UpdatePwd");
    public static final int SaveFeedBack  = generateApiId("SaveFeedBack");
    public static final int GetTCPPopMsgAddress  = generateApiId("GetTCPPopMsgAddress");
    public static final int GetPwd  = generateApiId("GetPwd");
    public static final int SHX007SetTCITYEASYIntervalforContinuousTracking  = generateApiId("SHX007SetTCITYEASYIntervalforContinuousTracking");
    public static final int SHX520SetInterval  = generateApiId("SHX520SetInterval");
    public static final int SHX007GetScenemode  = generateApiId("SHX007GetScenemode");
    public static final int SHX007SetScenemode  = generateApiId("SHX007SetScenemode");
    public static final int SHX002ClassTimeControl  = generateApiId("SHX002ClassTimeControl");
    public static final int SHX007SetGPSOpen  = generateApiId("SHX007SetGPSOpen");
    public static final int SHX520FireWallSetting  = generateApiId("SHX520FireWallSetting");
    public static final int SHX520KeylockSet  = generateApiId("SHX520KeylockSet");
    public static final int GetDeviceTraceDataStr_V2  = generateApiId("GetDeviceTraceDataStr_V2");
    public static final int SHX520Getalarmclock  = generateApiId("SHX520Getalarmclock");
    public static final int SHX5200SetAlarmclock  = generateApiId("SHX5200SetAlarmclock");
    public static final int SHX007BatchSetSOSNum  = generateApiId("SHX007BatchSetSOSNum");
    public static final int SHX007ListenPhone  = generateApiId("SHX007ListenPhone");
    public static final int SHX520SetListenNo  = generateApiId("SHX520SetListenNo");
    /**
     * 根据接口名生成接口实体
     * @param api
     * @return
     */
    private static int generateApiId(String api){
        int key = api.hashCode()+10000;
        store.put(key, api);
        return key;
    }

    /**
     * 根据apiId查找api
     * @param apiId 本类的常量
     * @return
     */
    public static String getApi(int apiId){
        return store.get(apiId);
    }
}
