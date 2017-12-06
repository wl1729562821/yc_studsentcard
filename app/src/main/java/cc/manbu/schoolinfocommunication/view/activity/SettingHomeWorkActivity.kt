package cc.manbu.schoolinfocommunication.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView

import com.jaeger.library.StatusBarUtil

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.Hw_Homework
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.view.adapter.RealeseHomeWorkAdapter
import kotlinx.android.synthetic.main.activity_setting_home_work.*
import kotlinx.android.synthetic.main.layout_head.*

class SettingHomeWorkActivity : BaseActivityStudent() {

    var list: MutableList<Hw_Homework> = ArrayList()
    var mAdapter: RealeseHomeWorkAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_setting_home_work
    }

    override fun initView() {
        init()
        mLoadingDoialog!!.show()
        mNetHelper.accessHomeWorkByTeacher(0, 20)

    }

    fun init() {
        id_tvTitle?.setText(R.string.text_homework)
        id_tvRight?.run {
            id_rlRight?.visibility=View.VISIBLE
            setText(R.string.text_realse_homework)
            visibility = View.VISIBLE
            setOnClickListener {
                val intent = Intent(this@SettingHomeWorkActivity, AssignHomeworkActivity::class.java)
                startActivity(intent)
            }
        }
        id_refreshLayout?.run {
            setColorSchemeResources(R.color.chocolate,
                    R.color.hotpink,
                    R.color.crimson,
                    R.color.orchid)
            setOnRefreshListener {
                mNetHelper.accessHomeWorkByTeacher(0, 20)
            }
            setProgressViewEndTarget(true, 120)//设置距离顶端的距离
        }
        mAdapter = RealeseHomeWorkAdapter(list, this@SettingHomeWorkActivity)
        id_lvSettingHomeWork?.run {
            setOnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@SettingHomeWorkActivity, HomeWorkDetailActivity::class.java)
                intent.putExtra("homework", list[position])
                startActivity(intent)
            }
            adapter = mAdapter
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ResposeEvent) {
        val msg = event.message
        if ("GetHomeworkByTeacher" == msg) {
            mLoadingDoialog!!.dismiss()
            id_refreshLayout!!.isRefreshing = false
            val datas = event.hw_homeworks
            list.clear()
            list.addAll(datas)
            mAdapter?.notifyDataSetChanged()
        }
    }
}
