package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.bean.SHX007Scenemode;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.Utils;
import cc.manbu.schoolinfocommunication.view.customer.SuperRadioGroup;

public class SceneModeActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_llNormal)
    public LinearLayout id_llNormal;
    @BindView(R2.id.id_superGroup)
    public SuperRadioGroup id_superGroup;
    @BindView(R2.id.id_llSilence)
    public LinearLayout id_llSilence;
    @BindView(R2.id.id_llInSchool)
    public LinearLayout id_llInSchool;
    @BindView(R2.id.id_llOutSchool)
    public LinearLayout id_llOutSchool;
    @BindView(R2.id.id_tvSave)
    public TextView id_tvSave;

    public SHX007Scenemode scenemode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_scene_mode;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        R_Users user = Configs.get(Configs.Config.CurUser,R_Users.class);
        if (user != null){
            getMLoadingDoialog().show();
            getMNetHelper().accessSceneMode(user.getSerialnumber());
        }
    }

    public void init(){
        id_tvTitle.setText(R.string.text_scence_mode);
        id_superGroup.setOnCheckedChangeListener(new SuperRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SuperRadioGroup group, int checkedId) {
                switch (checkedId){
                    case R2.id.id_rbNormal:
                        scenemode.setCModel(0x00);
                        break;
                    case R2.id.id_rbSilence:
                        scenemode.setCModel(0x01);
                        break;
                    case R2.id.id_rbInSchool:
                        scenemode.setCModel(0x02);
                        break;
                    case R2.id.id_rbOutSchool:
                        scenemode.setCModel(0x03);
                        break;
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SHX007GetScenemode".equals(msg)){
            getMLoadingDoialog().dismiss();
            scenemode = event.getScenemode();
            Utils.scenemode = scenemode;
            if (scenemode != null){
                switch(scenemode.getCModel()){
                    case 0x00:
                        id_superGroup.check(R.id.id_rbNormal);
                        break;
                    case 0x01:
                        id_superGroup.check(R.id.id_rbSilence);
                        break;
                    case 0x02:
                        id_superGroup.check(R.id.id_rbInSchool);
                        break;
                    case 0x03:
                        id_superGroup.check(R.id.id_rbOutSchool);
                        break;
                }
            }
        }else if ("SHX007SetScenemode".equals(msg)){
            getMLoadingDoialog().dismiss();
            String res = event.getContent();
            try {
                JSONObject object = new JSONObject(res);
                String s = object.getString("d");
                showMessage(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @OnClick(value = {R.id.id_tvSave,R.id.id_llNormal,R.id.id_llSilence,
            R.id.id_llInSchool,
            R.id.id_llOutSchool})
    public void onClick(View v){

        switch (v.getId()){
            case R.id.id_tvSave:
                scenemode = Utils.scenemode;
                if (scenemode != null){
                    Map<String,Object> params = new HashMap<>();
                    params.put("SHX007Scenemode",scenemode);
                    getMLoadingDoialog().show();
                    getMNetHelper().setSceneMode(params);
                }
                break;
            case R.id.id_llNormal:
                Utils.modeCode = 0x00;
                intentView();
                break;
            case R.id.id_llSilence:
                Utils.modeCode = 0x01;
                intentView();
                break;
            case R.id.id_llInSchool:
                Utils.modeCode = 0x02;
                intentView();
                break;
            case R.id.id_llOutSchool:
                Utils.modeCode = 0x03;
                intentView();
                break;
        }
    }

    public void intentView(){
        Intent intent = new Intent(this,UpdateSceneModeActivity.class);
        Utils.scenemode = scenemode;
        startActivity(intent);
    }
}
