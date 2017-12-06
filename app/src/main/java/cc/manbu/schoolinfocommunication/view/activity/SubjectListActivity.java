package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Subject;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.view.adapter.SubjectAdapter;

public class SubjectListActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_lvSubject)
    public ListView id_lvSubject;

    @Override
    public int getLayoutId() {
        return R.layout.activity_subject_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        R_Users rUsers = Configs.get(Configs.Config.CurUser,R_Users.class);
        if (rUsers != null){
            getMLoadingDoialog().show();
            getMNetHelper().accessSubjects(rUsers.getId());
        }
    }

    public void init(){
        id_tvTitle.setText(R.string.text_subject);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("GetR_SubjectListByTeacherId".equals(msg)){
            getMLoadingDoialog().dismiss();
            List<R_Subject> list = event.getSubjects();
            SubjectAdapter adapter = new SubjectAdapter(list,this);
            id_lvSubject.setAdapter(adapter);
        }
    }
}
