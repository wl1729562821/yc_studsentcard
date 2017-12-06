package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.view.fragment.TaskCompleteFragment;
import cn.yc.student.view.fragment.TaskFragment;
import cn.yc.student.view.fragment.TaskProgressFragment;

public class StepsActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_rlRight)
    public FrameLayout id_rlRight;
    @BindView(R2.id.id_tvRight)
    public TextView id_tvRight;

    private HashSet<String> fragmentTag = new HashSet<>();
    private HashMap<String,Fragment> cachFragment = new HashMap<>();
    private String currentTag;
    private SHX520Device_Config config;
    private int steps;
    private int flgs;

    @Override
    public int getLayoutId() {
        return R.layout.activity_steps;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        showFragments("TaskFragment");
    }

    private void init(){
        id_tvTitle.setText(R.string.text_steps);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void back() {
        if (flgs == 0 || flgs == 2){
            finish();
        }else if (flgs == 1){
            showFragments("TaskFragment");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event){
        String msg = event.getMessage();
        if (Constant.EVENT_TASK_MESSAGE.equals(msg)){
            config = event.getConfig();
            steps = event.getFlg();
            flgs = 1;
            showFragments("TaskProgressFragment");
        }else if (Constant.EVENT_TASK_COMPLETE.equals(msg)){
            flgs = 2;
            showFragments("TaskCompleteFragment");
        }
    }
    @OnClick(value = {R.id.id_rlRight})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.id_rlRight:
                getMLoadingDoialog().show();
                getMNetHelper().cancelTask();
                break;
        }
    }
    private void showFragments(String tag){
        Fragment fragment = null;
        id_rlRight.setVisibility(View.INVISIBLE);
        if ("TaskFragment".equals(tag)){
            fragment = new TaskFragment();
        }else if ("TaskProgressFragment".equals(tag)){
            fragment = new TaskProgressFragment();
            Bundle mBoundle = new Bundle();
            mBoundle.putSerializable("config", config);
            mBoundle.putInt("steps", steps);
            fragment.setArguments(mBoundle);
            id_rlRight.setVisibility(View.VISIBLE);
            id_tvRight.setText(R.string.clear);
        }else if ("TaskCompleteFragment".equals(tag)){
            fragment = new TaskCompleteFragment();
        }
        cachFragment.put(tag,fragment);
        selectFragment(tag);
    }
    protected void selectFragment(String tag){
        fragmentTag.add(tag);
        currentTag = tag;
        hideAllFragmen();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null){
            fragment = cachFragment.get(tag);
            transaction.add(R.id.id_flStepsContent,fragment,tag);
        }else {
            transaction.show(fragment);
        }
        transaction.commit();
    }
    private void hideAllFragmen(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (String tag : fragmentTag){
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null)
                transaction.hide(fragment);
        }
        transaction.commit();
    }
}
