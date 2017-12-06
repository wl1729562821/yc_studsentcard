package cn.yc.student.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.Sleave
import cc.manbu.schoolinfocommunication.tools.DateUtil

import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.Date
import kotlinx.android.synthetic.main.fragment_off_deatils.*

/**
 * A simple [Fragment] subclass.
 */
class OffDeatilsFragment : BaseFragmentStudent() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_off_deatils
    }

    override fun initView() {
        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {}

    private fun init() {
        val mBoundle = arguments
        val sleave = mBoundle.getSerializable("sleave") as Sleave
        if (sleave != null) {
            val state = sleave!!.getState()
            val addDate = sleave!!.getAddDate()
            val starDate = sleave!!.getStartTime()
            val endDate = sleave!!.getEndTime()
            when (state) {
                0//未审核
                -> {
                    id_tvState.setText(R.string.text_reading)
                    id_tvState.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                }
                1//未通过
                -> {
                    id_tvState.setText(R.string.text_pass)
                    id_tvState.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                2//通过
                -> {
                    id_tvState.setText(R.string.text_passed)
                    id_tvState.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            id_tvAddDate.setText(DateUtil.format("yyyy-MM-dd HH:mm:ss EE", addDate))
            id_tvRemark.setText(sleave!!.getRemark())
            id_tvReson.setText(sleave!!.getReason())
            id_tvDate.setText(DateUtil.format("yyyy/MM/dd HH:mm:ss", starDate) + " ----- " +
                    DateUtil.format("yyyy/MM/dd HH:mm:ss", endDate))
        }
    }
}// Required empty public constructor
