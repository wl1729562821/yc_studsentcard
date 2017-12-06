package cc.manbu.schoolinfocommunication.view.activity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.SHX007AlarmClock;
import cc.manbu.schoolinfocommunication.bean.SHX520Alarmclock;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.config.DeviceType;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.view.adapter.DailyRemindersAdapter;
import cc.manbu.schoolinfocommunication.view.adapter.SHX520DailyRemindersAdapter;
import cn.yc.model.listener.BaseOnItemListener;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;

import static cc.manbu.schoolinfocommunication.config.ManbuApplication.context;

public class ClockActivity extends BaseActivityStudent implements RadialTimePickerDialogFragment.OnTimeSetListener {
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;
    @BindView(R2.id.id_refreshLayout)
    public SwipeRefreshLayout id_refreshLayout;
    @BindView(R2.id.mListView)
    public RecyclerView mListView;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_tvRight)
    public TextView id_tvRight;
    @BindView(R2.id.id_rlRight)
    public FrameLayout id_rlRight;

    private List<SHX007AlarmClock.SHX007AlarmClockEntity> data = new ArrayList<>();
    private List<SHX520Alarmclock.SHX520AlarmClockEntity> data520 = new ArrayList<>();
    private DailyRemindersAdapter mAdapter;
    private SHX520DailyRemindersAdapter mRemindersAdapter;
    private int position;
    private SHX007AlarmClock shx007AlarmClock;
    private SHX520Alarmclock shx520Alarmclock;

    @Override
    public int getLayoutId() {
        return R.layout.activity_clock;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        getMLoadingDoialog().show();
        if (Configs.curDeviceType == DeviceType.StudentCard)
            getMNetHelper().accessClocks();
        else getMNetHelper().access520Clocks();
        mListView.setLayoutManager(new LinearLayoutManager(this));
        init();
    }

    private void init() {
        id_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Configs.curDeviceType == DeviceType.StudentCard)
                    getMNetHelper().accessClocks();
                else getMNetHelper().access520Clocks();
            }
        });
        if (Configs.curDeviceType == DeviceType.StudentCard) {
            mAdapter = new DailyRemindersAdapter(this, data);
            mAdapter.setListener(new BaseOnItemListener<Integer>() {
                @Override
                public void itemClick(@Nullable Integer integer) {
                    position=integer;
                    if(position+1==mAdapter.getItemCount()){
                        if (shx007AlarmClock != null && Configs.curDeviceType == DeviceType.StudentCard) {
                            shx007AlarmClock.setSHX007AlarmClockEntity(data);
                            getMLoadingDoialog().show();
                            getMNetHelper().setClocks(shx007AlarmClock);
                        } else if (shx520Alarmclock != null && Configs.curDeviceType != DeviceType.StudentCard) {
                            shx520Alarmclock.setSHX520AlarmClockEntity(data520);
                            getMLoadingDoialog().show();
                            getMNetHelper().set520Clocks(shx520Alarmclock);
                        } else {
                            showMessage(getResources().getString(R.string.text_unkonw_err));
                        }
                        return;
                    }
                    RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                            .setOnTimeSetListener(ClockActivity.this).setThemeLight();
                    rtpd.show(getSupportFragmentManager(), "EditDailyRemindersActivity");
                }
            });
            mListView.setAdapter(mAdapter);
        } else {
            mRemindersAdapter = new SHX520DailyRemindersAdapter(context, data520);
            mRemindersAdapter.setListener(new BaseOnItemListener<Integer>() {
                @Override
                public void itemClick(@Nullable Integer integer) {
                    position=integer;
                    if(position+1==mRemindersAdapter.getItemCount()){
                        if (shx007AlarmClock != null && Configs.curDeviceType == DeviceType.StudentCard) {
                            shx007AlarmClock.setSHX007AlarmClockEntity(data);
                            getMLoadingDoialog().show();
                            getMNetHelper().setClocks(shx007AlarmClock);
                        } else if (shx520Alarmclock != null && Configs.curDeviceType != DeviceType.StudentCard) {
                            shx520Alarmclock.setSHX520AlarmClockEntity(data520);
                            getMLoadingDoialog().show();
                            getMNetHelper().set520Clocks(shx520Alarmclock);
                        } else {
                            showMessage(getResources().getString(R.string.text_unkonw_err));
                        }
                        return;
                    }
                    RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                            .setOnTimeSetListener(ClockActivity.this).setThemeLight();
                    //rtpd.show(getSupportFragmentManager(), "EditDailyRemindersActivity");
                }
            });
            mListView.setAdapter(mRemindersAdapter);
        }
        id_refreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.orange),
                ContextCompat.getColor(context, R.color.red),
                ContextCompat.getColor(context, R.color.burlywood));
        id_refreshLayout.setProgressViewEndTarget(true, 120);//设置距离顶端的距离

        id_tvTitle.setText(R.string.text_alerts);
        id_tvRight.setText(R.string.text_save);
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

   /* @Event(value = R.id.mListView, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment().setOnTimeSetListener(this).setThemeLight();
        rtpd.show(getSupportFragmentManager(), "EditDailyRemindersActivity");
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewEvent event) {
        String msg = event.getMessage();
        if (Constant.EVENT_CHANGE_CLOCK.equals(msg)) {
            int pos = event.getFlg();
            SHX007AlarmClock.SHX007AlarmClockEntity clockEntity = event.getEntity();
            data.set(pos, clockEntity);
        } else if (Constant.EVENT_CHANGE_520_CLOCK.equals(msg)) {
            int pos = event.getFlg();
            SHX520Alarmclock.SHX520AlarmClockEntity clockEntity = event.getClockEntity();
            data520.set(pos, clockEntity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event) {
        String msg = event.getMessage();
        if ("SHX007Getalarmclock".equals(msg)) {
            getMLoadingDoialog().dismiss();
            id_refreshLayout.setRefreshing(false);
            shx007AlarmClock = event.getAlarmClock();
            List<SHX007AlarmClock.SHX007AlarmClockEntity> entity = shx007AlarmClock.getSHX007AlarmClockEntity();
            data.clear();
//            data.addAll(entity);
            for (int i = 0; i < 4; i++) {
                data.add(entity.get(i));
            }
            if (mAdapter != null) {
                mAdapter.setData(data);
                mListView.setVisibility(mAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
                id_llEmpty.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        } else if ("SHX007Setalarmclock".equals(msg) || "SHX5200SetAlarmclock".equals(msg)) {
            getMLoadingDoialog().dismiss();
            showMessage(getResources().getString(R.string.text_save_success));
        } else if ("SHX520Getalarmclock".equals(msg)) {
            getMLoadingDoialog().dismiss();
            id_refreshLayout.setRefreshing(false);
            shx520Alarmclock = event.getShx520Alarmclock();
            List<SHX520Alarmclock.SHX520AlarmClockEntity> entities = shx520Alarmclock.getSHX520AlarmClockEntity();
            data520.clear();
//            data520.addAll(entities);
            for (int i = 0; i < 4; i++) {
                data520.add(entities.get(i));
            }
            if (mRemindersAdapter != null) {
                mRemindersAdapter.setData(data520);
                mListView.setVisibility(mRemindersAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
                id_llEmpty.setVisibility(mRemindersAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        if (Configs.curDeviceType == DeviceType.StudentCard) {
            data.get(position).setH(hourOfDay);
            data.get(position).setS(minute);
            if (mAdapter != null)
                mAdapter.setData(data);
        } else {
            data520.get(position).setH(hourOfDay);
            data520.get(position).setS(minute);
            if (mRemindersAdapter != null)
                mRemindersAdapter.setData(data520);
        }
    }
}
