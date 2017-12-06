package cc.manbu.schoolinfocommunication.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.Sleave
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.view.adapter.MyFragmentPagerAdapter
import cn.yc.student.view.fragment.HandledFragment
import cn.yc.student.view.fragment.UnhandleFragment

import com.jaeger.library.StatusBarUtil
import com.nineoldandroids.animation.ObjectAnimator
import kotlinx.android.synthetic.main.activity_leave_manage.*
import kotlinx.android.synthetic.main.layout_head.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.common.util.DensityUtil
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject

import java.util.ArrayList

class LeaveManageActivity : BaseActivityStudent() {

    private var curIndex: Int = 0//當前頁面
    private var fragmentList: ArrayList<Fragment>? = null
    private var isDraw = true
    private val unHandleList = ArrayList<Sleave>()
    private val handleList = ArrayList<Sleave>()

    override fun getLayoutId(): Int {
        return R.layout.activity_leave_manage
    }

    override fun initView() {
        init()
        initViewPager()
        mLoadingDoialog?.show()
        mNetHelper.accessSleaves(0, 20)
    }

    private fun init() {
        id_tvTitle!!.setText(R.string.text_holoday)
        id_rgTitle!!.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (isDraw) {
                    setOffset(id_rbMain, 0)
                    isDraw = false
                }
                if (id_rgTitle.viewTreeObserver.isAlive)
                    id_rgTitle.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
        id_rbMain?.setOnClickListener {
            viewpager!!.currentItem = 0
        }
        id_rbNormal?.setOnClickListener {
            viewpager!!.currentItem = 1
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("SearchSleave" == msg) {
            val datas = event.getSleaves()
            mLoadingDoialog?.dismiss()
            if (!isLoadMore) {
                page = 0
                unHandleList.clear()
                handleList.clear()
            }
            for (sleave in datas) {
                val state = sleave.getState()
                if (state == 0) {
                    unHandleList.add(sleave)
                } else {
                    handleList.add(sleave)
                }
            }
            isLoadMore = true
            if (datas.size < 20) {
                isLoadMore = false
                page = 0
            }
            setDataToFragment()
        }

    }

    private fun setOffset(rb: RadioButton?, i: Int) {
        curIndex = i
        id_rbMain!!.setTextColor(getResources().getColor(R.color.black))
        id_rbNormal!!.setTextColor(getResources().getColor(R.color.black))
        rb!!.setTextColor(getResources().getColor(R.color.toolbar_text_color))
        rb.isChecked = true
        setDataToFragment()
        var width = id_ivLine!!.measuredWidth
        val screenW = Configs.get(Configs.Config.ScreenWidth, Integer.TYPE)
        val w = rb.paint.measureText(rb.text.toString()).toInt()
        val w2 = rb.width
        val params = id_ivLine.layoutParams
        params.width = w
        params.height = DensityUtil.dip2px(2f)
        id_ivLine.layoutParams = params
        val offset: Int
        if (width > w) {
            width = width - w
        } else {
            width = 0
        }
        if (i == 0)
            offset = (w2 - w - width) / 2
        else
            offset = screenW - (w2 - w + width) / 2 - w
        val animator = ObjectAnimator.ofFloat(id_ivLine, "translationX", offset.toFloat())
        animator.setDuration(200)
        animator.start()
    }

    private fun setDataToFragment() {
        val events = ViewEvent()
        if (curIndex == 0) {
            events.setSleaveList(unHandleList)
            events.setMessage(Constant.EVENT_UNHANDLE_LIST)
        } else {
            events.setSleaveList(handleList)
            events.setMessage(Constant.EVENT_HANDLE_LIST)
        }
        EventBus.getDefault().post(events)
    }

    /*
     * 初始化ViewPager
     */
    private fun initViewPager() {
        fragmentList = ArrayList()
        val mainGuardianFragment = UnhandleFragment()
        val normalUserFragment = HandledFragment()
        fragmentList!!.add(mainGuardianFragment)
        fragmentList!!.add(normalUserFragment)

        //给ViewPager设置适配器
        viewpager!!.adapter = MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList)
        viewpager.currentItem = 0//设置当前显示标签页为第一页
        viewpager.setOnPageChangeListener(MyOnPageChangeListener())//页面变化时的监听器
    }

    private inner class MyOnPageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            if (position == 0) {
                setOffset(id_rbMain, 0)
            } else {
                setOffset(id_rbNormal, 1)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    companion object {

        var isLoadMore: Boolean = false
        var page = 0//页数
    }
}
