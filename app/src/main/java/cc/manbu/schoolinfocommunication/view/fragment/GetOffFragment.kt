package cn.yc.student.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.bean.Sleave
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.view.adapter.DayOffAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList

import kotlinx.android.synthetic.main.fragment_get_off.*

/**
 * A simple [Fragment] subclass.
 */
class GetOffFragment : BaseFragmentStudent(), AbsListView.OnScrollListener {
    private var rootView: View? = null
    private val list = ArrayList<Sleave>()
    private var adapter: DayOffAdapter? = null
    private var isLoadMore: Boolean = false
    private var page = 1//页数
    private val count = 20//每页的内容量
    private var lastItemIndex = 0   //最后的可视项索引

    override fun getLayoutId(): Int {
        return R.layout.fragment_get_off
    }

    override fun initView() {
        registerEventbus(this)
        adapter = DayOffAdapter(list, activity)
        id_lvDayOff.setAdapter(adapter)
        id_lvDayOff.setOnScrollListener(this)
        id_lvDayOff.setOnItemClickListener { parent, view, position, id ->
            val event = ViewEvent()
            event.setMessage(Constant.EVENT_OFF_DEATILS)
            event.setSleave(list[position])
            EventBus.getDefault().post(event)
        }
        id_refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid)
        id_refreshLayout.setOnRefreshListener {
            isLoadMore = false
            mNetHelper.getDayOffList(page, count)
        }
        id_refreshLayout.setProgressViewEndTarget(true, 120)//设置距离顶端的距离
        mLoadingDoialog?.show()
        mNetHelper.getDayOffList(page, count)
    }

    override fun onDestroy() {
        unregisterEventbus(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("GetUserSleave" == msg) {
            mLoadingDoialog?.dismiss()
            id_refreshLayout.setRefreshing(false)
            val datas = event.getSleaves()
            if (!isLoadMore) {
                page = 1
                list.clear()
            }
            list.addAll(datas)
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
            isLoadMore = true
            if (datas.size < count) {
                isLoadMore = false
                page = 1
            }
            if (list.size <= 0) {
                id_llEmpty.setVisibility(View.VISIBLE)
                id_refreshLayout.setVisibility(View.GONE)
            } else {
                id_llEmpty.setVisibility(View.GONE)
                id_refreshLayout.setVisibility(View.VISIBLE)
            }
        }
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastItemIndex == adapter!!.count - 1 && isLoadMore) {
            //加载数据代码
            page++
            mNetHelper.getDayOffList(page, count)
        }
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        lastItemIndex = firstVisibleItem + visibleItemCount - 1
    }
}// Required empty public constructor
