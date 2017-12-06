package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
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
import butterknife.OnItemClick;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.Hw_Homework;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.view.adapter.HomeWorkAdapter;

public class HomeWorkActivity extends BaseActivityStudent implements AbsListView.OnScrollListener {
    @BindView(R2.id.id_refreshLayout)
    public SwipeRefreshLayout id_refreshLayout;
    @BindView(R2.id.id_lvHomeWork)
    public ListView id_lvHomeWork;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;

    private HomeWorkAdapter adapter;
    private List<Hw_Homework>list = new ArrayList<>();
    private boolean isLoadMore;
    private int page = 1;//页数
    private int count = 20;//每页的内容量
    private int lastItemIndex = 0;   //最后的可视项索引

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_work;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
        getMLoadingDoialog().show();
        getMNetHelper().homeWorkNotice(page,count);
    }

    private void init(){
        id_tvTitle.setText(R.string.text_home_work_notice);
        id_refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid);
        id_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                getMNetHelper().homeWorkNotice(page,count);
            }
        });
        id_refreshLayout.setProgressViewEndTarget(true,120);//设置距离顶端的距离

        adapter = new HomeWorkAdapter(list,this);
        id_lvHomeWork.setAdapter(adapter);
        id_lvHomeWork.setOnScrollListener(this);
        id_lvHomeWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getMAtv(),HomeWorkDetailActivity.class);
                intent.putExtra("homework",list.get(position));
                startActivity(intent);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("GetUserHw_Homework".equals(msg)){
            getMLoadingDoialog().dismiss();
            id_refreshLayout.setRefreshing(false);
            List<Hw_Homework> datas = event.getHw_homeworks();
            if (!isLoadMore){
                page = 1;
                list.clear();
            }
            list.addAll(datas);
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
            isLoadMore = true;
            if (datas.size() < count){
                isLoadMore = false;
                page = 1;
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastItemIndex == adapter.getCount() - 1 && isLoadMore) {
            //加载数据代码
            page++;
            getMNetHelper().homeWorkNotice(page,count);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItemIndex = firstVisibleItem + visibleItemCount - 1;
    }
}
