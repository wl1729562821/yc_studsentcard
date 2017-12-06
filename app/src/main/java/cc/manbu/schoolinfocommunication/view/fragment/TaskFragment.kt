package cn.yc.student.view.fragment


import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.bean.SHX520Device_Config
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import kotlinx.android.synthetic.main.fragment_task.*

/**
 * A simple [Fragment] subclass.
 */
/* @BindView(R.id.id_tv12000)
    private TextView id_tv12000;
    @BindView(R.id.id_tv6000)
    private TextView id_tv6000;
    @BindView(R.id.id_tv3000)
    private TextView id_tv3000;*/
class TaskFragment : BaseFragmentStudent() {
    private var steps: Int = 0
    private var config: SHX520Device_Config? = null


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_task
    }

    override fun initView() {
        registerEventbus(this)
        mLoadingDoialog?.show()
        mNetHelper.getDeviceDetial()
        id_rlSimple_task?.setOnClickListener {
            steps = 3000
            setTasks()
        }
        id_rlMedium_task?.setOnClickListener {
            steps = 6000
            setTasks()
        }
        id_rlHard_task?.setOnClickListener {
            steps = 12000
            setTasks()
        }
    }


    private fun setTasks() {
        val isRunning = config != null && config!!.getIsPerdometerRunning()
        if (isRunning) {
            setEventToActivity()
        } else {
            showDialogOfRewards()
        }
    }

    private fun setEventToActivity() {
        val event = ViewEvent()
        event.setMessage(Constant.EVENT_TASK_MESSAGE)
        event.setFlg(steps)
        EventBus.getDefault().post(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("GetDeviceDetial" == msg) {
            mLoadingDoialog?.dismiss()
            val d = event.getDevice()
            config = d.getSHX520Device_Config()
            if (config != null) {
                if (config!!.getIsPerdometerRunning()) {
                    when (config!!.getTargetStepCount()) {
                        3000 -> {
                            id_rlSimple_task!!.isEnabled = true
                            id_rlMedium_task!!.isEnabled = false
                            id_rlHard_task!!.isEnabled = false
                        }
                        6000 -> {
                            id_rlSimple_task!!.isEnabled = false
                            id_rlMedium_task!!.isEnabled = true
                            id_rlHard_task!!.isEnabled = false
                        }
                        12000 -> {
                            id_rlSimple_task!!.isEnabled = false
                            id_rlMedium_task!!.isEnabled = false
                            id_rlHard_task!!.isEnabled = true
                        }
                        else -> {
                            id_rlSimple_task!!.isEnabled = true
                            id_rlMedium_task!!.isEnabled = true
                            id_rlHard_task!!.isEnabled = true
                        }
                    }
                }
            }
        } else if ("SHX520ParentChildInteraction" == msg) {
            mLoadingDoialog?.dismiss()
            val res = event.getContent()
            try {
                val `object` = JSONObject(res)
                val flg = `object`.getString("d")
                if ("0" == flg) {
                    setEventToActivity()
                } else if ("1" == flg) {
                    showMessage(getResources().getString(R.string.tips_device_no_online))
                } else {
                    showMessage(getResources().getString(R.string.text_unkonw_err))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    private fun showDialogOfRewards() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.task_reward)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null)
        val edtInput = view.findViewById(R.id.id_edPhone) as EditText
        edtInput.setTextColor(-0xa89f95)
        edtInput.textSize = 12f
        edtInput.setHint(R.string.task_reward)
        val ea = edtInput.text
        edtInput.setSelection(ea.length)
        builder.setView(view)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setPositiveButton(R.string.text_ok, DialogInterface.OnClickListener { dialog, which ->
            val reward = edtInput.text.toString()
            dialog.dismiss()
            mLoadingDoialog?.show()
            mNetHelper.setTaskOfSteps(steps, reward)
        })
        builder.show()
    }
}// Required empty public constructor
