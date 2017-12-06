package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.bean.SR_Exam;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.R_Users;
import cc.manbu.schoolinfocommunication.view.adapter.ExamAdapter;

public class ExamActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_refreshLayout)
    public SwipeRefreshLayout id_refreshLayout;
    @BindView(R2.id.id_lvExam)
    public ListView id_lvExam;
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;

    private ExamAdapter adapter;
    private List<SR_Exam> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_exam;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        getMLoadingDoialog().show();
        getMNetHelper().getExam(1,20);
    }

    private void init(){
        id_tvTitle.setText(R.string.text_home_work_score);
        id_refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid);
        id_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMNetHelper().getExam(1,20);
            }
        });
        id_refreshLayout.setProgressViewEndTarget(true,120);//设置距离顶端的距离

        adapter = new ExamAdapter(list,this);
        id_lvExam.setAdapter(adapter);
        id_lvExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getMAtv(),ScoresActivity.class);
                intent.putExtra("exam",list.get(position));
                startActivity(intent);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("GetCurSR_Exam".equals(msg)){
            getMLoadingDoialog().dismiss();
            id_refreshLayout.setRefreshing(false);
            List<SR_Exam> datas = event.getSr_exams();
            list.clear();
            list.addAll(datas);
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
            if (list.size() <= 0){
                id_llEmpty.setVisibility(View.VISIBLE);
                id_refreshLayout.setVisibility(View.GONE);
            }else {
                id_llEmpty.setVisibility(View.GONE);
                id_refreshLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
