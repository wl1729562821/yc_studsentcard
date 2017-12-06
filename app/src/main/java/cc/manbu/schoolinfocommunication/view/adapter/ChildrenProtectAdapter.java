package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.ViewItemBean;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.config.DeviceType;
import cc.manbu.schoolinfocommunication.events.ViewEvent;

/**
 * Created by manbuAndroid5 on 2017/4/19.
 */

public class ChildrenProtectAdapter extends BaseAdapter implements SwitchButton.OnCheckedChangeListener{
    public List<ViewItemBean> list;
    public Context context;

    public ChildrenProtectAdapter(List<ViewItemBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_children_protect,parent,false);
            viewHolder = new ViewHoder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHoder) convertView.getTag();
        }
        ViewItemBean views = list.get(position);
        if (views != null){
            int id = views.getId();
            int state = views.getState();
            int classTimeState = views.getStateClassTime();
            int gpsState = views.getStateGPS();
            viewHolder.id_tvName.setText(context.getResources().getString(id));
            if (Configs.curDeviceType == DeviceType.S520Watch2_without_Wifi ||
                    Configs.curDeviceType == DeviceType.S520Watch2){
                if (id == R.string.text_listener || id == R.string.text_fair_wall){
                    viewHolder.id_tvLine.setVisibility(View.VISIBLE);
                    viewHolder.divider.setVisibility(View.GONE);
                }else {
                    viewHolder.id_tvLine.setVisibility(View.GONE);
                    viewHolder.divider.setVisibility(View.VISIBLE);
                }
            }else if (Configs.curDeviceType == DeviceType.StudentCard){
                if (id == R.string.text_upload_time || id == R.string.text_gps){
                    viewHolder.id_tvLine.setVisibility(View.VISIBLE);
                    viewHolder.divider.setVisibility(View.GONE);
                }else {
                    viewHolder.id_tvLine.setVisibility(View.GONE);
                    viewHolder.divider.setVisibility(View.VISIBLE);
                }
            }else if (Configs.curDeviceType == DeviceType.S582Watch || Configs.curDeviceType == 44){
                if (id == R.string.text_listener || id == R.string.text_lock){
                    viewHolder.id_tvLine.setVisibility(View.VISIBLE);
                    viewHolder.divider.setVisibility(View.GONE);
                }else {
                    viewHolder.id_tvLine.setVisibility(View.GONE);
                    viewHolder.divider.setVisibility(View.VISIBLE);
                }
            }else {
                if (id == R.string.text_listener || id == R.string.text_fair_wall){
                    viewHolder.id_tvLine.setVisibility(View.VISIBLE);
                    viewHolder.divider.setVisibility(View.GONE);
                }else {
                    viewHolder.id_tvLine.setVisibility(View.GONE);
                    viewHolder.divider.setVisibility(View.VISIBLE);
                }
            }

            if (id == R.string.text_fair_wall||id == R.string.text_class_time||
                    id == R.string.text_gps || id == R.string.text_lock){
                viewHolder.id_ivArrow.setVisibility(View.GONE);
                viewHolder.id_switchButton.setVisibility(View.VISIBLE);
                viewHolder.id_switchButton.setOnCheckedChangeListener(this);
                viewHolder.id_switchButton.setTag(id);
                if (id == R.string.text_fair_wall)
                    viewHolder.id_switchButton.setChecked(state == 1);
                else if (id == R.string.text_class_time)
                    viewHolder.id_switchButton.setChecked(classTimeState == 1);
                else if (id == R.string.text_gps)
                    viewHolder.id_switchButton.setChecked(gpsState == 1);
            }else {
                viewHolder.id_ivArrow.setVisibility(View.VISIBLE);
                viewHolder.id_switchButton.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        int id = (int) view.getTag();
        ViewEvent event = new ViewEvent();
        switch (id){
            case R.string.text_fair_wall:
                event.setMessage(Constant.EVENT_FAIR_WALL);
                break;
            case R.string.text_class_time:
                event.setMessage(Constant.EVENT_CLASS_TIME);
                break;
            case R.string.text_gps:
                event.setMessage(Constant.EVENT_GPS_OPEN);
                break;
            case R.string.text_lock:
                event.setMessage(Constant.EVENT_KEY_LOCK);
                break;
        }
        event.setCheck(isChecked);
        EventBus.getDefault().post(event);
    }

    public class ViewHoder{
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_ivArrow)
        public ImageView id_ivArrow;
        @BindView(R2.id.id_switchButton)
        public SwitchButton id_switchButton;
        @BindView(R2.id.id_tvLine)
        public TextView id_tvLine;
        @BindView(R2.id.divider)
        public View divider;

        public ViewHoder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
