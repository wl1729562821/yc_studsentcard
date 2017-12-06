package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.ContactEntity;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.tools.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manbuAndroid5 on 2017/4/25.
 */

public class ContactsAdapter extends BaseAdapter implements View.OnClickListener{
    public List<ContactEntity> list;
    public Context context;
    public HashSet<String> hashSet = new HashSet<>();
    public String path = "";

    public ContactsAdapter(List<ContactEntity> list, Context context) {
        this.list = list;
        this.context = context;
        hashSet.addAll(Utils.stringList);
        path = Configs.Photo_RCV + Configs.getCurDeviceSerialnumber() + File.separator + "images" + File.separator;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<ContactEntity> list){
        this.list = list;
        hashSet.clear();
        hashSet.addAll(Utils.stringList);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactEntity contact = list.get(position);
        if (contact != null){
            viewHolder.name.setText(contact.getName());
            viewHolder.photo.setImageBitmap(contact.getPhoto());
            viewHolder.number.setText(contact.getNumber());
            String num = contact.getNumber();
            if (hashSet.contains(num)){
                viewHolder.id_tvAdd.setText(R.string.text_added);
                viewHolder.id_tvAdd.setTextColor(ContextCompat.getColor(context,R.color.gray));
                viewHolder.id_tvAdd.setBackgroundResource(R.color.transparent);
                viewHolder.id_tvAdd.setOnClickListener(null);
            }else {
                viewHolder.id_tvAdd.setTag(position);
                viewHolder.id_tvAdd.setText(R.string.text_add);
                viewHolder.id_tvAdd.setTextColor(ContextCompat.getColor(context,R.color.toolbar_text_color));
                viewHolder.id_tvAdd.setBackgroundResource(R.drawable.yellow_3_bg);
                viewHolder.id_tvAdd.setOnClickListener(this);
            }
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Utils.contactEntity = list.get(pos);
        Bitmap bitmap = list.get(pos).getPhoto();
        long potoid = list.get(pos).getPhotoid();
        Utils.position = -2;
        if (bitmap != null && potoid > 0)
            Utils.saveBitmapToFile(bitmap,path+Utils.position+"_head.jpg");
        ViewEvent event = new ViewEvent();
        event.setMessage(Constant.EVENT_EDIT_PHONE_LIST);
        event.setFlg(2);
        EventBus.getDefault().post(event);
    }

    public class ViewHolder {
        @BindView(R2.id.id_tvName)
        public TextView name;
        @BindView(R2.id.id_tvNumber)
        public TextView number;
        @BindView(R2.id.id_tvAdd)
        public TextView id_tvAdd;
        @BindView(R2.id.roundedImageView)
        public CircleImageView photo;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
