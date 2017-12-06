package cn.yc.model.listener

/**
 *@Author:余慈
 *@Date:2017/10/27 0027
 *@Description:item点击事件回调接口
 **/
interface BaseOnItemListener<T> {

    fun itemClick(t:T?)

}