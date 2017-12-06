package cc.manbu.schoolinfocommunication.view.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.manbu.schoolinfocommunication.bean.Device;
import cc.manbu.schoolinfocommunication.bean.MobileCart;
import cc.manbu.schoolinfocommunication.bean.SHX007Device_Config;
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config;
import cc.manbu.schoolinfocommunication.config.API;
import cc.manbu.schoolinfocommunication.config.DeviceType;
import cc.manbu.schoolinfocommunication.httputils.MyCallBack;
import cc.manbu.schoolinfocommunication.tools.XUtil;
import cc.manbu.schoolinfocommunication.view.adapter.RelativesNumberAdapter;
import cn.yc.base.view.custom.percent.PercentLinearLayout;
import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.R2;
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent;
import cc.manbu.schoolinfocommunication.events.ViewEvent;
import cc.manbu.schoolinfocommunication.config.Constant;
import cc.manbu.schoolinfocommunication.events.ResposeEvent;
import cc.manbu.schoolinfocommunication.config.Configs;
import cn.yc.model.listener.BaseOnItemListener;

public class RelativesActivity extends BaseActivityStudent {

    private List<MobileCart> mobileCartList = new ArrayList<>();
    @BindView(R2.id.id_lvRelativesNum)
    public ListView id_lvRelativesNum;
    @BindView(R2.id.id_refreshLayout)
    public SwipeRefreshLayout id_refreshLayout;
    @BindView(R2.id.id_tvTitle)
    public TextView id_tvTitle;
    @BindView(R2.id.id_llEmpty)
    public LinearLayout id_llEmpty;
    @BindView(R2.id.activity_relatives)
    public RelativeLayout activity_relatives;

    private RelativesNumberAdapter adapter;
    private String[] numbers = {"", "", "", "", ""};

