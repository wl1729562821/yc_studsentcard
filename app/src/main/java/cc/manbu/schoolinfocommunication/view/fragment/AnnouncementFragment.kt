package cn.yc.student.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import kotlinx.android.synthetic.main.fragment_webview.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import org.xutils.common.util.LogUtil
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.ViewInject
import org.xutils.x
/**
 * A simple [Fragment] subclass.
 */
class AnnouncementFragment : BaseFragmentStudent() {
    private var rootView: View? = null

    val isWebViewCanGoBack: Boolean
        get() = id_webview != null && id_webview.canGoBack()

    override fun getLayoutId(): Int {
        return R.layout.fragment_webview
    }

    override fun onDestroy() {
        unregisterEventbus(this)
        super.onDestroy()
    }

    override fun initView() {
        registerEventbus(this)
        mLoadingDoialog?.show()
        mNetHelper.accessAnnouncementUrl()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("GetAnnouncementUrl" == msg) {
            val result = event.getContent()
            try {
                val `object` = JSONObject(result)
                val url = `object`.getString("d")
                LogUtil.e("urls===" + url)
                val webSettings = id_webview!!.settings
                //设置是否支持缩放
                webSettings.setSupportZoom(false)
                //设置是否显示缩放工具
                webSettings.builtInZoomControls = false
                //表示不支持js
                webSettings.javaScriptEnabled = true
                //把所有内容放到WebView组件等宽的一列中
                webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                //如果页面中链接，如果希望点击链接继续在当前browser中响应，
                // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象
                id_webview.setWebViewClient(object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        if (view.contentHeight != 0) {
                            mLoadingDoialog?.dismiss()
                        }
                    }

                    override fun shouldOverrideUrlLoading(view: WebView, request: String): Boolean {
                        view.loadUrl(request)
                        return true

                    }
                })
                id_webview.setWebChromeClient(object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        if (newProgress == 100) {
                            mLoadingDoialog?.dismiss()
                        }
                    }
                })
                id_webview.loadUrl(url)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }
}// Required empty public constructor
