package cc.manbu.schoolinfocommunication.view.activity;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import cc.manbu.schoolinfocommunication.view.fragment.HistoryRouteFragment;

public class HistoryRouteActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_ivRight)
    public ImageView id_ivRight;
    @BindView(R2.id.id_rlRight)
    public FrameLayout id_rlRight;
    @BindView(R2.id.id_ivTrangle)
    public ImageView id_ivTrangle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history_route;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        HistoryRouteFragment historyRouteFragment = new HistoryRouteFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.id_rlHistoryRoute,historyRouteFragment,"HistoryRouteFragment");
        transaction.commit();
        init();
    }

    private void init(){
        id_tvTitle.setText(R.string.text_track);
        id_ivRight.setImageResource(R.drawable.trace_button_selector);
        id_ivRight.setVisibility(View.VISIBLE);
    }
    @OnClick(value = {R.id.id_rlRight,R.id.id_ivRight})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.id_ivRight:
            case R.id.id_rlRight:
                ViewEvent event = new ViewEvent();
                event.setMessage(Constant.EVENT_POP_TIME_TRACE);
                EventBus.getDefault().post(event);
                id_ivTrangle.setVisibility(View.VISIBLE);
                animTrangle(id_ivTrangle,false);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event){
        String msg = event.getMessage();
        if (Constant.EVENT_HIDE_POP_TIME_TRACE.equals(msg)){
            animTrangle(id_ivTrangle,true);
        }
    }
    private void animTrangle(ImageView view, final boolean isHide){
        AnimatorSet set = new AnimatorSet();
        float fromAlpha = 0;
        float toAlpha = 1;
        float fromX = 0;
        float toX = -id_ivRight.getWidth();
        if (isHide){
            fromAlpha = 1;
            toAlpha = 0;
            fromX = -id_ivRight.getWidth();
            toX = 0;
        }
        set.playTogether(
                ObjectAnimator.ofFloat(view,"translationX",fromX,toX),
                ObjectAnimator.ofFloat(view,"alpha",fromAlpha,toAlpha)
        );
        set.setDuration(1000L).start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isHide){
                    id_ivTrangle.setVisibility(View.INVISIBLE);
                }else {
                    id_ivTrangle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
