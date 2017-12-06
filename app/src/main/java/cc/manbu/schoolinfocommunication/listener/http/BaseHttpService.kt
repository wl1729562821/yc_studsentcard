package cc.manbu.schoolinfocommunication.listener.http

/**
 *@Author:余慈
 *@Date:2017/10/27 0027
 *@Description: 公共网络请求实现类
 **/
open abstract class BaseHttpService {

    protected var cancel=true

    /**
     *@Description:取消网络请求
     *@Params:
     **/
    abstract fun cancelRequest()

    open fun isCancel():Boolean{
        return false
    }
}