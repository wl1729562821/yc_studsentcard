package cc.manbu.schoolinfocommunication.listener.http

import cc.manbu.schoolinfocommunication.listener.HttpRespnse


/**
 *@Author:余慈
 *@Date:2017/10/26 0026
 *@Description:http回调接口函数
 **/
interface HttpCallListener {

    fun <T> onNext(data: HttpRespnse<T>)

    fun onError(code:Int,msg:String?)

}