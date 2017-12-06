package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Department;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.view.adapter.StudentListAdapter;

public class StudentsActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_lvStudents)
    public ListView id_lvStudents;

    public StudentListAdapter adapter;
    public List<R_Users> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_students;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        R_Department department = (R_Department) bundle.getSerializable("dep");
        if (department != null) {
            getMLoadingDoialog().show();
            getMNetHelper().accessStudents(department.getId());
        }

        id_tvTitle.setText(R.string.text_myclass);
        adapter = new StudentListAdapter(getMAtv(), list);
        id_lvStudents.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event) {
        String msg = event.getMessage();
        if ("GetR_StudentListByDeptId".equals(msg)) {
            getMLoadingDoialog().dismiss();
            List<R_Users> datas = event.getrUsersList();
            list.clear();
            list.addAll(datas);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }
}
