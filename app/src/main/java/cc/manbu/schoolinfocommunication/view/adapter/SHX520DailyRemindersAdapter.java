package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.SHX520Alarmclock;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cn.yc.model.listener.BaseOnItemListener;
import cc.manbu.schoolinfocommunication.R;

/**
 * Created by manbuAndroid5 on 2017/3/2.
 */

public class SHX520DailyRemindersAdapter extends RecyclerView.Adapter implements SwitchButton.OnCheckedChangeListener {
    public List<SHX520Alarmclock.SHX520AlarmClockEntity> list=new ArrayList<>();
    public Context context;

    public MyViewHolder mViewHolder;
    public AddViewHolder mAddViewHolder;

    public BaseOnItemListener<Integer> mListener;

    public SHX520DailyRemindersAdapter(Context context, List<SHX520Alarmclock.SHX520AlarmClockEntity> list){
        this.context = context;
        this.list.addAll(list);
    }

    public void setListener(BaseOnItemListener<Integer> listener){
        mListener=listener;
    }

    public void setData(List<SHX520Alarmclock.SHX520AlarmClockEntity> list){
        this.list.clear();
        notifyDataSetChanged();
        this.list.addAll(list);
        SHX520Alarmclock.SHX520AlarmClockEntity entity=new SHX520Alarmclock.SHX520AlarmClockEntity();
        entity.type=2000;
        this.list.add(entity);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1000){
            mViewHolder=new MyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_daily_reminders,parent,false));
            return mViewHolder;
        }else {
            mAddViewHolder=new AddViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_clock_add,parent,false));
            return mAddViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SHX520Alarmclock.SHX520AlarmClockEntity daily_reminders=list.get(position);
        if(daily_reminders.type==1000){
            mViewHolder=(MyViewHolder)holder;
            String hourString = daily_reminders.getH() < 10 ? ("0"+String.valueOf(daily_reminders.getH())) : String.valueOf(daily_reminders.getH());
            String minuteString = daily_reminders.getS() < 10 ? ("0"+String.valueOf(daily_reminders.getS())) : String.valueOf(daily_reminders.getS());
            int mode  = daily_reminders.getMode();
            mViewHolder.tv_time.setText(hourString+":"+minuteString);
            mViewHolder.tv_remark.setVisibility(View.GONE);
            mViewHolder.switch_daily_reminderss.setChecked(mode == 1);
            mViewHolder.switch_daily_reminderss.setOnCheckedChangeListener(this);
            mViewHolder.switch_daily_reminderss.setTag(position);
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.itemClick(position);
                    }
                }
            });
        }else if(daily_reminders.type==2000){
            mAddViewHolder=(AddViewHolder) holder;
            mAddViewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.itemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        int pos = (int) view.getTag();
        SHX520Alarmclock.SHX520AlarmClockEntity daily_reminders = list.get(pos);
        daily_reminders.setMode(isChecked ? 1 : 0);
        ViewEvent event = new ViewEvent();
        event.setClockEntity(daily_reminders);
        event.setMessage(Constant.EVENT_CHANGE_520_CLOCK);
        event.setFlg(pos);
        EventBus.getDefault().post(event);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R2.id.tv_time)
        public TextView tv_time;
        @BindView(R2.id.tv_remark)
        public TextView tv_remark;
        @BindView(R2.id.switch_daily_reminderss)
        public SwitchButton switch_daily_reminderss;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    static class AddViewHolder extends RecyclerView.ViewHolder{

        @BindView(R2.id.clock_add_bt)
        public TextView mAdd;

        public AddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
