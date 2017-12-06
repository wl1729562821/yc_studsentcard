package cc.manbu.schoolinfocommunication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.bean.Hw_Homework;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cc.manbu.schoolinfocommunication.tools.DateUtil;

public class HomeWorkDetailActivity extends BaseActivityStudent {
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.home_work_detail_title)
    public TextView home_work_detail_title;
    @BindView(R2.id.home_work_detail_time)
    public TextView home_work_detail_time;
    @BindView(R2.id.home_work_detail_content)
    public WebView home_work_detail_content;

    private String html = "<html><body>content<body></html>";
    public  String baseUrl = "http://"+ Configs.STUDENT_DOMAIN;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_work_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        id_tvTitle.setText(R.string.text_home_work_notice);
        /*toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        home_work_detail_content.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        home_work_detail_content.getSettings().setSupportZoom(true);
        home_work_detail_content.getSettings().setJavaScriptEnabled(true);
        try {
            Method m = WebSettings.class.getMethod("setUseWebViewBackgroundForOverscrollBackground", boolean.class);
            if(m!=null){
                m.invoke(home_work_detail_content.getSettings(), true);
            }
            home_work_detail_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            Intent intent = this.getIntent();
            Hw_Homework hw =(Hw_Homework) intent.getSerializableExtra("homework");
            String context = hw.getContext();
            context = context.startsWith("<p>") && context.endsWith("<\\/p>")?context:"<p>"+context+"</p>";
            context = html.replace("content", context);
            home_work_detail_content.loadDataWithBaseURL(baseUrl, context, "text/html", "utf-8", "");
            home_work_detail_title.setText(hw.getTitle());
            home_work_detail_time.setText(DateUtil.format("yyyy-MM-dd HH:mm:ss", hw.getAddDate()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
