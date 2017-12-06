package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.NameListBean;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;

/**
 * Created by manbuAndroid5 on 2017/4/20.
 */

public class NameListAdaper extends BaseAdapter implements View.OnClickListener{
    public Context context;
    public List<NameListBean> list;

    public NameListAdaper(Context context, List<NameListBean> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_login_name,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NameListBean names = list.get(position);
        if (names != null){
            viewHolder.id_tvName.setText(names.getName());
            viewHolder.id_ivClear.setOnClickListener(this);
            viewHolder.id_ivClear.setTag(position);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        ViewEvent event = new ViewEvent();
        event.setMessage(Constant.EVENT_FREASH_LIST_LOGIN_NAME);
        event.setFlg(pos);
        EventBus.getDefault().post(event);
    }

    public class ViewHolder{
        @BindView(R2.id.id_ivClear)
        public ImageView id_ivClear;
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
