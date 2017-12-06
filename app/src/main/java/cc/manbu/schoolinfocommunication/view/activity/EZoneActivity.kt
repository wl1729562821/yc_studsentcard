package cc.manbu.schoolinfocommunication.view.activity

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.graphics.Color
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import java.util.HashMap
import java.util.HashSet
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.tools.ScreenUtils
import cc.manbu.schoolinfocommunication.tools.Utils
import cc.manbu.schoolinfocommunication.view.fragment.E_ZoneFragment
import cn.yc.student.view.fragment.EZoneFragment
import cn.yc.student.view.fragment.E_ZoneListFragment
import kotlinx.android.synthetic.main.activity_e__zone.*
import kotlinx.android.synthetic.main.layout_head.*

class EZoneActivity : BaseActivityStudent() {

    private var mListFragment:E_ZoneListFragment?=null
    private var mFragment:E_ZoneFragment?=null
    private var isSettingEZone = false
    private var shape = 1
    private var mCurrentTag="E_ZoneListFragment"

    override fun getLayoutId(): Int {
        return R.layout.activity_e__zone
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
    }

    override fun initView() {
        showFragments(mCurrentTag)
        id_tvTitle?.let {
            it.text = getString(R.string.text_e_zone)
        }
        toolbar_title_right?.let {
            it.setOnClickListener {
                toolbar_title_right?.let {
                    if (it.text == getString(R.string.e_zone_type)
                            || it.text.equals(getString(R.string.e_zone_type))) {
                        val radioGroup = RadioGroup(this@EZoneActivity)
                        radioGroup.orientation = LinearLayout.VERTICAL
                        radioGroup.setBackgroundColor(Color.WHITE)
                        val radioButton_polygon = RadioButton(this@EZoneActivity)
                        radioButton_polygon.setText(R.string.e_zone_type_polygon)
                        radioButton_polygon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        radioButton_polygon.setButtonDrawable(android.R.color.transparent)
                        radioButton_polygon.gravity = Gravity.CENTER
                        radioButton_polygon.id = R.id.btn_type_polygon
                        radioButton_polygon.setBackgroundResource(R.drawable.btn_item_selector)
                        val radioButton_circle = RadioButton(this@EZoneActivity)
                        radioButton_circle.setText(R.string.e_zone_type_circle)
                        radioButton_circle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        radioButton_circle.setButtonDrawable(android.R.color.transparent)
                        radioButton_circle.gravity = Gravity.CENTER
                        radioButton_circle.id = R.id.btn_type_circle
                        radioButton_circle.setBackgroundResource(R.drawable.btn_item_selector)
                        radioButton_circle.setPadding(10, 10, 10, 10)
                        radioButton_polygon.setPadding(10, 10, 10, 10)
                        radioGroup.addView(radioButton_polygon, ScreenUtils.dip2px(this@EZoneActivity, 120), ViewGroup.LayoutParams.WRAP_CONTENT)
                        radioGroup.addView(radioButton_circle, ScreenUtils.dip2px(this@EZoneActivity, 120), ViewGroup.LayoutParams.WRAP_CONTENT)
                        if (shape == 1)
                            radioButton_polygon.isChecked = true
                        else
                            radioButton_circle.isChecked = true
                        val popupWindow = PopupWindow(radioGroup,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                        popupWindow.setBackgroundDrawable(PaintDrawable())
                        popupWindow.isFocusable=true
                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            val event = ViewEvent()
                            event.message = Constant.EVENT_E_ZONE_TYPE
                            shape = if (checkedId == R.id.btn_type_polygon) 1 else 2
                            event.flg = shape
                            EventBus.getDefault().post(event)
                            popupWindow.dismiss()
                        }
                        popupWindow.showAsDropDown(it, 0,0)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun showEzoneFragment(){
        if(mCurrentTag=="E_ZoneListFragment"){
            mCurrentTag="E_ZoneFragment"
            showFragments(mCurrentTag)
        }
    }
    private fun showEzoneListFragment(){
        if(mCurrentTag=="E_ZoneFragment"){
            mCurrentTag="E_ZoneListFragment"
            showFragments(mCurrentTag)
        }
    }

    override fun back() {
        if(mCurrentTag=="E_ZoneFragment"){
            mCurrentTag="E_ZoneListFragment"
            showFragments(mCurrentTag)
        }else{
            finish()
        }
    }

    private fun showFragments(tag: String) {
        Log.e(TAG,"showFragments $tag")
        if ("E_ZoneFragment" == tag) {
            val tran=supportFragmentManager.beginTransaction()
            if(mFragment==null){
                mFragment=E_ZoneFragment()
                tran.add(R.id.id_flE_ZoneContent,mFragment)
            }
            mFragment?.run {
                if(mListFragment?.isAdded==true){
                    Log.e(TAG,"showFragments1 删除fragment")
                    tran.hide(mListFragment)
                }

                Log.e(TAG,"showFragments1 显示fragment")
                tran.show(this).commit()
            }
        } else if ("E_ZoneListFragment" == tag) {
            if(mListFragment==null){
                mListFragment= E_ZoneListFragment()
            }
            val tran=supportFragmentManager.beginTransaction()
            mListFragment?.run {
                if(mFragment?.isAdded==true){
                    Log.e(TAG,"showFragments2 删除fragment")
                    tran.hide(mFragment)
                }
                if(!isAdded){
                    Log.e(TAG,"showFragments2 添加fragment")
                    tran.add(R.id.id_flE_ZoneContent,this)
                }
                Log.e(TAG,"showFragments1 显示fragment")
                tran.show(this).commit()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ViewEvent) {
        mLoadingDoialog?.dismiss()
        val msg = event.message
        if (event.type == 2000) {
            toolbar_title_right?.text = getString(R.string.e_zone_type)
            Utils.CurGeography = null
            isSettingEZone = true
            shape = 1
            showEzoneFragment()
            return
        }else if(event.type==3000){
            showEzoneListFragment()
            return
        }
        if (Constant.EVENT_SHOW_GIS == msg) {
            showEzoneFragment()
            id_rlRight?.visibility = View.INVISIBLE
        } else if (Constant.EVENT_HIDE_RIGHT_BUTTON == msg) {
            id_rlRight?.visibility = View.INVISIBLE
        } else if (Constant.EVENT_SHOW_RIGHT_BUTTON == msg) {
            id_rlRight?.visibility = View.VISIBLE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.e(TAG,"onKeyDown $mCurrentTag")
            if(mCurrentTag=="E_ZoneFragment"){
                showEzoneListFragment()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
