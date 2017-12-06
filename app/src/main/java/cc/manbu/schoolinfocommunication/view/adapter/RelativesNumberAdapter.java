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
import cc.manbu.schoolinfocommunication.bean.MobileCart;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cn.yc.model.listener.BaseOnItemListener;

/**
 * Created by manbuAndroid5 on 2017/3/3.
 */

public class RelativesNumberAdapter extends BaseAdapter implements View.OnClickListener{
    public Context context;
    public List<MobileCart> mobileCartList;
    private BaseOnItemListener<Integer> mListener;
    public RelativesNumberAdapter(Context context, List<MobileCart> mobileCartList,BaseOnItemListener<Integer> listener){
        this.context = context;
        this.mobileCartList = mobileCartList;
        mListener=listener;
    }
    @Override
    public int getCount() {
        return mobileCartList == null ? 0 : mobileCartList.size();
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
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_relaves,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MobileCart mobileCart = mobileCartList.get(position);
        if (mobileCart != null){
            viewHolder.id_tvName.setText(mobileCart.getName());
            viewHolder.id_tvNum.setText(String.format(context.getResources().getString(R.string.text_cell_number),mobileCart.getTelNo()));
            viewHolder.id_tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.itemClick(position);
                    }
                }
            });
            viewHolder.id_tvSubmit.setTag(position);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();

    }

    public class ViewHolder{
        @BindView(R2.id.id_ivHead)
        public ImageView id_ivHead;
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_tvNum)
        public TextView id_tvNum;
        @BindView(R2.id.id_tvSubmit)
        public TextView id_tvSubmit;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
