package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.jaeger.library.StatusBarUtil;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

public class TimerActivity extends BaseActivityStudent implements RadialTimePickerDialogFragment.OnTimeSetListener{
    @BindView(R2.id.id_tvOnTime)
    public TextView id_tvOnTime;
    @BindView(R2.id.id_tvOffTime)
    public TextView id_tvOffTime;
    @BindView(R2.id.id_tvSetOnTime)
    public TextView id_tvSetOnTime;
    @BindView(R2.id.id_tvSetOffTime)
    public TextView id_tvSetOffTime;
    @BindView(R2.id.id_tvSave)
    public TextView id_tvSave;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_rightSwitchButton)
    public SwitchButton id_rightSwitchButton;

    public int[] onTimeArray = new int[2];
    public int[] offTimeArray = new int[2];
    public boolean isOnTime = true;
    public int powerState = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_timer;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        getMLoadingDoialog().show();
        getMNetHelper().getDeviceDetial();
        id_rightSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                powerState = isChecked ? 1 : 0;
            }
        });
        findViewById(R.id.id_tvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanSave()){
                    showMessage(getResources().getString(R.string.text_justy_time));
                }else {
                    getMLoadingDoialog().show();
                    getMNetHelper().setSleepTime(powerState,onTimeArray,offTimeArray);
                }
            }
        });
        findViewById(R.id.id_tvSetOnTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TimerActivity","点击事件");
                showTimeDialog(true);
            }
        });
        findViewById(R.id.id_tvSetOffTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TimerActivity","点击事件1");
                showTimeDialog(false);
            }
        });
    }

    public boolean isCanSave(){
        if (onTimeArray[0] > offTimeArray[0]){
            return false;
        }
        if (onTimeArray[0] == offTimeArray[0]){
            if (onTimeArray[1] >= offTimeArray[1]){
                return false;
            }
        }
        return true;
    }
    public void showTimeDialog(boolean b){
        isOnTime = b;
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment().setOnTimeSetListener(this).setThemeLight();
        rtpd.show(getSupportFragmentManager(),"TimerActivity");
    }
    public String getNumberString(int num){
        String format = "";
        if(num < 10){
            format = "0" + num;
        }else if(num >= 10){
            format = String.valueOf(num);
        }
        return format;
    }
    public void init(){
        id_rightSwitchButton.setVisibility(View.VISIBLE);
        id_tvTitle.setText(R.string.text_sleep_on_time);
    }

    public void curTimeSetIfNoValue(){
        Date date = new Date();
        int hour = Integer.parseInt(DateUtil.format("H",date));
        int min = Integer.parseInt(DateUtil.format("m",date));
        onTimeArray[0] = hour;
        onTimeArray[1] = min;
        offTimeArray[0] = hour + 1;
        offTimeArray[1] = min;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        if (isOnTime){
            onTimeArray[0] = hourOfDay;
            onTimeArray[1] = minute;
            id_tvOnTime.setText(getNumberString(hourOfDay)+":"+getNumberString(minute));
        }else {
            offTimeArray[0] = hourOfDay;
            offTimeArray[1] = minute;
            id_tvOffTime.setText(getNumberString(hourOfDay)+":"+getNumberString(minute));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("GetDeviceDetial".equals(msg)){
            getMLoadingDoialog().dismiss();
            Device d = event.getDevice();
            SHX520Device_Config config = d.getSHX520Device_Config();
            if (config != null) {
                powerState = config.getPowerState();
                id_rightSwitchButton.setChecked(powerState == 1);
                if(config.getPowerOnTime()!=null){
                    String[] power_on_time = config.getPowerOnTime().split(":");
                    if(power_on_time.length==2){
                        onTimeArray[0] = Integer.valueOf(power_on_time[0]);
                        onTimeArray[1] = Integer.valueOf(power_on_time[1]);
                        id_tvOnTime.setText(getNumberString(onTimeArray[0])+":"+getNumberString(onTimeArray[1]));
                    }else {
                        curTimeSetIfNoValue();
                        id_tvOnTime.setText(getNumberString(onTimeArray[0])+":"+getNumberString(onTimeArray[1]));
                    }
                }else {
                    curTimeSetIfNoValue();
                    id_tvOnTime.setText(getNumberString(onTimeArray[0])+":"+getNumberString(onTimeArray[1]));
                }
                if(config.getPowerOffTime()!=null){
                    String[] power_off_time = config.getPowerOffTime().split(":");
                    if(power_off_time.length==2){
                        offTimeArray[0] = Integer.valueOf(power_off_time[0]);
                        offTimeArray[1] = Integer.valueOf(power_off_time[1]);
                        id_tvOffTime.setText(getNumberString(offTimeArray[0])+":"+getNumberString(offTimeArray[1]));
                    }else {
                        curTimeSetIfNoValue();
                        id_tvOffTime.setText(getNumberString(offTimeArray[0])+":"+getNumberString(offTimeArray[1]));
                    }
                }else {
                    curTimeSetIfNoValue();
                    id_tvOffTime.setText(getNumberString(offTimeArray[0])+":"+getNumberString(offTimeArray[1]));
                }
            }
        }else if ("SHX520SetSleepTime".equals(msg)){
            getMLoadingDoialog().dismiss();
            showMessage(getResources().getString(R.string.text_sleep_time_success));
        }
    }
}
