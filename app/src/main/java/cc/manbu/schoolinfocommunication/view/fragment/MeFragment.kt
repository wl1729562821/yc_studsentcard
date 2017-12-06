package cn.yc.student.view.fragment


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import cc.manbu.schoolinfocommunication.adapter.MeFragmentAdapter

import org.xutils.common.util.LogUtil
import org.xutils.image.ImageOptions
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.bean.ViewItemBean
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.tools.DateUtil
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * A simple [Fragment] subclass.
 */
class MeFragment : BaseFragmentStudent() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initView() {
        val items = resources.getStringArray(R.array.items_me_view)
        val itemsValue = arrayOfNulls<String>(8)
        val users = Configs.get(Configs.Config.CurUser, R_Users::class.java)
        if (users != null) {
            itemsValue[0] = users.userName
            itemsValue[1] = DateUtil.format("yyyy-MM-dd", users.birthday)
            itemsValue[2] = users.tel
            itemsValue[3] = users.address
            itemsValue[4] = users.nation
            itemsValue[5] = users.cardId
            itemsValue[6] = users.politicalAffiliation
            itemsValue[7] = DateUtil.format("yyyy-MM-dd HH:mm", users.joinIime)
            //id_tvLoginName?.text = users.loginName
        }
        val user = Configs.get(Configs.Config.CurUser, R_Users::class.java)
        val bean= ViewItemBean()
        bean.type=20
        if (user != null) {
            val imageUrl = user.u_ImageUrl
            bean.uri=imageUrl
            bean.name=user.loginName
        }
        val list = ArrayList<ViewItemBean>()
        list.add(bean)
        for (i in items.indices) {
            val views = ViewItemBean()
            views.name = items[i]
            views.value = itemsValue[i]
            list.add(views)
        }
        val bean1=ViewItemBean()
        bean1.type=30
        list.add(4,bean1)
        id_lvMe?.layoutManager=LinearLayoutManager(activity)
        for(i in 0..(list.size-1)){
            Log.e(TAG,"initData ${list.get(i)}")
        }
        val adapter = MeFragmentAdapter(context,list)
        id_lvMe?.adapter = adapter
    }

}// Required empty public constructor
