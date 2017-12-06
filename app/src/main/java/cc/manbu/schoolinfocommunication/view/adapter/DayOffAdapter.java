package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.Sleave;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

/**
 * Created by manbuAndroid5 on 2017/4/28.
 */

public class DayOffAdapter extends BaseAdapter {
    public List<Sleave>list;
    public Context context;

    public DayOffAdapter(List<Sleave> list, Context context) {
        this.list = list;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_day_off,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Sleave sleave = list.get(position);
        if (sleave != null){
            viewHolder.id_tvTitleContent.setText(sleave.getTitle());
            viewHolder.id_tvResonContent.setText(sleave.getReason());
            int state = sleave.getState();
            switch (state){
                case 0://未审核
                    viewHolder.id_tvState.setText(R.string.text_reading);
                    viewHolder.id_tvState.setTextColor(ContextCompat.getColor(context,R.color.red));
                    break;
                case 1://未通过
                    viewHolder.id_tvState.setText(R.string.text_pass);
                    viewHolder.id_tvState.setTextColor(ContextCompat.getColor(context,R.color.toolbar_text_color));
                    break;
                case 2://通过
                    viewHolder.id_tvState.setText(R.string.text_passed);
                    viewHolder.id_tvState.setTextColor(ContextCompat.getColor(context,R.color.toolbar_text_color));
                    break;
            }
            String formate = "yyyy/MM/dd EE HH:mm:ss";
            Date starDate = sleave.getStartTime();
            Date endDate = sleave.getEndTime();
            viewHolder.id_tvDateContent.setText(DateUtil.format(formate,starDate)+"\n"+ DateUtil.format(formate,endDate));
            viewHolder.id_tvName.setText(sleave.getStuName()+"["+sleave.getDeptName()+"]");
            if (Configs.whichRole == 1){
                viewHolder.id_tvName.setVisibility(View.VISIBLE);
                viewHolder.id_tvSleaveName.setVisibility(View.VISIBLE);
            }else {
                viewHolder.id_tvName.setVisibility(View.GONE);
                viewHolder.id_tvSleaveName.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R2.id.id_tvState)
        public TextView id_tvState;
        @BindView(R2.id.id_tvTitleContent)
        public TextView id_tvTitleContent;
        @BindView(R2.id.id_tvResonContent)
        public TextView id_tvResonContent;
        @BindView(R2.id.id_tvDateContent)
        public TextView id_tvDateContent;
        @BindView(R2.id.id_tvSleaveName)
        public TextView id_tvSleaveName;
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
