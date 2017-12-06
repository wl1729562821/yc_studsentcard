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
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.Hw_Homework;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

/**
 * Created by manbuAndroid5 on 2017/4/27.
 */

public class RealeseHomeWorkAdapter extends BaseAdapter {
    public List<Hw_Homework>list;
    public Context context;

    public RealeseHomeWorkAdapter(List<Hw_Homework> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_realse_home_work,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Hw_Homework hw_homework = list.get(position);
        if (hw_homework != null){
            viewHolder.id_tvSubject.setText(hw_homework.getSubjectName());
            viewHolder.id_tvDate.setText(DateUtil.format("yyyy-MM-dd HH:mm",hw_homework.getAddDate()));
            viewHolder.id_tvClass.setText(hw_homework.getDeptName());
        }
        return convertView;
    }
    public class ViewHolder{
        @BindView(R2.id.id_tvSubject)
        public TextView id_tvSubject;
        @BindView(R2.id.id_tvDate)
        public TextView id_tvDate;
        @BindView(R2.id.id_tvClass)
        public TextView id_tvClass;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
