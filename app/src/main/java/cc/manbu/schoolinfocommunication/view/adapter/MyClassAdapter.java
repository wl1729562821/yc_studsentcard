package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.R_Department;

/**
 * Created by manbuAndroid5 on 2017/4/28.
 */

public class MyClassAdapter extends BaseAdapter {
    public List<R_Department>list;
    public Context context;

    public MyClassAdapter(List<R_Department> list, Context context) {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_content,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        R_Department department = list.get(position);
        if (department != null){
            viewHolder.id_tvName.setText(department.getDepName());
            viewHolder.id_tvValue.setVisibility(View.INVISIBLE);
            viewHolder.img.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_tvValue)
        public TextView id_tvValue;
        @BindView(R2.id.id_img)
        public ImageView img;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
