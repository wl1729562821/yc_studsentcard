package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
import cc.manbu.schoolinfocommunication.view.adapter.MyClassAdapter;

public class TeacherClassActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_lvClassList)
    public ListView id_lvClassList;

    public MyClassAdapter adapter;
    public List<R_Department>list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_teacher_class;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        R_Users users = Configs.get(Configs.Config.CurUser,R_Users.class);
        if (users != null){
            getMLoadingDoialog().show();
            getMNetHelper().accessDepartmentList(users.getId());
        }
    }

    public void init(){
        id_tvTitle.setText(R.string.text_myclass);
        adapter = new MyClassAdapter(list,this);
        id_lvClassList.setAdapter(adapter);
        id_lvClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getMAtv(),StudentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dep",list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("GetR_DepartmentListByTeacherId".equals(msg)){
            getMLoadingDoialog().dismiss();
            List<R_Department>datas = event.getDepartments();
            list.clear();
            list.addAll(datas);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }
}
