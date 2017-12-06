package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.SHX007Device_Config;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.config.DeviceType;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;

public class ListenActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_edUploadSpace)
    public EditText id_edListen;
    @BindView(R2.id.id_tvStartListen)
    public TextView id_tvStartListen;

    @Override
    public int getLayoutId() {
        return R.layout.activity_listen;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        id_tvStartListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listentNo = id_edListen.getText().toString();
                if (TextUtils.isEmpty(listentNo)){
                    showMessage(getResources().getString(R.string.hint_listen));
                }else {
                    getMLoadingDoialog().show();
                    if (Configs.curDeviceType == DeviceType.StudentCard){
                        getMNetHelper().setListenPhone(listentNo);
                    }else {
                        getMNetHelper().setListenNo(listentNo);
                    }
                }
            }
        });
        init();
        getMLoadingDoialog().show();
        getMNetHelper().getDeviceDetial();
    }

    private void init(){
        id_tvTitle.setText(R.string.text_listener);
        /*toolbar.setTitle("");
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SHX520SetListenNo".equals(msg)||"SHX007ListenPhone".equals(msg)){
            getMLoadingDoialog().dismiss();
            String number = event.getContent();
            String s = getResources().getString(R.string.text_listening);
            s = String.format(s,number);
            showMessage(s);
        }else if ("GetDeviceDetial".equals(msg)){
            getMLoadingDoialog().dismiss();
            Device d = event.getDevice();
            if (d != null){
                SHX007Device_Config shx007DeviceConfig = d.getSHX007Device_Config();
                SHX520Device_Config shx520DeviceConfig = d.getSHX520Device_Config();
                if (shx007DeviceConfig != null){
                    id_edListen.setText(shx007DeviceConfig.getListenNum());
                }
                if (shx520DeviceConfig != null){
                    id_edListen.setText(shx520DeviceConfig.getListenNo());
                }
                Editable editable = id_edListen.getText();
                id_edListen.setSelection(editable.length());
            }
        }
    }
}
