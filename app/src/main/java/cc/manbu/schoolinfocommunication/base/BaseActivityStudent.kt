package cc.manbu.schoolinfocommunication.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.config.ManbuApplication
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.httputils.NetHelper
import cc.manbu.schoolinfocommunication.push.PushMessageService
import cc.manbu.schoolinfocommunication.view.activity.LoginActivity
import cc.manbu.schoolinfocommunication.view.activity.WelcomeActivity
import cc.manbu.schoolinfocommunication.view.customer.CustomDialog
import cn.yc.model.listener.BaseActivityListener
import com.bugtags.library.Bugtags
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.layout_head.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.x
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Administrator on 2017/12/1 0001.
 */
abstract class BaseActivityStudent:AppCompatActivity(), BaseActivityListener{
    var TAG = ""

    var displayWidth = 0
        private set
        get() {
            val display = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(display)
            return display.widthPixels
        }
    var displayHeight = 0
        private set
        get(){
            val display = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(display)
            return display.heightPixels
        }

    init {
        TAG = javaClass.simpleName
    }

    protected var mFragmentStudent: BaseFragmentStudent? = null
    protected var mNetHelper = NetHelper.getInstance()
    protected var mLoadingDoialog: CustomDialog?=null

    protected var mSavedInstanceState: Bundle?=null
    protected var mAtv:BaseActivityStudent?=null

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = javaClass.simpleName
        setContentView(getLayoutId())
        StatusBarUtil.setTransparent(this)
        init(savedInstanceState)
        initView()
    }

    override fun init(savedInstanceState: Bundle?) {
        mAtv=this
        ManbuApplication.getInstance().addActivity(this)
        registerEventbus(this)
        x.view().inject(this);
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        Configs.put(Configs.Config.ScreenWidth, dm.widthPixels)
        Configs.put(Configs.Config.ScreenHeight, dm.heightPixels)
        toolbar_back?.setOnClickListener {
            Log.e(TAG,"toolbar_back back")
            back()
        }
        mSavedInstanceState=savedInstanceState
        mLoadingDoialog=mLoadingDoialog?: CustomDialog(mAtv,R.style.CustomDialog)
    }

    override fun onAttachFragment(fragment: android.app.Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let {
            (it as? BaseFragmentStudent)?.let {
                mFragmentStudent=it
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Bugtags.onDispatchTouchEvent(this, ev)
        return if (mFragmentStudent != null && mFragmentStudent?.dispatchTouchEvent(ev) == true) {
            true
        } else super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        Bugtags.onResume(this)
        val cls = javaClass
        if (cls != LoginActivity::class.java || cls!=WelcomeActivity::class.java) {
            val lastOperateTime = Configs.lastOperateTime
            if (lastOperateTime != 0L && System.currentTimeMillis() - lastOperateTime >= 1000000) {
                val rUsers = Configs.get(Configs.Config.CurUser, R_Users::class.java)
                rUsers?.run {
                    val account =loginName
                    val pwd = passWord
                    mNetHelper.login(account, pwd, 2)
                }

            }
        }
    }

    override fun onDestroy() {
        mSavedInstanceState=null
        mFragmentStudent=null
        unregisterEventbus(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventMainThread(event: ViewEvent) {
        val msg = event.message
        if (Constant.EVENT_ON_ERROR == msg) {
            var errTips = event.content
            if (TextUtils.isEmpty(errTips)) {
                errTips = resources.getString(R.string.text_unkonw_err)
            }
            mLoadingDoialog?.dismiss()
            showMessage(errTips)
        }
    }

    open fun onEventMainThread(event: ResposeEvent) {
        val msg = event.message
        val flgs = event.flg
        mAtv?.let {
            activity->
            if ("Login" == msg && flgs == 2) {
                val users = event.getrUsers()
                users?.let {
                    stopService(Intent(mAtv, PushMessageService::class.java))
                    PushMessageService.cancelTimerAlarmTask()
                    Configs.put(Configs.Config.CurClientUser, null)
                    val rUsers = Configs.get(Configs.Config.CurUser, R_Users::class.java)
                    if (rUsers != null) {
                        rUsers?.isLogined = false
                    }
                    activity.stopService(Intent(activity, PushMessageService::class.java))
                    PushMessageService.cancelTimerAlarmTask()
                    ManbuApplication.activityList.remove(activity)
                    activity.finish()
                    ManbuApplication.exit()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Bugtags.onPause(this)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun back() {
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}