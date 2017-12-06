package cc.manbu.schoolinfocommunication.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.View
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.ManbuApplication
import cc.manbu.schoolinfocommunication.push.PopSocket
import cc.manbu.schoolinfocommunication.push.PushMessageService
import cc.manbu.schoolinfocommunication.view.activity.FeebackActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.layout_head.*
/**
 * Created by Administrator on 2017/11/25 0025.
 */
class SettingsActivity:BaseActivityStudent(){

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initView() {
        id_tvTitle?.setText(R.string.text_set)
        switch_push.setOnCheckedChangeListener { _, isChecked ->
            Configs.put(Configs.Config.PushEnable, isChecked)
            if (isChecked) {
                startService(Intent(this, PushMessageService::class.java))
            } else {
                stopService(Intent(this, PushMessageService::class.java))
                PushMessageService.cancelTimerAlarmTask()
                PopSocket.UsrMap.clear()
            }
        }
        var isPushEnable = Configs.get(Configs.Config.PushEnable, java.lang.Boolean.TYPE)
        isPushEnable = if (isPushEnable == null) true else isPushEnable
        switch_push.isChecked = isPushEnable!!
        settings_gy?.setOnClickListener{
            intent = Intent(mAtv, AboutActivity::class.java)
            startActivity(intent)
        }
        id_llChangePwd?.setOnClickListener {
            intent = Intent(mAtv, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        id_llFeedback?.setOnClickListener {
            intent = Intent(mAtv, FeebackActivity::class.java)
            startActivity(intent)
        }
        id_llAbout?.setOnClickListener {
            intent = Intent(mAtv, AboutActivity::class.java)
            startActivity(intent)
        }

        id_tvExit?.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        mAtv?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.text_tips)
            builder.setMessage(R.string.text_logout)
            builder.setPositiveButton(R.string.text_ok) { dialog, which ->
                dialog.dismiss()
                stopService(Intent(it, PushMessageService::class.java))
                PushMessageService.cancelTimerAlarmTask()
                ManbuApplication.activityList.remove(it)
                finish()
                ManbuApplication.exit()
                val intent = Intent(it, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            builder.setNegativeButton(R.string.cancel, null)
            builder.create().show()
        }
    }

}