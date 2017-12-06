package cc.manbu.schoolinfocommunication.view.activity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.bean.Sleave;
import cn.yc.student.view.fragment.AddSleaveFragment;
import cn.yc.student.view.fragment.GetOffFragment;
import cn.yc.student.view.fragment.OffDeatilsFragment;

public class DayOffActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_rlRight)
    public FrameLayout id_rlRight;
    @BindView(R2.id.toolbar_right)
    public ImageView id_tvRight;

    private HashSet<String> fragmentTag = new HashSet<>();
    private HashMap<String,Fragment> cachFragment = new HashMap<>();
    private String currentTag;
    private boolean isFinish = true;
    private Sleave sleave;

    private TextView tv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_day_off;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        showFragments("GetOffFragment");
    }

    private void init(){
        id_tvTitle.setText(R.string.text_vacations);
        id_tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void back() {
        doReturns();
    }

    @Override
    public void onBackPressed() {
        doReturns();
    }

    @OnClick(R.id.toolbar_right)
    public void onClick(){
        isFinish = false;
        showFragments("AddSleaveFragment");
    }
    private void doReturns(){
        if (isFinish){
            finish();
        }else {
            isFinish = true;
            removeFragmentByTag(currentTag);
            showFragments("GetOffFragment");
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event){
        if(event.type==10000){
            doReturns();
            return;
        }
        String msg = event.getMessage();
        if (Constant.EVENT_OFF_DEATILS.equals(msg)){
            isFinish = false;
            sleave = event.getSleave();
            showFragments("OffDeatilsFragment");
        }
    }
    private void showFragments(String tag){
        Fragment fragment = null;
        id_rlRight.setVisibility(View.VISIBLE);
        if ("GetOffFragment".equals(tag)){
            id_tvRight.setVisibility(View.VISIBLE);
            fragment = new GetOffFragment();
        }else if ("OffDeatilsFragment".equals(tag)){
            fragment = new OffDeatilsFragment();
            Bundle mBoundle = new Bundle();
            mBoundle.putSerializable("sleave", sleave);
            fragment.setArguments(mBoundle);
            id_tvRight.setVisibility(View.INVISIBLE);
        }else if ("AddSleaveFragment".equals(tag)){
            fragment = new AddSleaveFragment();
            id_tvRight.setVisibility(View.INVISIBLE);
        }
        cachFragment.put(tag,fragment);
        selectFragment(tag);
    }
    @SuppressLint("ResourceType")
    protected void selectFragment(String tag){
        fragmentTag.add(tag);
        currentTag = tag;
        hideAllFragmen();
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null){
            fragment = cachFragment.get(tag);
            transaction.add(R.id.id_flOffContent,fragment,tag);
        }else {
            transaction.show(fragment);
        }
        transaction.commit();
    }
    private void hideAllFragmen(){
        FragmentManager fm =getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        for (String tag : fragmentTag){
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null)
                transaction.hide(fragment);
        }
        transaction.commit();
    }
    private void removeFragmentByTag(String tag){
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null)
            transaction.remove(fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (isFinish){
                return super.onKeyDown(keyCode, event);
            }else {
                isFinish = true;
                removeFragmentByTag(currentTag);
                showFragments("GetOffFragment");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
