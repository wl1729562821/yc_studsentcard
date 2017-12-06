package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.Device_Geography;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.tools.Utils;

/**
 * Created by manbuAndroid5 on 2017/4/21.
 */

public class E_ZoneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public String TAG="E_ZoneListAdapter";

    public Context context;
    public List<Device_Geography> mList;

    public MyViewHolder mViewHolder;
    public AddViewHolder mAddViewHolder;

    public E_ZoneListAdapter(Context context) {
        this.context = context;
        mList=new ArrayList<>();
    }

    public List<Device_Geography> getData(){
        return mList;
    }

    public void setData(List<Device_Geography> list){
        Log.e(TAG,"setData"+mList.size());
        if(mList.size()>0){
            notifyItemRangeRemoved(0,mList.size());
            mList.clear();
        }
        Log.e(TAG,"setData1"+mList.size()+";"+list.size());
        mList.addAll(list);
        mList.add(new Device_Geography(20));
        Log.e(TAG,"setData2"+mList.size());
        notifyDataSetChanged();
    }

    public void setData(Device_Geography data){
        //插入倒数第二个
        mList.add(mList.size()-2,data);
        notifyItemInserted(mList.size()-2);
        //notifyDataSetChanged();
    }

    public void remove(int position){
        if(mList!=null&& mList.size()>0){
            if(mList.size()>(position-1)){
                mList.get(position);
                notifyItemRemoved(position);
                mList.remove(position);
                notifyItemRangeChanged(0, getItemCount());
            }
        }
    }

    public void remove(int position,boolean add){
        List<Device_Geography> list=mList;
        if(mList.size()>0){
            mList.clear();
            notifyDataSetChanged();
            setData(list);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
       return mList==null? 20:mList.get(position).type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==10){
            mViewHolder=new MyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_e_zone,parent,false));
            return mViewHolder;
        }else {
            mAddViewHolder=new AddViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_electricfence_add,parent,false));
            return mAddViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Device_Geography device_geography = mList.get(position);
        int type = device_geography.getType();
        if(device_geography.type==10){
            mViewHolder=(MyViewHolder)holder;
            mViewHolder.id_tvEZoneName.setText(device_geography.getName());
            String string = context.getString(R.string.text_alert_type);
            if (type == 1)
                string = String.format(string,context.getString(R.string.text_alert_out));
            else if (type == 0)
                string = String.format(string,context.getString(R.string.text_alert_in));
            else
                string = String.format(string,context.getString(R.string.text_alert_all));
            mViewHolder.id_tvAlert.setText(string);
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.CurGeography = mList.get(position);
                    ViewEvent event = new ViewEvent();
                    event.setMessage(Constant.EVENT_SHOW_GIS);
                    EventBus.getDefault().post(event);
                }
            });
        }else if(device_geography.type==20){
            mAddViewHolder=(AddViewHolder)holder;
            mAddViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewEvent event=new ViewEvent();
                    event.type=2000;
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R2.id.id_tvEZoneName)
        public TextView id_tvEZoneName;
        @BindView(R2.id.id_tvAlert)
        public TextView id_tvAlert;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class AddViewHolder extends RecyclerView.ViewHolder{

        public AddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
