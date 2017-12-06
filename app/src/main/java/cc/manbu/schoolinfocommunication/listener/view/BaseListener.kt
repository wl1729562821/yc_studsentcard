package cn.yc.model.listener

import android.os.Bundle
import android.view.KeyEvent
import android.view.View

/**
 * Created by Administrator on 2017/11/6 0006.
 */
 interface BaseListener{

    fun initView()

    fun onKey(keyCode: Int, event: KeyEvent?): Boolean{
        return false
    }

    fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean{
        return false
    }

    fun showMessage(message:String)

    fun back()

    fun  getLayoutId():Int

    fun init(savedInstanceState: Bundle?)



}