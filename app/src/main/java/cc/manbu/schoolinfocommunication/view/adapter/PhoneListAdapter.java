package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.PhoneListBean;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.CameraUtils;
import cc.manbu.schoolinfocommunication.tools.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manbuAndroid5 on 2017/4/24.
 */

public class PhoneListAdapter extends BaseAdapter {
    public Context context;
    public List<PhoneListBean>list;
    public String path = "";
    public PhoneListAdapter(Context context, List<PhoneListBean> list) {
        this.context = context;
        this.list = list;
        path = Configs.Photo_RCV + Configs.getCurDeviceSerialnumber() + File.separator + "images" + File.separator;
        Utils.createDirs(path);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_phone_list,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhoneListBean phoneListBean = list.get(position);
        if (phoneListBean != null){
            viewHolder.id_tvName.setText(phoneListBean.getContactName());
            viewHolder.id_tvNumber.setText(phoneListBean.getContactNumber());
            try {
                Utils.generateImage(phoneListBean.getIconStr(),path + position + "_head.jpg");
                File file = new File(path + position + "_head.jpg");
                Bitmap bitmap = null;
                if (file.exists() && file.length() > 0){
                    if (file.length() > 30000)
                        bitmap = CameraUtils.copressImage(file.getAbsolutePath());
                    else
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    viewHolder.id_ivHead.setImageBitmap(bitmap);
                }else {
                    viewHolder.id_ivHead.setImageResource(R.drawable.gray_head);
                }
                Log.e("PhoneListAdapter","getView "+viewHolder.id_ivHead);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R2.id.profile_image)
        public CircleImageView id_ivHead;
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_tvNumber)
        public TextView id_tvNumber;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
