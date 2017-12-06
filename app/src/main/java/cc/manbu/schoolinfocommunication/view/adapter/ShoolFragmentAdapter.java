package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.ViewItemBean;

/**
 * Created by manbuAndroid5 on 2017/4/26.
 */

public class ShoolFragmentAdapter extends BaseAdapter {
    public Context context;
    public List<ViewItemBean>list;

    public ShoolFragmentAdapter(Context context, List<ViewItemBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_shool,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewItemBean viewItemBean = list.get(position);
        if (viewItemBean != null){
            viewHolder.id_tvName.setText(viewItemBean.getName());
        }
        return convertView;
    }
    public class ViewHolder{
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
