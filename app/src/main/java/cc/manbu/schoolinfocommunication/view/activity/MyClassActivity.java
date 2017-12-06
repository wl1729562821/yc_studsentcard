package cc.manbu.schoolinfocommunication.view.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.bean.Curriculum;
import cc.manbu.schoolinfocommunication.tools.JSONHelper;
import cc.manbu.schoolinfocommunication.tools.ScreenUtils;
import cn.yc.base.view.custom.percent.PercentLinearLayout;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
public class MyClassActivity extends BaseActivityStudent {
    @BindView(R2.id.linearLayout_syllabus)
    public LinearLayout linearLayout_syllabus;
    @BindView(R2.id.id_line)
    public View id_line;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;

    private TextView[] lessons = new TextView[10];
    private LinearLayout[] linearLayouts = new LinearLayout[10];
    private List<Curriculum> mClassInfoEntityList = new ArrayList<>();
    private boolean isDraw = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_class;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        lessons[0] = (TextView)findViewById(R.id.tv_lesson0);
        lessons[1] = (TextView)findViewById(R.id.tv_lesson1);
        lessons[2] = (TextView)findViewById(R.id.tv_lesson2);
        lessons[3] = (TextView)findViewById(R.id.tv_lesson3);
        lessons[4] = (TextView)findViewById(R.id.tv_lesson4);
        lessons[5] = (TextView)findViewById(R.id.tv_lesson5);
        lessons[6] = (TextView)findViewById(R.id.tv_lesson6);
        lessons[7] = (TextView)findViewById(R.id.tv_lesson7);
        lessons[8] = (TextView)findViewById(R.id.tv_lesson8);
        lessons[9] = (TextView)findViewById(R.id.tv_lesson9);
        id_tvTitle.setText(R.string.text_my_class);
        linearLayout_syllabus.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isDraw){
                    isDraw = false;
                    int w = linearLayout_syllabus.getWidth();
                    int h1 = id_line.getHeight();
                    int h = lessons[0].getHeight() + h1;
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    tvParams.topMargin = 6;
                    tvParams.leftMargin = 3;
                    tvParams.rightMargin = 3;
                    tvParams.bottomMargin = 6;
                    LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(w / 7,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    childParams.gravity = Gravity.CENTER;
                    for (int i = 0; i < lessons.length; i++) {
                        TextView tv = lessons[i];
                        tv.setText(String.valueOf(i + 1));
                        tv.setTag(i);
                        LinearLayout ll = new LinearLayout(getMAtv());
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setLayoutParams(parms);
                        for (int j = 0; j < 7; j++) {
                            TextView tvClass = new TextView(getMAtv());
                            LinearLayout childLayout = new LinearLayout(getMAtv());
                            tvClass.setText("");
                            tvClass.setTextSize(ScreenUtils.sp2px(getMAtv(),10));
                            tvClass.setTextColor(ContextCompat.getColor(getMAtv(),R.color.white));
                            tvClass.setGravity(Gravity.CENTER);
                            tvClass.setLayoutParams(tvParams);
                            childLayout.setLayoutParams(childParams);
                            childLayout.addView(tvClass);
                            ll.addView(childLayout);
                        }
                        linearLayouts[i] = ll;
                        linearLayout_syllabus.addView(ll);
                    }
                    getMLoadingDoialog().show();
                    if (Configs.whichRole == 0)
                        getMNetHelper().accessCurriculums();
                    else
                        getMNetHelper().accessTeacherClass();
                }
                if (linearLayout_syllabus.getViewTreeObserver().isAlive()){
                    linearLayout_syllabus.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }
    private int getClassId(String clsId){
        int id = 0;
        if ("第一节".equals(clsId))
            return 1;
        else if ("第二节".equals(clsId))
            return 2;
        else if ("第三节".equals(clsId))
            return 3;
        else if ("第四节".equals(clsId))
            return 4;
        else if("第五节".equals(clsId))
            return 5;
        else if ("第六节".equals(clsId))
            return 6;
        else if ("第七节".equals(clsId))
            return 7;
        else if ("第八节".equals(clsId))
            return 8;
        else if ("第九节".equals(clsId))
            return 9;
        else if ("第十节".equals(clsId))
            return 10;
        return id;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event){
        String msg = event.getMessage();
        if ("SearchCurriculumlis".equals(msg) || "GetTeacherCurriculum".equals(msg)){
            getMLoadingDoialog().dismiss();
            String reslut = event.getContent();
            try {
                JSONObject resultJSON = new JSONObject(reslut);// 获取ModelUser类型的JSON对象
                JSONArray ja = resultJSON.optJSONArray("d");
                List<List<Curriculum>> data = new ArrayList<>();
                if(ja!=null){
                    for(int i=0;i<ja.length();i++){
                        JSONArray cs = ja.optJSONArray(i);
                        List<Curriculum> list = (List<Curriculum>) JSONHelper.parseCollection(cs, ArrayList.class, Curriculum.class);
                        data.add(list);
                    }
                }
                mClassInfoEntityList.clear();
                for (int i = 0; i < data.size(); i++) {
                    mClassInfoEntityList.addAll(data.get(i));
                }
                for (int i = 0; i < mClassInfoEntityList.size(); i++) {
                    Curriculum c = mClassInfoEntityList.get(i);
                    int y = getClassId(c.getWorkandrestName()) -1;
                    int x = c.getWeekCode();
                    LinearLayout ll;
                    if (x == 0)
                        ll = (LinearLayout) linearLayouts[y].getChildAt(6);
                    else
                        ll = (LinearLayout) linearLayouts[y].getChildAt(x - 1);
                    String clsName = c.getSubjectName();
                    TextView tv = (TextView) ll.getChildAt(0);
                    tv.setText(clsName);
                    tv.setTag(c);
                    switch (clsName){
                        case "数学":
                            tv.setBackgroundResource(R.drawable.red_bg);
                            break;
                        case "语文":
                            tv.setBackgroundResource(R.drawable.royalblue_bg);
                            break;
                        case "物理":
                            tv.setBackgroundResource(R.drawable.violet_bg);
                            break;
                        case "化学":
                            tv.setBackgroundResource(R.drawable.firebrick_bg);
                            break;
                        case "英语":
                            tv.setBackgroundResource(R.drawable.lawngreen_bg);
                            break;
                        default:
                            tv.setBackgroundResource(R.drawable.theme_bg);
                            break;
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Curriculum curriculum = (Curriculum) v.getTag();
                            showCourseDialog(curriculum);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void showCourseDialog(Curriculum c){
        View view = LayoutInflater.from(getMAtv()).inflate(R.layout.layout_dialog_course,null);
        int width=getDisplayWidth();
        PercentLinearLayout.LayoutParams lp=new PercentLinearLayout.LayoutParams(
                (int)(width*0.94), PercentLinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        TextView id_tvCourse = (TextView) view.findViewById(R2.id.id_tvCourse);
        TextView id_tvClass = (TextView) view.findViewById(R2.id.id_tvClass);
        TextView id_tvTime = (TextView) view.findViewById(R2.id.id_tvTime);
        TextView id_tvRemark = (TextView) view.findViewById(R2.id.id_tvRemark);
        if (c != null){
            id_tvCourse.setText(c.getSubjectName());
            id_tvClass.setText(c.getDepName());
            id_tvTime.setText(c.getStartTime()+"-"+c.getEndTime()+"("+c.getWorkandrestName()+")");
            id_tvRemark.setText(c.getRemark());
            if(c.getRemark()==null ||c.getRemark().trim().length()<=0){
                id_tvRemark.setVisibility(View.GONE);
            }
        }
        final AlertDialog builder = new AlertDialog.Builder(getMAtv()).setView(view).create();
        builder.setCancelable(true);
        view.findViewById(R2.id.id_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        //builder.setPositiveButton(R.string.text_ok,null);
        builder.setView(view);
        builder.show();
    }
}
