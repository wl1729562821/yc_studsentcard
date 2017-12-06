package cc.manbu.schoolinfocommunication.view.activity

import android.view.View
import android.widget.AbsListView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.bean.SHX002COW
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.tools.DateUtil
import cc.manbu.schoolinfocommunication.view.adapter.CheckWorksAdapter
import java.util.*
import kotlinx.android.synthetic.main.activity_check_work.*
import kotlinx.android.synthetic.main.layout_head.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.common.util.LogUtil

/**
 * Created by Administrator on 2017/11/25 0025.
 */
class CheckWorkActivity:BaseActivityStudent(){

    private var isLoading = true
    private var isLoadMore: Boolean = false
    private val count = 20//每页的内容量
    private var index = 1//页数
    private val group = ArrayList<String>()
    private val child = ArrayList<List<SHX002COW>>()
    private val set = HashSet<String>()
    private val maps = HashMap<String, List<SHX002COW>>()
    private var adapter: CheckWorksAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_check_work
    }

    override fun initView() {
        mLoadingDoialog?.show()
        getCheckWorks()
        id_refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid)
        id_refreshLayout.setProgressViewEndTarget(true, 120)//设置距离顶端的距离

        adapter = CheckWorksAdapter(this, group, child)
        id_lvCheckWork.setAdapter(adapter)
        id_lvCheckWork.setOnScrollListener(object:AbsListView.OnScrollListener{
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                view?.let {
                    when (scrollState) {
                    // 当不滚动时
                        AbsListView.OnScrollListener.SCROLL_STATE_IDLE ->
                            // 判断滚动到底部
                            if (it.lastVisiblePosition == it.count - 1 && isLoadMore && isLoading) {
                                //TODO
                                index++
                                getCheckWorks()
                                isLoading = false
                            }
                    }
                }
            }
        })

        id_tvTitle.setText(R.string.text_check_class)
    }

    fun getCheckWorks(){
        val month = Calendar.getInstance().time.month
        mNetHelper?.accessCheckRecords(count, index, month)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread1(event: ResposeEvent) {
        val msg = event.message
        if ("SearchSHX002COW" == msg) {
            val cowList = event.getCheckWorksList()
            val tempTitle = ArrayList<String>()
            val tempChild = ArrayList<List<SHX002COW>>()
            //HashMap<String,List<SHX002COW>> maps = new HashMap<>();
            mLoadingDoialog?.dismiss()
            id_refreshLayout.isRefreshing = false
            isLoading = true
            if (!isLoadMore) {
                index = 1
                group.clear()
                child.clear()
            }
            maps.clear()
            if (cowList != null) {
                for (c in cowList!!) set.add(DateUtil.format("yyyy-MM-dd EE", c.time))
                for (s in set) {
                    val lists = ArrayList<SHX002COW>()
                    for (c in cowList!!) {
                        val time = c.time
                        val dateString = DateUtil.format("yyyy-MM-dd EE", time)
                        if (s == dateString) {
                            lists.add(c)
                        }
                    }
                    maps.put(s, lists)
                }
                for (o in maps.entries) {
                    val entry = o
                    val key = entry.key
                    val value = entry.value
                    tempTitle.add(key)
                    tempChild.add(value)
                }
                for (i in tempTitle.indices) {
                    val s = tempTitle[i]
                    val values = maps[s]
                    if (group.contains(s)) {
                        child[i]=values?: arrayListOf()
                    } else {
                        group.add(s)
                        child.add(values?: arrayListOf())
                    }
                }
                Collections.sort(group)
                Collections.reverse(group)
                LogUtil.e("hhahaha==" + group.size + "ssss==" + child.size)
                if (adapter != null) {
                    for (i in 0 until (adapter?.groupCount ?:0)) {
                        id_lvCheckWork.expandGroup(i)
                        adapter?.notifyDataSetChanged()
                        break
                    }
                }
                LogUtil.e("lllll===" + cowList!!.size)
                isLoadMore = true
                if (cowList!!.size < count) {
                    isLoadMore = false
                    index = 1
                }
                if (cowList!!.size <= 0) {
                    id_llEmpty.visibility = View.VISIBLE
                    id_refreshLayout.visibility = View.GONE
                } else {
                    id_llEmpty.visibility = View.GONE
                    id_refreshLayout.visibility = View.VISIBLE
                }
            }
        }
    }

}