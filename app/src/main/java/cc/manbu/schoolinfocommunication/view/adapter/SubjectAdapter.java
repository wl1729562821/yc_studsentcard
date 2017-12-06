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
import cc.manbu.schoolinfocommunication.bean.R_Subject;

/**
 * Created by manbuAndroid5 on 2017/4/28.
 */

public class SubjectAdapter extends BaseAdapter {
    public List<R_Subject>list;
    public Context context;

    public SubjectAdapter(List<R_Subject> list, Context context) {
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
        R_Subject subject = list.get(position);
        if (subject != null){
            viewHolder.id_tvName.setText(subject.DepName);
            viewHolder.id_tvValue.setText(subject.getName());
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_tvValue)
        public TextView id_tvValue;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
