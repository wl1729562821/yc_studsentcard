package cc.manbu.schoolinfocommunication.view.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.ViewUtils
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import cn.yc.model.listener.BaseOnItemListener

import cn.yc.student.bean.ViewHoler
import cn.yc.student.view.adapter.NameAdapter
import com.nineoldandroids.animation.ObjectAnimator
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.layout_pop_login_list.view.*
import org.xutils.ex.DbException
import org.xutils.x
import org.greenrobot.eventbus.EventBus
import org.xutils.db.sqlite.WhereBuilder
import org.xutils.DbManager
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.recyclerViewInit
import cc.manbu.schoolinfocommunication.bean.NameListBean
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.ManbuApplication
import cc.manbu.schoolinfocommunication.listener.HttpRespnse
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener

/**
 *@Author:余慈
 *@Date:2017/11/25 0025
 *@Description:登录界面
 **/
class LoginActivity : BaseActivityStudent() {

    private var loginName: String = ""
    private var loginPwd: String = ""
    private var popupWindow: PopupWindow? = null
    private var list: ArrayList<NameListBean>? = null

    private var mLogin=false

    private var mClickListener = View.OnClickListener {
        it?.let {
            when (it.id) {
                R.id.id_tvLogin -> {
                    mLogin=true
                    mNetHelper?.tcpPopMsgAddress
                    loginName = id_edName.text.toString()
                    if (loginName.trim().isEmpty()) {
                        showMessage(resources.getString(R.string.text_input_loginname))
                        return@let
                    }
                    loginPwd = id_edPwd.text.toString()
                    if (loginPwd.trim().isEmpty()) {
                        showMessage(resources.getString(R.string.text_input_pwd))
                        return@let
                    }
                    mLoadingDoialog?.show()
                    mNetHelper?.login(loginName, loginPwd, 1,object: HttpCallListener {
                        override fun onError(code: Int,msg: String?) {
                            var message=msg?:"登录失败，请稍后在试"
                            mLoadingDoialog?.dismiss()
                            showMessage(message)
                        }

                        override fun <T> onNext(data: HttpRespnse<T>) {
                            data.data?.run {
                                this as? R_Users
                            }?.run {
                                this.loginName = loginName
                                this.passWord = loginPwd
                                this.isLogined = true
                                this.setLastLoginedTime(System.currentTimeMillis())
                                Configs.put(Configs.Config.CurUser,this)
                                saveTokent(this)
                                return
                            }
                            mLoadingDoialog?.dismiss()
                            showMessage("登录失败，请稍后再试")
                        }
                    })
                }
                R.id.id_ivDown -> {
                    if (list == null || list?.isEmpty() == true) {
                        showMessage(getString(R.string.text_no_login_history))
                    } else {
                        if(popupWindow==null || popupWindow?.isShowing==false){
                            ObjectAnimator.ofFloat(id_ivDown, "rotation", 180f).setDuration(1000L).start()
                            showDownList(id_edName)
                        }else{
                            popupWindow?.run {
                                ObjectAnimator.ofFloat(id_ivDown, "rotation", 360f).setDuration(1000L).start()
                                dismiss()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveTokent(user:R_Users){
        mNetHelper.saveToken(this,user,object :HttpCallListener{
            override fun <T> onNext(data: HttpRespnse<T>) {
                mLoadingDoialog?.dismiss()
                data.data?.run {
                    this as? R_Users
                }?.run {
                    val db = x.getDb(ManbuApplication.getInstance().daoConfig)
                    val listBean = NameListBean()
                    listBean.name = loginName
                    listBean.password = loginPwd
                    try {
                        val b = WhereBuilder.b()
                        b.and("name", "=", loginName)
                        db.delete(NameListBean::class.java, b)
                        db.save(listBean)
                    } catch (e: DbException) {
                        e.printStackTrace()
                        showMessage("登录失败，请稍后再试")
                        return
                    }
                    if (isTeacher) {
                        Configs.whichRole = 1//老师
                    } else {
                        Configs.whichRole = 0//学生
                    }
                    if(mLogin){
                        mLogin=false
                        val intent= Intent()
                        intent.setClass(mAtv,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    return
                }
                showMessage("登录失败，请稍后再试")
            }

            override fun onError(code: Int, msg: String?) {
                var message=msg?:"登录失败，请稍后在试"
                mLoadingDoialog?.dismiss()
                showMessage(message)
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_signin
    }

    override fun initView() {
        getLoginEditName()
        id_edName.addTextChangedListener(object : TextWatcher {
            internal var l = 0////////记录字符串被删除字符之前，字符串的长度
            internal var location = 0//记录光标的位置
            internal var changedCount = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                l = s.length
                location = id_edName.selectionStart
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (l - s.length > 0)
                    changedCount++
                else
                    changedCount = 0
            }

            override fun afterTextChanged(s: Editable) {
                if (l > s.toString().length && changedCount >= 2) {
                    id_edName.text.clear()
                    id_edPwd.text.clear()
                }
            }
        })
        id_edPwd.addTextChangedListener(object : TextWatcher {
            internal var l = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                l = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (l > s.toString().length) {
                    id_edPwd.text.clear()
                }
            }
        })
        id_tvLogin?.setOnClickListener(mClickListener)
        id_ivDown?.setOnClickListener(mClickListener)
    }

    //获取当前用户输入框下拉数据
    private fun getLoginEditName() {
        x.task().run {
            val db = x.getDb(ManbuApplication.getInstance().daoConfig)
            try {
                val dbList = db.selector(NameListBean::class.java).orderBy("id", true).findAll()
                dbList?.let {
                    list= arrayListOf()
                    for(i in 0..(dbList.size-1)){
                        list?.add(dbList[i])
                    }
                    runOnUiThread {
                        list?.let {
                            if (id_edName != null) {
                                id_edName.setText(it[0].name)
                                val etext = id_edName.text
                                id_edName.setSelection(etext.length)
                            }
                            if (id_edPwd != null) {
                                id_edPwd.setText(it[0].password)
                            }
                        }
                    }
                }
            } catch (e: DbException) {
                e.printStackTrace()
            }
        }
    }

    private fun showDownList(v: View) {
        val popView = LayoutInflater.from(this).inflate(R.layout.layout_pop_login_list, null)
        popView.id_lvNameList?.let {
            with(it) {
                recyclerViewInit(this, 0)
                list = list ?: arrayListOf()
                list?.let { data ->
                    adapter = NameAdapter(
                            this@LoginActivity,
                            data,
                            object : BaseOnItemListener<ViewHoler<NameListBean>> {
                                override fun itemClick(t: ViewHoler<NameListBean>?) {
                                    t?.let {
                                        with(it) {
                                            when (type) {
                                                1000 -> {
                                                    val event = ViewEvent()
                                                    event.message = Constant.EVENT_FREASH_LIST_LOGIN_NAME
                                                    event.flg = position
                                                    EventBus.getDefault().post(event)
                                                }
                                                1001 -> {
                                                    if (popupWindow != null && popupWindow?.isShowing ==true){
                                                        popupWindow?.dismiss()
                                                    }
                                                    if (id_edName != null){
                                                        id_edName.setText(dataBean.name)
                                                    }
                                                    if (id_edPwd != null){
                                                        id_edPwd.setText(dataBean?.password)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    )

                }
                Log.e(TAG,"showDownList $displayWidth")
                popupWindow = PopupWindow(popView,(displayWidth*0.745).toInt(),
                        ViewGroup.LayoutParams.WRAP_CONTENT, false)
                popupWindow?.let {
                    with(it) {
                        animationStyle = R.style.popwin_anim_style2
                        isTouchable = true
                        isOutsideTouchable = true
                        setBackgroundDrawable(BitmapDrawable(resources, null as Bitmap?))
                        showAsDropDown(v, (displayWidth*0.005).toInt(), 0)
                        setOnDismissListener { ObjectAnimator.ofFloat(id_ivDown, "rotation", 360f).setDuration(1000L).start() }

                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ResposeEvent) {
        val msg = event.message
        val flgs = event.flg
        if ("Login" == msg && flgs == 1) {
            val rUsers = event.getrUsers()
            if (rUsers != null) {
                rUsers.loginName = loginName
                rUsers.passWord = loginPwd
                rUsers.isLogined = true
                rUsers.setLastLoginedTime(System.currentTimeMillis())
                Configs.put(Configs.Config.CurUser, rUsers)
                mNetHelper.saveToken(mAtv, rUsers)
                val db = x.getDb(ManbuApplication.getInstance().daoConfig)
                val listBean = NameListBean()
                listBean.name = loginName
                listBean.password = loginPwd
                try {
                    val b = WhereBuilder.b()
                    b.and("name", "=", loginName)
                    db.delete(NameListBean::class.java, b)
                    db.save(listBean)
                } catch (e: DbException) {
                    e.printStackTrace()
                }

            } else {
                mLoadingDoialog?.dismiss()
                showMessage(resources.getString(R.string.text_login_failed))
            }
        } else if ("SaveAndroidToken" == msg) {
            val flg = event.flg
            mLoadingDoialog?.dismiss()
            if (flg == 1) {
                val rUsers = event.getrUsers()
                if (rUsers.isTeacher) {
                    Configs.whichRole = 1//老师
                } else {
                    Configs.whichRole = 0//学生
                }
                val intent= Intent()
                intent.setClass(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showMessage(resources.getString(R.string.text_data_failed))
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThreadUi(event: ViewEvent) {
        val msg = event.message
        if (Constant.EVENT_FREASH_LIST_LOGIN_NAME == msg) {
            val pos = event.flg
            val db = x.getDb(ManbuApplication.getInstance().daoConfig)
            val name = list?.get(pos)?.name
            val b = WhereBuilder.b()
            b.and("name", "=", name)
            try {
                db.delete(NameListBean::class.java, b)
                list?.removeAt(pos)
                popupWindow?.dismiss()
            } catch (e: DbException) {
                e.printStackTrace()
            }

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mLoadingDoialog?.isShowing==true){
                mLogin=false
                mLoadingDoialog?.dismiss()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}