    @Override
    public int getLayoutId() {
        return R.layout.activity_relatives;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        getMLoadingDoialog().show();
        getMNetHelper().getDeviceDetial();
        init();
        id_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMNetHelper().getDeviceDetial();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void init() {
        adapter = new RelativesNumberAdapter(this, mobileCartList,
                new BaseOnItemListener<Integer>() {
                    @Override
                    public void itemClick(@Nullable final Integer position) {
                        final String oldNum = numbers[position];
                        final String name = mobileCartList.get(position).getName();
                        Log.e("Activity","onEventMainThread "+position+":"+oldNum+";"+name);
                        View pwContentView = View.inflate(getMAtv(), R.layout.pw_ed, null);
                        TextView nameTv = (TextView) pwContentView.findViewById(R.id.pw_ed_name);
                        final EditText ed = (EditText) pwContentView.findViewById(R.id.pw_ed_ed);
                        TextView ok =(TextView) pwContentView.findViewById(R.id.pw_ed_ok);
                        cancel =(TextView) pwContentView.findViewById(R.id.pw_ed_cancel);
                        int width = getDisplayWidth();
                        Log.e("ReLativesActivity", "show " + width);
                        PercentLinearLayout.LayoutParams lp = new PercentLinearLayout.LayoutParams((int) (width * 0.88),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        pwContentView.setLayoutParams(lp);
                        mDialog = new AlertDialog.Builder(getMAtv()).setView(pwContentView).create();
                        mDialog.setCancelable(true);
                        if (!TextUtils.isEmpty(oldNum)) {
                            ed.setText(formatPhone(" ", oldNum));
                        }
                        nameTv.setText(name);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String number = ed.getText().toString();
                                number = number.replaceAll(" ", "");
                                numbers[position] = number;
                                MobileCart mobileCart = mobileCartList.get(position);
                                mobileCart.setTelNo(number);
                                mobileCartList.set(position, mobileCart);
                                Log.e("Activity","提交 "+number +";"+position+";"+mobileCart);
                                setRelativesNumber();
                            }
                        });
                        mDialog.show();
                    }
                });
        id_lvRelativesNum.setAdapter(adapter);
        id_refreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange), getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.red));
        id_refreshLayout.setProgressViewEndTarget(true, 120);//设置距离顶端的距离

        id_tvTitle.setText(R.string.text_kinship);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private AlertDialog mDialog;
    private TextView ok;
    private TextView cancel;
    private TextView name;
    private EditText ed;
    private int position=0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResposeEvent event) {
        String msg = event.getMessage();
        if ("GetDeviceDetial".equals(msg)) {
            Device d = event.getDevice();
            if (Configs.curDeviceType == DeviceType.StudentCard) {
                SHX007Device_Config config = d.getSHX007Device_Config();
                if (config != null) {
                    mobileCartList.clear();
                    MobileCart mobileCartNo1 = new MobileCart();
                    MobileCart mobileCartNo2 = new MobileCart();
                    MobileCart mobileCartNo3 = new MobileCart();
                    MobileCart mobileCartSOSNo = new MobileCart();
                    MobileCart mobileCartListenNo = new MobileCart();
                    mobileCartNo1.setTelNo(config.getKey1CallNo());
                    mobileCartNo1.setName(getResources().getString(R.string.dad));
                    mobileCartNo2.setTelNo(config.getKey2CallNo());
                    mobileCartNo2.setName(getResources().getString(R.string.mom));
                    mobileCartNo3.setTelNo(config.getKey3CallNo());
                    mobileCartNo3.setName(getResources().getString(R.string.sibling));
                    mobileCartSOSNo.setTelNo(config.getKeySOSCallNo());
                    mobileCartSOSNo.setName(getResources().getString(R.string.sos));
                    mobileCartListenNo.setTelNo(config.getListenNum());
                    mobileCartListenNo.setName(getResources().getString(R.string.listen));
                    mobileCartList.add(mobileCartNo1);
                    mobileCartList.add(mobileCartNo2);
                    mobileCartList.add(mobileCartNo3);
                    mobileCartList.add(mobileCartSOSNo);
                    mobileCartList.add(mobileCartListenNo);
                    numbers[0] = config.getKey1CallNo();
                    numbers[1] = config.getKey2CallNo();
                    numbers[2] = config.getKey3CallNo();
                    numbers[3] = config.getKeySOSCallNo();
                    numbers[4] = config.getListenNum();
                }
            } else {
                SHX520Device_Config config = d.getSHX520Device_Config();
                if (config != null) {
                    mobileCartList.clear();
                    MobileCart mobileCartNo1 = new MobileCart();
                    MobileCart mobileCartNo2 = new MobileCart();
                    MobileCart mobileCartSOSNo = new MobileCart();
                    MobileCart mobileCartListenNo = new MobileCart();
                    mobileCartNo1.setTelNo(config.getButtonNo1());
                    mobileCartNo1.setName(getResources().getString(R.string.dad));
                    mobileCartNo2.setTelNo(config.getButtonNo2());
                    mobileCartNo2.setName(getResources().getString(R.string.mom));
                    mobileCartSOSNo.setTelNo(config.getSOSNo());
                    mobileCartSOSNo.setName(getResources().getString(R.string.sos));
                    mobileCartListenNo.setTelNo(config.getListenNo());
                    mobileCartListenNo.setName(getResources().getString(R.string.listen));
                    mobileCartList.add(mobileCartNo1);
                    mobileCartList.add(mobileCartNo2);
                    mobileCartList.add(mobileCartSOSNo);
                    //mobileCartList.add(mobileCartListenNo);
                    numbers[0] = config.getButtonNo1();
                    numbers[1] = config.getButtonNo2();
                    numbers[2] = config.getSOSNo();
                    numbers[3] = config.getListenNo();
                }
            }
            if (adapter != null) adapter.notifyDataSetChanged();
            getMLoadingDoialog().dismiss();
            id_refreshLayout.setRefreshing(false);
            if (mobileCartList.size() <= 0) {
                id_llEmpty.setVisibility(View.VISIBLE);
                id_refreshLayout.setVisibility(View.GONE);
            } else {
                id_llEmpty.setVisibility(View.GONE);
                id_refreshLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private TextWatcher mWatcher = new TextWatcher() {
        private boolean isAdd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isAdd = count == 1;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isAdd) {
                String str = s.toString();
                if (str.length() == 3 || str.length() == 8) {
                    s.insert(str.length(), " ");
                }
            }
        }
    };

    private String formatPhone(String space, String phone) {
        if (!TextUtils.isEmpty(phone) && phone.matches("1[\\d]{10}")) {
            return phone.substring(0, 3) + space + phone.substring(3, 7) + space
                    + phone.substring(7);
        }
        return phone;
    }

    private void setRelativesNumber() {
        String url;
        String params;
        if (Configs.curDeviceType == DeviceType.StudentCard) {
            url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX007BatchSetSOSNum);
            String jsonParam = "{'Serialnumber':'%s','KeySOSSMSNo':'%s','Key1SMSNo':'%s','Key2SMSNo':'%s','Key3SMSNo':'%s','Listen':'%s'}";
            jsonParam = String.format(jsonParam, Configs.getCurDeviceSerialnumber(), numbers[3], numbers[0], numbers[1], numbers[2], numbers[4]);
            params = jsonParam;
        } else {
            url = Configs.STUDENT_DOMAIN + Configs.ADDRESS_A + API.getApi(API.SHX520SetManyPhoneConfig);
            String jsonParam = "{'Serialnumber':'%s','ButtonNo1':'%s','ButtonNo2':'%s','SOSNo':'%s','ListenNo':'%s'}";
            jsonParam = String.format(jsonParam, Configs.getCurDeviceSerialnumber(), numbers[0], numbers[1], numbers[2], numbers[3]);
            params = jsonParam;
        }
        Log.e("Activity","提交亲情号码 "+params+";"+numbers.toString());
        getMLoadingDoialog().show();
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                mDialog.dismiss();
                JSONObject resultJSON = null;// 获取ModelUser类型的JSON对象
                try {
                    LogUtil.e("result===" + result);
                    resultJSON = new JSONObject(result);
                    String r = (String) resultJSON.opt("d");
                    if ("命令提交成功".equals(r) || "".equals(r)) {
                        if (adapter != null) adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                getMLoadingDoialog().dismiss();
                id_refreshLayout.setRefreshing(false);
                mDialog.dismiss();
                Toast.makeText(RelativesActivity.this,"提交失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                getMLoadingDoialog().dismiss();
                mDialog.dismiss();
                id_refreshLayout.setRefreshing(false);
            }
        });
    }
}
