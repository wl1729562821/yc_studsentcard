package cc.manbu.schoolinfocommunication.view.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.config.API;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.httputils.MyCallBack;
import cc.manbu.schoolinfocommunication.tools.DateUtil;
import cc.manbu.schoolinfocommunication.tools.XUtil;

public class FeebackActivity extends BaseActivityStudent {
    @BindView(R2.id.id_edFeedback)
    public EditText id_edFeedback;
    @BindView(R2.id.id_tvSubmit)
    public TextView id_tvSubmit;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feeback;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.id_tvSubmit)
    public void onClick(){
        String feeback = id_edFeedback.getText().toString();
        if (TextUtils.isEmpty(feeback)){
            showMessage(getResources().getString(R.string.feedback_content_hint));
            return;
        }
        getMLoadingDoialog().show();
        feedBack();
    }
    private void init(){
        id_tvTitle.setText(R.string.text_feedback);
    }
    private void feedBack(){
        String url = Configs.STUDENT_DOMAIN  + Configs.ADDRESS_A  + API.getApi(API.SaveFeedBack);
        PackageManager pm =getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String contact = "";
        String content = id_edFeedback.getText().toString();
        String desc = "<![CDATA[\n[OS:Android;packageName:%s;versionCode:%s;versionName:%s;androidSdk:%s]\n]]>";
        desc = String.format(desc,getPackageName(),pi.versionCode,pi.versionName, Build.VERSION.SDK_INT);
        R_Users user = Configs.get(Configs.Config.CurUser,R_Users.class);
        final String jsonStr = String.format("{'FeedBack':{'Contact':'%s','Content':'%s','Id':'%s','LoginName':'%s','FeedBackDate':'%s'}}",contact,desc+content,
                UUID.randomUUID().toString(),user.getLoginName(), DateUtil.format("yyyy-MM-dd HH:mm:ss", new Date(System.currentTimeMillis())));
        XUtil.Post(url,jsonStr,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                if (!TextUtils.isEmpty(result)){
                    id_edFeedback.getText().clear();
                    new AlertDialog.Builder(getMAtv()).setTitle(R.string.text_tips).setMessage(R.string.feedback_commite).setCancelable(true).
                            setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create().show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                getMLoadingDoialog().dismiss();
                showMessage(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                getMLoadingDoialog().dismiss();
            }
        });
    }
}
