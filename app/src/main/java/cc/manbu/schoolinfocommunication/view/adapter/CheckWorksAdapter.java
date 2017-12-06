package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.SHX002COW;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

/**
 * Created by manbuAndroid5 on 2017/4/25.
 */

public class CheckWorksAdapter extends BaseExpandableListAdapter {
    public Context context;
    public List<String> group;
    public List<List<SHX002COW>> child;

    public CheckWorksAdapter(Context context, List<String> group, List<List<SHX002COW>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group == null ? 0 : group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child == null ? 0 : child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_works_group,parent,false);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (GroupHolder) convertView.getTag();
        }
        String groupName = getGroup(groupPosition).toString();
        holder.id_tvGroupName.setText(groupName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_works_child,parent,false);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ChildHolder) convertView.getTag();
        }
        SHX002COW cow = (SHX002COW) getChild(groupPosition,childPosition);
        if (cow != null){
            int type = cow.getType();
            Date date = cow.getTime();
            String str = cow.getTy();
            String status = context.getResources().getString(R.string.text_states);
            String timestr = context.getResources().getString(R.string.text_time);
            timestr = String.format(timestr, DateUtil.format("yyyy/MM/dd HH:mm",date));
            status = String.format(status,str);
            /*if (type == 1)
                status = String.format(status,context.getResources().getString(R.string.text_enter));
            else if (type == 2)
                status = String.format(status,context.getResources().getString(R.string.text_leave));
            else if (type == 3)
                status = String.format(status,context.getResources().getString(R.string.text_up_car));
            else if (type == 4)
                status = String.format(status,context.getResources().getString(R.string.text_down_car));*/
            holder.id_tvDescribe.setText(status);
            holder.id_tvTime.setText(timestr);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ChildHolder{
        @BindView(R2.id.id_tvDescribe)
        public TextView id_tvDescribe;
        @BindView(R2.id.id_tvTime)
        public TextView id_tvTime;
        public ChildHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
    public class GroupHolder{
        @BindView(R2.id.id_tvGroupName)
        public TextView id_tvGroupName;

        public GroupHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
