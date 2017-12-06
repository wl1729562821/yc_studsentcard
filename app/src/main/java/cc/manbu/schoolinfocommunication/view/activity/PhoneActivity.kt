package cc.manbu.schoolinfocommunication.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.jaeger.library.StatusBarUtil

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject

import java.util.HashMap
import java.util.HashSet
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.tools.Utils
import cn.yc.student.view.fragment.ContactsFragment
import cn.yc.student.view.fragment.EditPhoneFragment
import cn.yc.student.view.fragment.PhoneListFragment
import kotlinx.android.synthetic.main.layout_head.*

class PhoneActivity : BaseActivityStudent() {

    private val fragmentTag = HashSet<String>()
    private val cachFragment = HashMap<String, Fragment>()
    private var currentTag: String? = null
    private var flgs: Int = 0
    private var curFragment: Fragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_phone
    }

    override fun initView() {
        init()
        showFragments("PhoneListFragment")
    }

    private fun init() {
        id_tvTitle?.setText(R.string.text_phone_books)
        toolbar_title_right?.setOnClickListener(mListenr)
        toolbar_right?.setOnClickListener(mListenr)
        toolbar_back?.setOnClickListener(mListenr)
    }

    val mListenr=object :View.OnClickListener{
        override fun onClick(v: View?) {
            v?.let {
                when(it.id){
                    //添加事件的点击事件
                    R.id.toolbar_title_right ->{
                        flgs = 2
                        Utils.position = -1
                        id_rlRight!!.visibility = View.INVISIBLE
                        showFragments("EditPhoneFragment")
                    }
                    R.id.toolbar_right ->{
                        flgs = 1
                        showFragments("ContactsFragment")
                    }
                    R.id.toolbar_back ->{
                        Log.e("PhoneActivity","doReturns")
                        doReturns()
                    }
                }
            }
        }
    }

    fun showFragment(type:Int){
        when(type){
            1->{
                toolbar_title_right?.visibility=View.GONE
                toolbar_right?.visibility=View.VISIBLE
            }
            2->{
                toolbar_title_right?.visibility=View.VISIBLE
                toolbar_right?.visibility=View.GONE
                toolbar_title_right?.text=getString(R.string.text_add)
            }
            else ->{
                toolbar_title_right?.visibility=View.GONE
                toolbar_right?.visibility=View.GONE
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ViewEvent) {
        if(event.type==4000){
            doReturns()
            return
        }
        val msg = event.message
        if (Constant.EVENT_EDIT_PHONE_LIST == msg) {
            flgs = event.flg
            id_rlRight!!.visibility = View.INVISIBLE
            showFragments("EditPhoneFragment")
        }
    }

    private fun doReturns() {
        if (flgs == 0) {
            finish()
        } else if (flgs == 1) {
            flgs = 0
            showFragments("PhoneListFragment")
        } else if (flgs == 2) {
            flgs = 1
            showFragments("ContactsFragment")
        }
        id_rlRight!!.visibility = View.VISIBLE
    }

    private fun showFragments(tag: String) {
        var fragment: Fragment? = null
        if ("PhoneListFragment" == tag) {
            fragment = PhoneListFragment()
            showFragment(1)
        } else if ("ContactsFragment" == tag) {
            fragment = ContactsFragment()
            showFragment(2)
        } else if ("EditPhoneFragment" == tag) {
            fragment = EditPhoneFragment()
            showFragment(3)
        }
        fragment?.let {
            cachFragment.put(tag, it)
            selectFragment(tag)
        }
    }

    protected fun selectFragment(tag: String) {
        fragmentTag.add(tag)
        currentTag = tag
        hideAllFragmen()
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
        var fragment: Fragment? = fm.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = cachFragment[tag]
            transaction.add(R.id.id_flPhoneContent, fragment, tag)
        } else {
            transaction.show(fragment)
        }
        curFragment = fragment
        transaction.commit()
    }

    private fun hideAllFragmen() {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        for (tag in fragmentTag) {
            val fragment = fm.findFragmentByTag(tag)
            if (fragment != null)
                transaction.hide(fragment)
        }
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (curFragment != null)
            curFragment!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(flgs!=0){
                doReturns()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
