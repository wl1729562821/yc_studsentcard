package cc.manbu.schoolinfocommunication.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.httputils.MyCallBack;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by manbuAndroid5 on 2017/4/24.
 */

public class StudentListAdapter extends BaseAdapter {
    public Context context;
    public List<R_Users>list;
    public ViewHolder viewHolder;
    public StudentListAdapter(Context context, List<R_Users> list) {
        this.context = context;
        this.list = list;
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
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_phone_list,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        R_Users rUsers = list.get(position);
        if (rUsers != null){
            viewHolder.id_tvName.setText(rUsers.getUserName());
            viewHolder.id_tvNumber.setText(rUsers.getSex());
            ImageOptions options = new ImageOptions.Builder().setFadeIn(true).setCircular(true).build(); //淡入效果
            //ImageOptions.Builder()的一些其他属性：
            //.setCircular(true) //设置图片显示为圆形
            //.setSquare(true) //设置图片显示为正方形
            //setCrop(true).setSize(200,200) //设置大小
            //.setAnimation(animation) //设置动画
            //.setFailureDrawable(Drawable failureDrawable) //设置加载失败的动画
            //.setFailureDrawableId(int failureDrawable) //以资源id设置加载失败的动画
            //.setLoadingDrawable(Drawable loadingDrawable) //设置加载中的动画
            //.setLoadingDrawableId(int loadingDrawable) //以资源id设置加载中的动画
            //.setIgnoreGif(false) //忽略Gif图片
            //.setParamsBuilder(ParamsBuilder paramsBuilder) //在网络请求中添加一些参数
            //.setRaduis(int raduis) //设置拐角弧度
            //.setUseMemCache(true) //设置使用MemCache，默认true
            x.image().loadDrawable(rUsers.getU_ImageUrl(),options,new MyCallBack<Drawable>(){
                @Override
                public void onSuccess(Drawable result) {
                    super.onSuccess(result);
                    if (result != null){
                        viewHolder.profile_image.setImageDrawable(result);
                    }else {
                        viewHolder.profile_image.setImageResource(R.drawable.gray_head);
                    }
                }
            });
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.profile_image)
        public CircleImageView profile_image;
        @BindView(R2.id.id_tvName)
        public TextView id_tvName;
        @BindView(R2.id.id_tvNumber)
        public TextView id_tvNumber;
        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
