package cc.manbu.schoolinfocommunication.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView

import com.jaeger.library.StatusBarUtil

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
import cn.yc.student.view.fragment.*
import kotlinx.android.synthetic.main.layout_head.*

class SchoolActivity : BaseActivityStudent() {
    private val fragmentTag = HashSet<String>()
    private val cachFragment = HashMap<String, Fragment>()
    private var currentTag: String? = null
    private var isFinish = true
    private var pos: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_school
    }

    override fun initView() {
        init()
        showFragments("SchoolFragment")
    }

    private fun init() {
        id_tvTitle?.setText(R.string.text_my_school)
        toolbar_back?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                doReturn()
            }
        })
    }

    private fun doReturn() {
        if (isFinish) {
            finish()
        } else {
            isFinish = true
            if (pos == 2) {
                val newsFragment = cachFragment["SchoolNewsFragment"] as SchoolNewsFragment
                val isCanGoBack = newsFragment.isWebViewCanGoBack
                if (isCanGoBack) {
                    val event = ViewEvent()
                    event.message = Constant.EVENT_GO_BACK
                    EventBus.getDefault().post(event)
                    isFinish = false
                } else {
                    pos = 0
                    showFragments("SchoolFragment")
                }
            } else {
                showFragments("SchoolFragment")
            }
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
            transaction.add(R.id.id_flSchoolContent, fragment, tag)
        } else {
            transaction.show(fragment)
        }
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

    private fun removeFragmentByTag(tag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        val fragment = fm.findFragmentByTag(tag)
        if (fragment != null)
            transaction.remove(fragment)
        transaction.commit()
    }

    private fun showFragments(tag: String) {
        var fragment: Fragment? = null
        if ("SchoolFragment" == tag) {
            fragment = SchoolFragment()
        } else if ("IntroductionFragment" == tag) {
            fragment = IntroductionFragment()
        } else if ("AnnouncementFragment" == tag) {
            fragment = AnnouncementFragment()
        } else if ("SchoolNewsFragment" == tag) {
            fragment = SchoolNewsFragment()
        } else if ("FantasticActivityFragment" == tag) {
            fragment = FantasticActivityFragment()
        }
        fragment?.let {
            cachFragment.put(tag, it)
            selectFragment(tag)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ViewEvent) {
        val msg = event.message
        if (Constant.EVENT_SHOW_SCHOOL_ITEM == msg) {
            val flg = event.flg
            isFinish = false
            pos = flg
            if (flg == 0) {
                showFragments("IntroductionFragment")
            } else if (flg == 1) {
                showFragments("AnnouncementFragment")
            } else if (flg == 2) {
                showFragments("SchoolNewsFragment")
            } else if (flg == 3) {
                showFragments("FantasticActivityFragment")
            }
        }
    }

    override fun onBackPressed() {
        doReturn()
    }
}
