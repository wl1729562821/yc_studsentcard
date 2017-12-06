package cc.manbu.schoolinfocommunication.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.httputils.NetHelper
import cc.manbu.schoolinfocommunication.view.customer.CustomDialog

/**
 * Created by Administrator on 2017/12/1 0001.
 */
open abstract class BaseMapFragment : Fragment() {
    protected var mGaodeMapView: com.amap.api.maps.MapView? = null
    protected var mGaodeMap: com.amap.api.maps.AMap?=null

    protected var mNetHelper = NetHelper.getInstance()

    protected var mLoadingDoialog: CustomDialog?=null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingDoialog=mLoadingDoialog?: CustomDialog(activity,R.style.CustomDialog)
    }

    /**
     * 高德地图初始化时调用
     * @param savedInstanceState
     */
    protected fun onGaodeMapViewCreate(savedInstanceState: Bundle?) {
        if (mGaodeMapView != null) {
            // *** IMPORTANT ***
            // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
            // objects or sub-Bundles.
            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(GAODE_MAPVIEW_BUNDLE_KEY)
            }
            mGaodeMapView!!.onCreate(mapViewBundle)
            mGaodeMap = mGaodeMapView!!.map
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        onSaveMapViewState(outState)
        super.onSaveInstanceState(outState)
    }

    protected fun onSaveMapViewState(outState: Bundle) {
        if (mGaodeMapView != null) {
            var mapViewBundle = outState.getBundle(GAODE_MAPVIEW_BUNDLE_KEY)
            if (mapViewBundle == null) {
                mapViewBundle = Bundle()
                outState.putBundle(GAODE_MAPVIEW_BUNDLE_KEY, mapViewBundle)
            }
            mGaodeMapView!!.onSaveInstanceState(mapViewBundle)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mGaodeMapView != null) {
            mGaodeMapView!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mGaodeMapView != null) {
            mGaodeMapView!!.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mGaodeMapView != null) {
            mGaodeMapView!!.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mGaodeMapView != null) {
            mGaodeMapView!!.onLowMemory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        protected val GAODE_MAPVIEW_BUNDLE_KEY = "InternalGaodeMapViewBundleKey41968"
    }

    open fun showMessage(msg:String){
        Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()
    }
}// Required empty public constructor
