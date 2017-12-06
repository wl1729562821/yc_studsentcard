package cc.manbu.schoolinfocommunication.base

import android.app.Fragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cc.manbu.schoolinfocommunication.httputils.NetHelper
import cc.manbu.schoolinfocommunication.view.customer.CustomDialog
import cn.yc.model.listener.BaseFragmentListener
import cc.manbu.schoolinfocommunication.R
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Administrator on 2017/12/1 0001.
 */
open abstract class BaseFragmentStudent: android.support.v4.app.Fragment(), BaseFragmentListener {
    protected var mNetHelper = NetHelper.getInstance()

    protected var mLoadingDoialog: CustomDialog?=null

    protected var TAG=""

    var displayWidth = 0
        private set
        get() {
            val display = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(display)
            return display.widthPixels
        }
    var displayHeight = 0
        private set
        get(){
            val display = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(display)
            return display.heightPixels
        }

    init {
        TAG=javaClass.simpleName
    }

    private var mRoot: View?=null

    final override fun onCreateView(inflater: LayoutInflater?,
                                    container: ViewGroup?,
                                    savedInstanceState: Bundle?): View? {
        mRoot?.parent?.let {
            (it as? ViewGroup)?.let {
                it.removeAllViews()
            }
        }
        mRoot=inflater?.inflate(getLayoutId(),null)
        initRoot(mRoot,savedInstanceState)
        return mRoot
    }

    final override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun showMessage(message: String) {
        Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
    }

    open fun initRoot(root: View?, savedInstanceState: Bundle?){}

    open fun dispatchTouchEvent(ev: MotionEvent):Boolean{
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        mLoadingDoialog=mLoadingDoialog?: CustomDialog(activity, R.style.CustomDialog)
    }

    override fun back() {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}