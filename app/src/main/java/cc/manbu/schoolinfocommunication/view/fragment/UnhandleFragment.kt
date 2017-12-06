package cn.yc.student.view.fragment


import android.content.Intent
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
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.Sleave
import cc.manbu.schoolinfocommunication.view.adapter.DayOffAdapter

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList

import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.view.activity.LeaveManageActivity
import cc.manbu.schoolinfocommunication.view.activity.LeavesDeatilsActivity
import kotlinx.android.synthetic.main.fragment_unhandle.*

/**
 * A simple [Fragment] subclass.
 */
class UnhandleFragment : BaseFragmentStudent(), AbsListView.OnScrollListener {

    private var adapter: DayOffAdapter? = null
    private val list = ArrayList<Sleave>()
    private var lastItemIndex = 0   //最后的可视项索引

    override fun getLayoutId(): Int {
        return R.layout.fragment_unhandle
    }

    override fun initView() {
        registerEventbus(this)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun init() {
        id_refreshLayout!!.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid)
        id_refreshLayout?.setOnRefreshListener {
            LeaveManageActivity.isLoadMore = false
            mNetHelper.accessSleaves(0, 20)
        }
        id_refreshLayout.setProgressViewEndTarget(true, 120)//设置距离顶端的距离

        adapter = DayOffAdapter(list, context)
        id_lvUnhandleList!!.adapter = adapter
        id_lvUnhandleList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, LeavesDeatilsActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("sleave", list[position])
            bundle.putString("flgs", "UnhandleFragment")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        id_lvUnhandleList.setOnScrollListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ViewEvent) {
        val msg = event.getMessage()
        if (Constant.EVENT_UNHANDLE_LIST.equals(msg)) {
            id_refreshLayout!!.isRefreshing = false
            val datas = event.getSleaveList()
            if (!LeaveManageActivity.isLoadMore) {
                LeaveManageActivity.page = 0
                list.clear()
            }
            list.addAll(datas)
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
            if (list.size <= 0) {
                id_llEmpty!!.visibility = View.VISIBLE
                id_refreshLayout.visibility = View.GONE
            } else {
                id_llEmpty!!.visibility = View.GONE
                id_refreshLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastItemIndex == adapter!!.getCount() - 1 && LeaveManageActivity.isLoadMore) {
            //加载数据代码
            LeaveManageActivity.page++
            mNetHelper.accessSleaves(LeaveManageActivity.page, 20)
        }
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        lastItemIndex = firstVisibleItem + visibleItemCount - 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}// Required empty public constructor
