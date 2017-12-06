package cn.yc.student.view.fragment

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.*
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cn.yc.student.bean.DrawableBean
import com.qihoo360.replugin.RePlugin
import kotlinx.android.synthetic.main.fragment_student.*
import java.io.File


/**
 * Created by Administrator on 2017/11/8 0008.
 */
class StudentOneFragment : BaseFragmentStudent() {

    private var mList: ArrayList<DrawableBean>? = null
    private var mShow=false

    companion object {

        private var STUDENT_FRAGMENT_BUNDLE_KEY = "drawableList"

        fun newFragment(key:String,drawableList: ArrayList<DrawableBean>?): StudentOneFragment {
            val fragment = StudentOneFragment()
            var bundle = Bundle()
            if (fragment.arguments == null) {
                fragment.arguments = bundle
            } else {
                bundle = fragment.arguments
            }
            bundle.putString(STUDENT_FRAGMENT_BUNDLE_KEY,key)
            drawableList?.run {
                AppData.putData(key,this)
            }
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_student
    }

    override fun initView() {
        arguments?.getString(STUDENT_FRAGMENT_BUNDLE_KEY)?.run {
            AppData.getData<ArrayList<DrawableBean>>(this)
        }?.run {
            mList = this
            if (size > 0) {
                (0..(size-1)).forEach {
                    setData(get(it),it)
                }
                student_img1?.setOnClickListener(mListener)
                student_img2?.setOnClickListener(mListener)
                student_img3?.setOnClickListener(mListener)
                student_img4?.setOnClickListener(mListener)
                student_img5?.setOnClickListener(mListener)
                student_img6?.setOnClickListener(mListener)
            }
        }
    }

    private val mListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let {
                mList?.run {
                    val startIntent:(Any)->Unit={
                        activityNmae->
                        var intent=Intent()
                        if(activityNmae is String){
                            mLoadingDoialog?.show()
                            mShow=true
                            val intent = Intent()
                            intent.component = ComponentName("MusicModule",
                                    "cn.yc.music.view.activity.EarlyeducationActivity")
                            RePlugin.startActivityForResult(activity, intent,2, null)

                        }else{
                            intent.setClass(activity, activityNmae as Class<*>?)
                            startActivity(intent)
                        }

                    }
                    when (it.id) {
                        R.id.student_img1 ->{
                            Log.e(TAG,"onClick ${get(0).activityNmae}")
                            get(0).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        R.id.student_img2 ->{
                            Log.e(TAG,"onClick ${get(1).activityNmae}")
                            get(1).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        R.id.student_img3 ->{
                            Log.e(TAG,"onClick ${get(2).activityNmae}")
                            get(2).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        R.id.student_img4 ->{
                            Log.e(TAG,"onClick ${get(3).activityNmae}")
                            get(3).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        R.id.student_img5 ->{
                            Log.e(TAG,"onClick ${get(4).activityNmae}")
                            get(4).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        R.id.student_img6 ->{
                            Log.e(TAG,"onClick ${get(5).activityNmae}")
                            get(5).activityNmae?.run {
                                startIntent(this)
                            }
                        }
                        else->{

                        }
                    }
                }

            }
        }
    }

    private fun setData(data: DrawableBean, index: Int) {
        when (index) {
            0 ->
                setData(data, student_img1, student_title1)
            1 ->
                setData(data, student_img2, student_title2)
            2 ->
                setData(data, student_img3, student_title3)
            3 ->
                setData(data, student_img4, student_title4)
            4 ->
                setData(data, student_img5, student_title5)
            5 ->
                setData(data, student_img6, student_title6)
        }
    }

    private fun setData(data: DrawableBean, image: ImageView, tv: TextView) {
        image.setBackgroundResource(data.drawableId)
        tv.text = data.title
    }

    override fun onPause() {
        super.onPause()
        mLoadingDoialog?.dismiss()
        mShow=false
    }

    override fun onKey(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mLoadingDoialog?.isShowing==true && mShow){
                return true
            }
        }
        return super.onKey(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}