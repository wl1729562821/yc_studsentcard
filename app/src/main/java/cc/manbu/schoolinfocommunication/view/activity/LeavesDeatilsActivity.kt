package cc.manbu.schoolinfocommunication.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.Sleave
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.tools.DateUtil
import com.jaeger.library.StatusBarUtil

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import org.xutils.view.annotation.Event
import kotlinx.android.synthetic.main.activity_leaves_deatils.*
import kotlinx.android.synthetic.main.layout_head.*

class LeavesDeatilsActivity : BaseActivityStudent() {

    private var sleave: Sleave? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_leaves_deatils
    }

    override fun initView() {
        init()
    }

    private fun init() {
        val intent = intent
        val bundle = intent.extras
        sleave = bundle?.getSerializable("sleave") as Sleave
        if (sleave != null) {
            val state = sleave?.state
            val addDate = sleave?.addDate
            val starDate = sleave?.startTime
            val endDate = sleave?.endTime
            when (state) {
                0//未审核
                -> {
                    id_tvState?.setText(R.string.text_reading)
                    id_tvState.setTextColor(ContextCompat.getColor(this, R.color.yellow))
                }
                1//未通过
                -> {
                    id_tvState?.setText(R.string.text_pass)
                    id_tvState.setTextColor(ContextCompat.getColor(this, R.color.red))
                }
                2//通过
                -> {
                    id_tvState?.setText(R.string.text_passed)
                    id_tvState.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
            id_tvAddDate?.text = DateUtil.format("yyyy-MM-dd HH:mm:ss EE", addDate)
            id_tvRemark?.text = sleave?.remark
            id_tvReson?.text = sleave?.reason
            id_tvDate?.text = DateUtil.format("yyyy/MM/dd HH:mm:ss", starDate) + " ----- " +
                    DateUtil.format("yyyy/MM/dd HH:mm:ss", endDate)
            val s = bundle.getString("flgs")
            if ("HandledFragment" == s) {
                id_llReason?.visibility = View.GONE
            } else {
                id_llReason?.visibility = View.VISIBLE
            }
        }
        id_tvTitle?.setText(R.string.text_holoday)
        id_tvAllow?.setOnClickListener {
            val reason = id_edRemark?.text.toString()
            if (TextUtils.isEmpty(reason)) {
                showMessage(resources.getString(R.string.hint_reason))
            } else {
                mLoadingDoialog?.show()
                mNetHelper.allowSleave(sleave?.id?:0, reason)
            }
        }
        id_tvRefuse?.setOnClickListener {
            val reasons = id_edRemark?.text.toString()
            if (TextUtils.isEmpty(reasons)) {
                showMessage(resources.getString(R.string.hint_reason))
            } else {
                mLoadingDoialog?.show()
                mNetHelper.refuseSleave(sleave?.id?:0, reasons)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ResposeEvent) {
        val msg = event.message
        if ("AgreeSleave" == msg || "DisAgreeSleave" == msg) {
            val result = event.content
            mLoadingDoialog?.dismiss()
            try {
                val `object` = JSONObject(result)
                val s = `object`.getString("d")
                if ("True" == s) {
                    showMsgDialog()
                } else {
                    showMessage(getString(R.string.text_handle_msg_fail))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }
    protected fun showMsgDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_tips)
        builder.setMessage(R.string.text_handle_msg_success)
        builder.setPositiveButton(R.string.text_ok) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        builder.create().show()
    }
}
