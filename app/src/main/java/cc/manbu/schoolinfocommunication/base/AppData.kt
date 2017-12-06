package cc.manbu.schoolinfocommunication.base

import android.util.Log

/**
 * Created by Administrator on 2017/12/6 0006.
 */
object AppData {

    var a:String=""

    private var mData:HashMap<String,Any>?=null

    init {
        initData()
    }

    private fun initData(){
        mData= mData?: hashMapOf()
    }

    fun <T> putData(key:String,data:T){
        initData()
        Log.e("AppData","putData ${mData?.size} $key")
        mData?.run {
            put(key,data as Any)
        }
    }

    fun <T> getData(key: String):T?{
        Log.e("AppData","getData ${mData?.size} $key")
        return if(mData?.get(key)!=null){
            (mData?.get(key)) as? T
        }else{
            null
        }
    }

    fun clear(){
        mData?.clear()
    }
}
