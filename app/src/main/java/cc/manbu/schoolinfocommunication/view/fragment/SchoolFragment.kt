package cn.yc.student.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.ViewItemBean
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.view.adapter.ShoolFragmentAdapter
import kotlinx.android.synthetic.main.fragment_school.*

import org.greenrobot.eventbus.EventBus
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList
/**
 * A simple [Fragment] subclass.
 */
class SchoolFragment : BaseFragmentStudent() {
    private val list = ArrayList<ViewItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_school
    }

    override fun initView() {
        val items = getResources().getStringArray(R.array.items_shool_view)
        for (item in items) {
            val views = ViewItemBean()
            views.setName(item)
            list.add(views)
        }
        val adapter = ShoolFragmentAdapter(context, list)
        id_lvShool!!.adapter = adapter
        id_lvShool?.setOnItemClickListener { parent, view, position, id ->
            val event = ViewEvent()
            event.setMessage(Constant.EVENT_SHOW_SCHOOL_ITEM)
            event.setContent(list[position].getName())
            event.setFlg(position)
            EventBus.getDefault().post(event)
        }
    }

}// Required empty public constructor
