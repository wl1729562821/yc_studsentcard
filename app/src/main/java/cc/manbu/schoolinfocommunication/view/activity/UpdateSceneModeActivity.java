package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.SHX007Scenemode;
import cc.manbu.schoolinfocommunication.bean.SHX007ScenemodeEntity;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.Utils;

public class UpdateSceneModeActivity extends BaseActivityStudent{
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_seekBarPhone)
    public SeekBar id_seekBarPhone;
    @BindView(R2.id.id_seekBarMessage)
    public SeekBar id_seekBarMessage;
    @BindView(R2.id.id_seekBarVoice)
    public SeekBar id_seekBarVoice;

    public SHX007Scenemode scenemode;
    public SHX007ScenemodeEntity mode;
    public int modeCode = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_scene_mode;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        scenemode = Utils.scenemode;
        modeCode = Utils.modeCode;
        id_seekBarPhone.setOnSeekBarChangeListener(listener);
        id_seekBarMessage.setOnSeekBarChangeListener(listener);
        id_seekBarVoice.setOnSeekBarChangeListener(listener);
        switch (modeCode){
            case 0x00:
                id_tvTitle.setText(R.string.text_normal);
                mode = scenemode.getM0()==null?new SHX007ScenemodeEntity():scenemode.getM0();
                scenemode.setM0(mode);
                break;
            case 0x01:
                id_tvTitle.setText(R.string.text_silence);
                mode = scenemode.getM1()==null?new SHX007ScenemodeEntity():scenemode.getM1();
                scenemode.setM1(mode);
                break;
            case 0x02:
                id_tvTitle.setText(R.string.text_in_school);
                mode = scenemode.getM2()==null?new SHX007ScenemodeEntity():scenemode.getM2();
                scenemode.setM2(mode);
                break;
            case 0x03:
                id_tvTitle.setText(R.string.text_ou_school);
                mode = scenemode.getM3()==null?new SHX007ScenemodeEntity():scenemode.getM3();
                scenemode.setM3(mode);
                break;
        }
        if(null!=mode){
            id_seekBarPhone.setProgress(mode.getCV());
            id_seekBarMessage.setProgress(mode.getSV());
            id_seekBarVoice.setProgress(mode.getPV());
        }
    }

    public void init(){

    }

    @Override
    public void back() {
        switch (modeCode){
            case 0x00:
                scenemode.setM0(mode);
                break;
            case 0x01:
                scenemode.setM1(mode);
                break;
            case 0x02:
                scenemode.setM2(mode);
                break;
            case 0x03:
                scenemode.setM3(mode);
                break;
        }
        Utils.scenemode = scenemode;
        finish();
    }

    public SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()){
                case R2.id.id_seekBarPhone:
                    mode.setCV(progress);
                    break;
                case R2.id.id_seekBarMessage:
                    mode.setSV(progress);
                    break;
                case R2.id.id_seekBarVoice:
                    mode.setPV(progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
