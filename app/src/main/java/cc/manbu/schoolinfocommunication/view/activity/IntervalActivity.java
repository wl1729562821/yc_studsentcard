package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.bean.SHX007Device_Config;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.config.DeviceType;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;

public class IntervalActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_edUploadSpace)
    public EditText id_edUploadSpace;
    @BindView(R2.id.id_tvSubmit)
    public TextView id_tvSubmit;

    private R_Users user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_interval;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        user = Configs.get(Configs.Config.CurUser,R_Users.class);
        getMLoadingDoialog().show();
        getMNetHelper().getDeviceDetial();
    }

    private void init(){
        id_tvTitle.setText(R.string.text_upload_time);
       /* toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }
    @OnClick(value = {R.id.id_tvSubmit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.id_tvSubmit:
                String space = id_edUploadSpace.getText().toString().trim();
                if (TextUtils.isEmpty(space)){
                    showMessage(getString(R.string.hint_space_time));
                }else if (user == null){
                    showMessage(getString(R.string.text_access_device_failed));
                }else {
                    getMLoadingDoialog().show();
                    if (Configs.curDeviceType == DeviceType.StudentCard)
                        getMNetHelper().setUploadInterval(user.getSerialnumber(),space);
                    else
                        getMNetHelper().set520UploadInterval(user.getSerialnumber(),space);
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SHX007SetTCITYEASYIntervalforContinuousTracking".equals(msg)){
            getMLoadingDoialog().dismiss();
            String result = event.getContent();
            try {
                JSONObject object = new JSONObject(result);
                String s = object.getString("d");
                showMessage(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if ("SHX520SetInterval".equals(msg)){
            getMLoadingDoialog().dismiss();
            String result = event.getContent();
            try {
                JSONObject object = new JSONObject(result);
                String s = object.getString("d");
                showMessage(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if ("GetDeviceDetial".equals(msg)){
            Device d = event.getDevice();
            getMLoadingDoialog().dismiss();
            if (d != null){
                SHX007Device_Config config = d.getSHX007Device_Config();
                SHX520Device_Config configs520 = d.getSHX520Device_Config();
                if (config != null){
                    String timeSpace = config.getIntervalTime();
                    id_edUploadSpace.setText(timeSpace);
                }
                if (configs520 != null){
                    String timeSpace = configs520.getInterval();
                    id_edUploadSpace.setText(timeSpace);
                }
                Editable editable = id_edUploadSpace.getText();
                id_edUploadSpace.setSelection(editable.length());
            }
        }
    }
}
