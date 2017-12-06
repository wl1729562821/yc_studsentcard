package cn.yc.student.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.Timer
import java.util.TimerTask
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import kotlinx.android.synthetic.main.fragment_task_progress.*

/**
 * A simple [Fragment] subclass.
 */
class TaskProgressFragment : BaseFragmentStudent() {
    private var config: SHX520Device_Config? = null
    private var steps: Int = 0
    private var curFinishedSteps: Int = 0
    private var lastFinishedSteps: Int = 0
    private var lastTargetSteps: Int = 0
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private var isHiden = true

    override fun getLayoutId(): Int {
        return R.layout.fragment_task_progress
    }

    override fun initView() {
        registerEventbus(this)
        val mBoundle = arguments
        config = mBoundle.getSerializable("config") as SHX520Device_Config
        if (config != null) {
            lastFinishedSteps = config!!.finishStepCount
            curFinishedSteps = lastFinishedSteps
            steps = config!!.targetStepCount
            lastTargetSteps = steps
            initValue()
        }
        startTimer()
        id_tvSteps.setText(steps.toString())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("SHX520CancelParentChildInteraction" == msg) {
            mLoadingDoialog!!.dismiss()
        } else if ("GetDeviceDetial" == msg && !isHiden) {
            val device = event.getDevice()
            if (device != null) {
                config = device!!.getSHX520Device_Config()
                if (config != null) {
                    steps = config!!.targetStepCount
                    curFinishedSteps = config!!.finishStepCount
                    initValue()
                    val isCurrentTaskRunning = lastTargetSteps != 0 && lastTargetSteps == steps &&
                            curFinishedSteps >= lastFinishedSteps
                    if (!isCurrentTaskRunning) {
                        id_tvSteps.setText(steps.toString())
                    }
                    lastTargetSteps = steps
                    lastFinishedSteps = curFinishedSteps
                    if (curFinishedSteps != 0 && steps == curFinishedSteps) {
                        val event1 = ViewEvent()
                        event1.setMessage(Constant.EVENT_TASK_COMPLETE)
                        EventBus.getDefault().post(event1)
                    }
                }
            }
        }
    }

    private fun initValue() {
        if (steps != 0) {
            pb_task_progress.setProgress(curFinishedSteps * 100 / steps)
        } else {
            pb_task_progress.setProgress(0)
        }
    }

    private fun startTimer() {
        mTimer = Timer()
        mTimerTask = object : TimerTask() {

            override fun run() {
                x.task().post { mNetHelper.getDeviceDetial() }
            }
        }
        mTimer!!.scheduleAtFixedRate(mTimerTask, 100, 5 * 1000L)
    }

    private fun stopTimer() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
        if (mTimerTask != null) {
            mTimerTask!!.cancel()
            mTimerTask = null
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        isHiden = hidden
        if (hidden) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    override fun onDestroy() {
        unregisterEventbus(this)
        super.onDestroy()
    }
}// Required empty public constructor
