package cn.yc.student.view.fragment


import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.tools.DateUtil

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.Date
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import kotlinx.android.synthetic.main.fragment_add_sleave.*
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class AddSleaveFragment : BaseFragmentStudent(), CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {
    private var rootView: View? = null
    private var isStart = true
    private var date: String? = null
    private var time: String? = null
    private var startString: String? = null
    private var endString: String? = null
    private var mDialogYearMonthDay: TimePickerDialog?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_sleave
    }

    override fun initView() {
        registerEventbus(this)
        val tenYears = 10L * 365 * 1000 * 60 * 60 * 24L
        mDialogYearMonthDay = TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack { timePickerView, millseconds ->
                    val d = Date(millseconds)
                    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val text =sf.format(d)
                    Log.e(TAG,"onDateSet $text")
                    if(isStart){
                        startString = text
                        id_tvStartTime!!.text = startString
                    }else{
                        endString = text
                        id_tvEndTime!!.text = endString
                    }
                }
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setThemeColor(resources.getColor(R.color.coral))
                .setToolBarTextColor(resources.getColor(R.color.white))
                .build()
        id_tvStartTime?.setOnClickListener {
            isStart = true
            setDateAndTime()
        }
        id_tvEndTime?.setOnClickListener {
            isStart = false
            setDateAndTime()
        }
        id_tvApply?.setOnClickListener {
            val title = id_edTitle!!.text.toString()
            val reason = id_edReason!!.text.toString()
            if (TextUtils.isEmpty(title)) {
                showMessage(resources.getString(R.string.hint_sleave_title))
            } else if (TextUtils.isEmpty(reason)) {
                showMessage(resources.getString(R.string.hint_sleave_reason))
            } else if (TextUtils.isEmpty(startString) || TextUtils.isEmpty(endString)) {
                showMessage(resources.getString(R.string.text_chose_time))
            } else {
                val starDates = DateUtil.parse("yyyy-MM-dd HH:mm", startString)
                val endDates = DateUtil.parse("yyyy-MM-dd HH:mm", endString)
                val curTime = System.currentTimeMillis()
                val starTime = starDates.time + 60 * 60 * 1000L
                val endTime = endDates.time + 60 * 60 * 1000L
                Log.e(TAG,"提交 $startString $starDates $endString $endDates")
                if(starTime<curTime){
                    showMessage("请假开始时间不能小于当前时间")
                    return@setOnClickListener
                }else if(endTime <curTime){
                    showMessage("请假结束时间不能小于当前时间")
                    return@setOnClickListener
                }else if(endTime <starTime){
                    showMessage("请假结束时间不能小于请假开始时间")
                    return@setOnClickListener
                }
                val star = id_tvStartTime!!.text.toString()
                val end = id_tvEndTime!!.text.toString()
                mLoadingDoialog?.show()
                mNetHelper.addOffDay(star, end, reason, title)
            }
        }
    }

    override fun onDestroy() {
        unregisterEventbus(this)
        mCount?.run {
            cancel()
        }
        mCount=null
        super.onDestroy()
    }

    private fun setDateAndTime() {
        mDialogYearMonthDay?.show(fragmentManager, "year_month_day")
        /*val cdp = CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setDoneText(resources.getString(R.string.text_ok))
                .setCancelText(resources.getString(R.string.cancel))
        cdp.show(activity.supportFragmentManager, "AddSleaveFragment")*/
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date = year.toString() + "-" + getNumberString(monthOfYear + 1) + "-" + getNumberString(dayOfMonth)
        val rtpd = RadialTimePickerDialogFragment().setOnTimeSetListener(this).setThemeLight()
        rtpd.show(activity.supportFragmentManager, "AddSleaveFragment")
    }

    override fun onTimeSet(dialog: RadialTimePickerDialogFragment, hourOfDay: Int, minute: Int) {
        time = getNumberString(hourOfDay) + ":" + getNumberString(minute)
        if (isStart) {
            startString = date + " " + time
            id_tvStartTime!!.text = startString
        } else {
            endString = date + " " + time
            id_tvEndTime!!.text = endString
        }
    }

    private fun getNumberString(num: Int): String {
        var format = ""
        if (num < 10) {
            format = "0" + num
        } else if (num >= 10) {
            format = num.toString()
        }
        return format
    }

    private var mCount:CountDownTimer?=null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("AddSleave" == msg) {
            mNetHelper.getDayOffList(1, 20)
            mLoadingDoialog?.dismiss()
            showMessage(resources.getString(R.string.text_commited))
            mCount=object :CountDownTimer(1000,2000){
                override fun onFinish() {
                    val event= ViewEvent()
                    event.type=10000
                    EventBus.getDefault().post(event)
                }

                override fun onTick(millisUntilFinished: Long) {

                }
            }.start()
        }
    }
}// Required empty public constructor
