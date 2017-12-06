package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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
import cc.manbu.schoolinfocommunication.bean.SR_Exam;
import cc.manbu.schoolinfocommunication.bean.SR_Score;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.view.adapter.ScoresAdapter;

public class ScoresActivity extends BaseActivityStudent {
    @BindView(R2.id.id_lvScores)
    public ListView id_lvScores;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;

    public List<SR_Score>list = new ArrayList<>();
    public ScoresAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_scores;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        Intent intent = this.getIntent();
        id_tvTitle.setText(R.string.text_home_work_score);
        adapter = new ScoresAdapter(list,this);
        id_lvScores.setAdapter(adapter);
        SR_Exam sr_exam = (SR_Exam) intent.getSerializableExtra("exam");
        if (sr_exam != null){
            getMLoadingDoialog().show();
            getMNetHelper().getScores(sr_exam);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SearchScore".equals(msg)){
            getMLoadingDoialog().dismiss();
            List<SR_Score> datas = event.getSr_scores();
            list.clear();
            list.addAll(datas);
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
            if (list.size() <= 0){
                id_llEmpty.setVisibility(View.VISIBLE);
                id_lvScores.setVisibility(View.GONE);
            }else {
                id_llEmpty.setVisibility(View.GONE);
                id_lvScores.setVisibility(View.VISIBLE);
            }
        }
    }
}
