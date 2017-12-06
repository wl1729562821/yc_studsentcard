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
import cc.manbu.schoolinfocommunication.bean.SR_Score;

/**
 * Created by manbuAndroid5 on 2017/4/28.
 */

public class ScoresAdapter extends BaseAdapter {
    public List<SR_Score>list;
    public Context context;

    public ScoresAdapter(List<SR_Score> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scores,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SR_Score sr_score = list.get(position);
        if (sr_score != null){
            viewHolder.id_ExamName.setText(sr_score.getR_SubjectName());
            viewHolder.id_tvScore.setText(String.valueOf(sr_score.getScore()));
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R2.id.id_ExamName)
        public TextView id_ExamName;
        @BindView(R2.id.id_tvScore)
        public TextView id_tvScore;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
