package cc.manbu.schoolinfocommunication.view.activity

import android.text.TextUtils
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_head.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Administrator on 2017/11/25 0025.
 */
class ChangePasswordActivity:BaseActivityStudent(){

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initView() {
        id_tvTitle?.setText(R.string.text_change_pwd)

        id_tvSure?.setOnClickListener {
            val oldPwd = id_edOldPwd.getText().toString()
            val newPwd = id_edNewPwd.getText().toString()
            val newPwd2 = id_edNewPwd2.getText().toString()
            if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwd2)) {
                showMessage(getString(R.string.text_new_pwd_not_null))
            } else if (TextUtils.isEmpty(oldPwd)) {
                showMessage(getString(R.string.text_old_pwd_not_null))
            } else if (newPwd != newPwd2) {
                showMessage(getString(R.string.text_new_not_match))
            } else {
                val users = Configs.get(Configs.Config.CurUser, R_Users::class.java)
                users?.let {
                    mLoadingDoialog?.show()
                    mNetHelper.undatePassword(oldPwd, newPwd,it.loginName)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread1(event: ResposeEvent) {
        val msg = event.message
        if ("UpdatePwd" == msg) {
            mLoadingDoialog?.dismiss()
            id_edOldPwd.editableText.clear()
            id_edNewPwd.editableText.clear()
            id_edNewPwd2.editableText.clear()
            val result = event.content
            try {
                val json = JSONObject(result)
                val s = json.getString("d")
                showMessage(s)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

}