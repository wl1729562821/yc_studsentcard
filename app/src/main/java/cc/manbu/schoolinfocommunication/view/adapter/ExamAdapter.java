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
import cc.manbu.schoolinfocommunication.bean.SR_Exam;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

/**
 * Created by manbuAndroid5 on 2017/4/27.
 */

public class ExamAdapter extends BaseAdapter {
    public List<SR_Exam>list;
    public Context context;

    public ExamAdapter(List<SR_Exam> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_work,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SR_Exam sr_exam = list.get(position);
        if (sr_exam != null){
            viewHolder.id_tvSubject.setText(sr_exam.getName());
            viewHolder.id_tvDate.setText(DateUtil.format("yyyy-MM-dd",sr_exam.getAddDate()));
        }
        return convertView;
    }
    public class ViewHolder{
        @BindView(R2.id.id_tvSubject)
        public TextView id_tvSubject;
        @BindView(R2.id.id_tvDate)
        public TextView id_tvDate;

        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
