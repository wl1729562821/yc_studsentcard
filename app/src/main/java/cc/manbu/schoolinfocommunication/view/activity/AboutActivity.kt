package cc.manbu.schoolinfocommunication.view.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_head.*

/**
 * Created by Administrator on 2017/11/25 0025.
 */
class AboutActivity: BaseActivityStudent(){

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        id_tvTitle?.setText(R.string.text_about)
        val pm = packageManager
        val pi: PackageInfo
        try {
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES)
            val versionName = if (pi.versionName == null)
                "null"
            else
                pi.versionName
            val versionCode = pi.versionCode.toString() + ""
            curent_vision?.text = resources.getString(R.string.current_version) + versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

}