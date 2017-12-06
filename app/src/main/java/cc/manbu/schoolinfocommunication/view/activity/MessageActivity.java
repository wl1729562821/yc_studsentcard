package cc.manbu.schoolinfocommunication.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.PushUserMsgM;
import cc.manbu.schoolinfocommunication.config.ManbuApplication;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.push.PushMessageService;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

public class MessageActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_tvRight)
    public TextView id_tvRight;
    @BindView(R2.id.listview_msg)
    public ListView listview_msg;
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;

    private String curSerialnumber;
    private PushMessageAdapter mPushMessageAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        curSerialnumber = Configs.getCurDeviceSerialnumber();
        init();
        updateMessage();
        registerReceiver(MessageReceiver, new IntentFilter(PushMessageService.Action_MessageUpdated));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MessageReceiver);
    }
    private void updateMessage() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                DbManager db = x.getDb(ManbuApplication.getInstance().getDaoConfig());
                String curSerialnumber = Configs.getCurDeviceSerialnumber();
                //添加查询条件进行查询
                try {
                    final List<PushUserMsgM> data = db.selector(PushUserMsgM.class).where("From","=",curSerialnumber).
                            and("To","=",curSerialnumber).and("UserId","=",curSerialnumber).findAll();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mPushMessageAdapter == null){
                                mPushMessageAdapter = new PushMessageAdapter();
                                mPushMessageAdapter.setData(data);
                                listview_msg.setAdapter(mPushMessageAdapter);
                            }else{
                                mPushMessageAdapter.setData(data);
                                mPushMessageAdapter.notifyDataSetChanged();
                            }
                            if(data!=null){
                                markMessageHasRead(data);
                                listview_msg.setSelection(data.size() - 1);
                            }
                            if (data == null || data.size() <= 0){
                                id_llEmpty.setVisibility(View.VISIBLE);
                            }else {
                                id_llEmpty.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick(value = {R.id.id_tvRight})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.id_tvRight:
                clearMsg();
                break;
        }
    }
    private void clearMsg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.text_tips);
        builder.setMessage(R.string.title_sure_to_delete);
        builder.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbManager db = x.getDb(ManbuApplication.getInstance().getDaoConfig());
                WhereBuilder b = WhereBuilder.b();
                b.and("From","=",curSerialnumber);
                b.and("To","=",curSerialnumber);
                b.and("UserId","=",curSerialnumber);
                try {
                    db.delete(PushUserMsgM.class, b);
                    updateMessage();
                    dialog.dismiss();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel,null);
        builder.create().show();
    }
    private void markMessageHasRead(List<PushUserMsgM> data) {
        DbManager db = x.getDb(ManbuApplication.getInstance().getDaoConfig());
        if(data!=null){
            WhereBuilder b = WhereBuilder.b();
            b.and("IsRead","=",0);
            KeyValue name = new KeyValue("IsRead",1);
            try {
                db.update(PushUserMsgM.class,b,name);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(){
        id_tvTitle.setText(R.string.text_msg);
        id_tvRight.setText(R.string.clean_up);
       /* toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String receiver = intent.getStringExtra("Receiver");
        if(receiver!=null && curSerialnumber!=null){
            if(receiver.equals(curSerialnumber)){
                updateMessage();
            }
        }
    }

    private BroadcastReceiver MessageReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra("Receiver");
            if(to!=null && curSerialnumber!=null){
                if(curSerialnumber.equals(to)){
                    updateMessage();
                }
            }
        }};

    private class PushMessageAdapter extends BaseAdapter {
        private List<PushUserMsgM> data;
        public void setData(List<PushUserMsgM> data){
            this.data = data;
        }

        @Override
        public int getCount() {
            return data==null?0:data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PushUserMsgM msg = (PushUserMsgM) getItem(position);
            if(convertView == null){
                convertView = View.inflate(getMAtv(), R.layout.item_message, null);
            }
            ImageView message_receiver = (ImageView) convertView.findViewById(R.id.message_receiver);
            TextView message_content = (TextView) convertView.findViewById(R.id.message_content);
            TextView message_time = (TextView) convertView.findViewById(R.id.message_time);
            message_content.setText(msg.getContext());
            message_time.setText(DateUtil.format(msg.getCreateTime()));
            return convertView;
        }

    }
}
