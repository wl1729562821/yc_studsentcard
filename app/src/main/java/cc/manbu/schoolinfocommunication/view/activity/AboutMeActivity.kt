package cc.manbu.schoolinfocommunication.view.activity

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cc.manbu.schoolinfocommunication.adapter.MeFragmentAdapter
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.bean.ViewItemBean
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.tools.DateUtil
import kotlinx.android.synthetic.main.activity_aboutme.*
import kotlinx.android.synthetic.main.layout_head.*
import java.util.ArrayList

/**
 * Created by Administrator on 2017/11/25 0025.
 */
class AboutMeActivity:BaseActivityStudent(){

    override fun getLayoutId(): Int {
        return R.layout.activity_aboutme
    }

    override fun initView() {
        id_tvTitle.setText(R.string.text_main_tab4)
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
        id_lvMe?.layoutManager= LinearLayoutManager(this)
        for(i in 0..(list.size-1)){
            Log.e(TAG,"initData ${list.get(i)}")
        }
        val adapter = MeFragmentAdapter(this,list)
        id_lvMe?.adapter = adapter
    }

}