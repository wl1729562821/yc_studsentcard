package cc.manbu.schoolinfocommunication.base

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cc.manbu.schoolinfocommunication.R
import org.greenrobot.eventbus.EventBus

var TAG:String=""
fun registerEventbus(any: Any){
    if (!EventBus.getDefault().isRegistered(any)) {
        EventBus.getDefault().register(any)
    }
}

fun unregisterEventbus(any: Any){
    if (EventBus.getDefault().isRegistered(any)) {
        EventBus.getDefault().unregister(any)
    }
}

fun swipeRefreshLayoutInit(layout: SwipeRefreshLayout){
    with(layout){
        setColorScheme(R.color.tab_bg)
        setProgressBackgroundColorSchemeResource(android.R.color.white)
    }
}
fun  recyclerViewInit(rv: RecyclerView, type:Int){
    when(type){
        0 -> rv.layoutManager= LinearLayoutManager(rv.context)
        1-> rv.layoutManager= GridLayoutManager(rv.context,2)
    }
    rv.overScrollMode= RecyclerView.OVER_SCROLL_NEVER
}

fun byteArrayToHexString(data: ByteArray): String {
    val sb = StringBuffer()
    for (b in data) {
        var hex = Integer.toHexString(b.toInt() and 0xFF)
        if (hex.length == 1) {
            hex = '0' + hex
        }
        sb.append(hex)
    }
    return sb.toString().toUpperCase()
}