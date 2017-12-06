package cn.yc.student.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_e__zone.*
import android.text.TextUtils
import android.R.attr.strokeWidth
import android.R.attr.fillColor
import android.R.attr.strokeColor
import android.R.attr.radius
import android.R.attr.radius
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import cn.yc.base.view.custom.percent.PercentLinearLayout
import cn.yc.base.view.custom.percent.PercentRelativeLayout
import com.amap.api.mapcore.util.i
import com.amap.api.maps.model.*
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.services.core.LatLonPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.bean.Device_Geography
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.listener.HttpRespnse
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener
import cc.manbu.schoolinfocommunication.tools.Utils

/**
 * Created by Administrator on 2017/11/30 0030.
 */
class EZoneFragment:BaseFragmentStudent(){

    private var mMapView: MapView? = null
    private var mAMap:AMap?=null
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null //定位客户端
    private var mLocationOption: AMapLocationClientOption? = null //定位参数

    private var curDevice: MobileDevicAndLocation? = null
    private var geography: Device_Geography? = null
    private var fencePoints: ArrayList<LatLng> = ArrayList()
    private var currentIndex = -1
    private var radius = 500//半径，最小500，单位：米
    private var zoom=15
    private var polyline:Polygon?=null
    private var marker:Marker?=null
    private var mTime: CountDownTimer? = null
    private var markerList = ArrayList<BitmapDescriptor>()
    private var shape=1
    private var isAdd=true
    private var isFirst=true
    private var circle: Circle? = null
    private val mHandler=BaseHandler()
    private var nameString: String?=null
    private var alertType=2

    inner class BaseHandler:Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            circle?.remove()
            addCircle()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ezone
    }

    override fun initRoot(root: View?, savedInstanceState: Bundle?) {
        super.initRoot(root, savedInstanceState)
        registerEventbus(this)
        Log.e(TAG,"initRoot1 $root $savedInstanceState")
        root?.let { root->
            ButterKnife.bind(this,root)
            val mapView=root.findViewById(R.id.map_view) as? MapView
            mapView?.run {
                mMapView=this
                Log.e(TAG,"initRoot3 $mMapView ${mMapView?.map} $map")
                mMapView
            }?.run {
                curDevice = Configs.get(Configs.Config.CurDevice, MobileDevicAndLocation::class.java)
                geography = Device_Geography()

                Log.e(TAG,"initRoot5 $savedInstanceState")
                savedInstanceState?.let {
                    Log.e(TAG,"initRoot6 ${it.getBundle("InternalGaodeMapViewBundleKey41968")}")
                    onCreate(it.getBundle("InternalGaodeMapViewBundleKey41968"))
                    return
                }
                onCreate(savedInstanceState)
                mAMap=map
                Log.e(TAG,"initRoot5 $mAMap")
            }
        }
    }

    override fun initView() {
        zone_alert.alpha = 0.6f
        //id_tvAddress?.text = curDevice?.address
        id_tvAddress?.text = "正在定位"
        if (Utils.CurGeography != null){
            isAdd = false
            zone_alert.visibility = View.GONE
            id_llBottom.visibility = View.GONE
            id_llSeekbar.visibility = View.GONE
            showGIS()
        }else {
            isAdd = true
            zone_alert.visibility = View.VISIBLE
            id_llBottom.visibility = View.VISIBLE
        }
        id_tvClear.setOnClickListener {
            if (fencePoints.size > 0)
                doClear()
            radius = 500
        }
        id_sbRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                radius = 500 + progress * 5
                mHandler.sendEmptyMessage(0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        id_llZoneName?.setOnClickListener {
            showPopupWindowDialog(root)
        }
        id_tvSetArea?.setOnClickListener {
            nameString = id_tvZoneName.text.toString()
            geography = Utils.CurGeography
            if (geography == null){
                if (nameString.equals(resources.getString(R.string.text_e_zone_name))){
                    showMessage(resources.getString(R.string.text_tips_input_zone_name))
                }else {
                    if(fencePoints.size <= 2 && shape==1){
                        //shape = 2;
                        showMessage(resources.getString(R.string.text_points_too_less))
                    }else {
//                            shape = 1;
//                            radius = 0;
                        doSave()
                    }
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){
            doClear()
            initView()
            isFirst = true
            initDeviceInMap()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
        Log.e(TAG,"onResume $mMapView ${mMapView?.map}")
        if (markerList.isEmpty()){
            addMarkers()
        }
        setUpMap()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
        clearMap()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycleMarker()
        root?.removeAllViews()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        recycleMarker()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mMapView?.run {
            var mapViewBundle = outState.getBundle("InternalGaodeMapViewBundleKey41968")
            if (mapViewBundle == null) {
                mapViewBundle = Bundle()
                outState.putBundle("InternalGaodeMapViewBundleKey41968",mapViewBundle)
            }
            onSaveInstanceState(mapViewBundle)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
        mTime=null
        mMapView?.onDestroy()
        super.onDestroy()
        unregisterEventbus(this)
    }

    private fun showGIS() {
        Utils.CurGeography?.run {
            val pointStr = Utils.CurGeography.geography
            if (!pointStr.isEmpty()) {
                val points = pointStr.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (point1 in points) {
                    val point = point1.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val latLng = LatLng(java.lang.Double.parseDouble(point[0]), java.lang.Double.parseDouble(point[1]))
                    fencePoints.add(latLng)
                    currentIndex++
                }
            }
        }

        val count = fencePoints.size
        if (count in 1..2) {
            addCircle()
        }
        if (count >= 3) {
            addPolygon()
        }
        mAMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(fencePoints[0], zoom.toFloat()))
    }

    //圆形围栏
    private fun addCircle() {
        Log.e(TAG,"addCircle() $curDevice")
        if(currentIndex == -1 && curDevice != null){
            val latLng = LatLng(curDevice?.offsetLat?:0.0,curDevice?.offsetLng?:0.0)
            fencePoints.add(latLng)
            currentIndex = -2
        }
        if(currentIndex == 1){
            fencePoints.isNotEmpty().run {
                radius = AMapUtils.calculateLineDistance(
                        fencePoints[0], fencePoints[1]).toInt()
                if (radius < 500){
                    radius = 500
                }
                if(radius>1000){
                    radius = 1000
                }
            }
        }
        Log.e(TAG,"addCircle() ${fencePoints.isNotEmpty()}$")
        if(fencePoints.isNotEmpty()){
            // 绘制一个圆形
            circle = mAMap?.addCircle(CircleOptions().center(fencePoints[0])
                    .radius(radius.toDouble()).strokeColor(strokeColor).fillColor(fillColor)
                    .strokeWidth(strokeWidth.toFloat()))
            Log.e(TAG,"addCircle() $circle ${fencePoints[0]}")
        }
    }

    private fun doClear() {
        currentIndex = -1
        fencePoints.clear()
        refreshMap()
    }

    private fun refreshMap() {
        Log.e(TAG,"refreshMap $currentIndex")
        if (circle != null || polyline != null) {
            mAMap?.clear()
        }
        initDeviceInMap()
        if (currentIndex < 2 && currentIndex > -1) {
            Log.e(TAG,"refreshMap addCircle")
            addCircle()
        }
        if (currentIndex >= 2) {
            Log.e(TAG,"refreshMap addPolygon")
            addPolygon()
            id_llSeekbar.visibility = View.GONE
        }
    }
    private fun initDeviceInMap() {
        curDevice?.run {
            val latLng=LatLng(offsetLat, offsetLng)
            val mop = MarkerOptions().position(latLng)
                    .title(deviecName)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark0))
                    .anchor(0.5f, 0.5f)
            marker?.remove()
            marker = mAMap?.addMarker(mop)
            marker?.position = latLng
            marker?.title = deviecName
            if (isFirst){
                isFirst = false
                mAMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))
            }
        }
    }

    //多边形围栏
    fun addPolygon() {
        val options = PolygonOptions()
        (0..currentIndex).mapTo(options.points){fencePoints[it]}
        if(fencePoints.isNotEmpty()){
            options.add(fencePoints[0])
        }
        polyline = mAMap?.addPolygon(options.strokeWidth(strokeWidth.toFloat())
                .strokeColor(strokeColor).fillColor(fillColor))
    }

    private fun addMarkers() {
        markerList.clear()
        (0..10).mapTo(markerList){
            val id = context.resources.getIdentifier("mark" + it, "drawable", Configs.APP_PACKAGE_NAME)
            BitmapDescriptorFactory.fromResource(id)
        }
        for (i in 0..10) {
            val id = context.resources.getIdentifier("mark" + i, "drawable", Configs.APP_PACKAGE_NAME)
            markerList.add(BitmapDescriptorFactory.fromResource(id))
        }
    }

    private fun setUpMap() {
        mAMap=mAMap?:mMapView?.map
        Log.e(TAG,"setUpMap $mAMap")
        mAMap?.run {
            Log.e(TAG,"setUpMap1")
            uiSettings?.run {
                Log.e(TAG,"setUpMap2")
                isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
                isZoomControlsEnabled = false//隐藏缩放控件
            }
            val  myLocationStyle=MyLocationStyle()
            myLocationStyle.interval(3000)
            //定位一次，且将视角移动到地图中心点
            myLocationStyle.showMyLocation(true)
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
            setMyLocationStyle(myLocationStyle)
            isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            setLocationSource(object :LocationSource{
                override fun activate(listener: LocationSource.OnLocationChangedListener?) {
                    Log.e(TAG,"activate 开始定位 $listener")
                    mListener = listener
                    if (mlocationClient == null) {
                        mlocationClient = AMapLocationClient(activity)
                        mLocationOption = AMapLocationClientOption()
                        mLocationOption?.run {
                            isOnceLocation = true
                            //设置为高精度定位模式
                            setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                        }
                        mlocationClient?.run {
                            //设置定位监听
                            setLocationListener {amapLocation->
                                Log.e(TAG,"onLocationChanged $mListener")
                                if (mListener != null && amapLocation != null) {
                                    if (amapLocation != null && amapLocation.errorCode == 0) {
                                        mListener?.onLocationChanged(amapLocation)
                                        val curLatlng = LatLng(amapLocation.latitude, amapLocation.longitude)
                                        mAMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f))

                                    } else {
                                        val errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo()
                                        Log.e("AmapErr", errText)
                                    }
                                }
                            }
                            //设置定位参数
                            setLocationOption(mLocationOption)
                            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                            // 在定位结束后，在合适的生命周期调用onDestroy()方法
                            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                            startLocation()
                        }
                    }
                }

                override fun deactivate() {
                    Log.e(TAG,"deactivate() 停止定位")
                    mListener = null
                    mlocationClient?.stopLocation()
                    mlocationClient?.onDestroy()
                    mlocationClient = null
                }

            })
            // 对amap添加单击地图事件监听器
            setOnMapClickListener {
                Log.e(TAG,"onMapClick1 $isAdd $currentIndex")
                if (isAdd){
                    if (currentIndex < -1){
                        currentIndex = -1
                    }
                    currentIndex++
                    if (currentIndex > -1){
                        if(shape==2){
                            fencePoints.clear()
                            currentIndex = 0
                        }
                        fencePoints.add(currentIndex,it)
                        refreshMap()
                    }
                }
            }
            setOnCameraChangeListener(object :AMap.OnCameraChangeListener{
                override fun onCameraChange(p0: CameraPosition?) {
                    p0?.let {
                        zoom =it.zoom.toInt()
                    }
                }

                override fun onCameraChangeFinish(p0: CameraPosition?) {
                    p0?.let {
                        zoom =it.zoom.toInt()
                    }
                }
            })
            initDeviceInMap()
        }
    }

    private fun clearMap() {
        mAMap?.clear()
        marker = null
    }

    private fun recycleMarker(){
        markerList.forEach {
            it.recycle()
        }
        markerList.clear()
    }

    private fun doSave(){
        if (fencePoints != null && fencePoints.size > 0) {
            // 圆形只保存一个点
            if (fencePoints.size <= 2) {
                if (fencePoints.size == 2)
                    fencePoints.removeAt(1)
                if (fencePoints.size == 0){
                    curDevice?.run {
                        fencePoints.add(LatLng(offsetLat,offsetLng))
                    }
                }
            }
            var strList =""
            (0 until fencePoints.size).map { fencePoints[it] }.forEach {
                strList += "$it.latitude,$it.longitude;"
            }
            val user = Configs.get(Configs.Config.CurUser, R_Users::class.java)
            user?.let {
                geography = Device_Geography()
                geography?.run {
                    geography = strList
                    _id = UUID.randomUUID().toString()
                    serialnumber=if(curDevice != null){
                        curDevice?.serialnumber
                    }else{
                        it.loginName
                    }
                    createTime = Date()
                    name = nameString
                    setType(alertType)
                    shape = shape
                    if(shape==2){
                        radius = radius
                    }
                }
                mLoadingDoialog?.show()
                mNetHelper.saveGeography(geography,isAdd)
                return
            }
            showMessage(resources.getString(R.string.text_no_user_msg))
        }else {
            showMessage(resources.getString(R.string.draw_electronicfence))
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEventMainThread(event: ViewEvent){
        val msg = event.message
        if (Constant.EVENT_CLEAR_E_ZONE_SHAPES == msg){
            if (fencePoints.size > 0)
                doClear()
            radius = 500
        }else if(Constant.EVENT_E_ZONE_TYPE == msg){
            shape = event.flg
            clearMap()
            fencePoints.clear()
            initDeviceInMap()
            if(shape==1){
                zone_alert.visibility = View.VISIBLE
                id_llSeekbar.visibility = View.GONE
            }else {
                zone_alert.visibility = View.GONE
                id_llSeekbar.visibility = View.VISIBLE
                currentIndex = -1
                addCircle()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEventMainThread(event: ResposeEvent){
        val msg = event.message
        if ("SaveOrUpdateGeography" == msg){
            mNetHelper.accessE_ZoneList()
            mLoadingDoialog?.dismiss()
            doClear()
            Utils.isSuccess = true
            showMessage(resources.getString(R.string.text_zone_add_success))
            mTime=object:CountDownTimer(1000,2000) {
                override fun onFinish() {
                    val viewEvent=ViewEvent()
                    viewEvent.type=3000
                    EventBus.getDefault().post(viewEvent)
                }

                override fun onTick(millisUntilFinished: Long) {

                }
            }.start()
        }
    }

    private var mRadio = HashMap<Int, Boolean>()
    fun showPopupWindowDialog(view: View) {
        if (mRadio.size <= 0) {
            mRadio.put(0, true)
            mRadio.put(1, false)
            mRadio.put(0, false)
        }
        val wm = activity.windowManager
        val width = wm.defaultDisplay.width
        val contentView = View.inflate(activity, R.layout.pw_zone, null)
        val id_buttonCancel = contentView.findViewById(R.id.pw_zone_cancel) as TextView
        val id_buttonSend = contentView.findViewById(R.id.pw_zone_ok) as TextView
        val pl1 = contentView.findViewById(R.id.pw_zone_check_parent1) as PercentLinearLayout
        val imag1 = contentView.findViewById(R.id.pw_zone_check1) as ImageView
        val imag2 = contentView.findViewById(R.id.pw_zone_check2) as ImageView
        val imag3 = contentView.findViewById(R.id.pw_zone_check3) as ImageView
        val pl2 = contentView.findViewById(R.id.pw_zone_check_parent2) as PercentLinearLayout
        val pl3 = contentView.findViewById(R.id.pw_zone_check_parent3) as PercentLinearLayout
        val clear = contentView.findViewById(R.id.pw_zone_clear) as PercentRelativeLayout
        pl1.setOnClickListener {
            imag1.setBackgroundResource(R.drawable.radio_selected)
            imag2.setBackgroundResource(R.drawable.radio)
            imag3.setBackgroundResource(R.drawable.radio)
            mRadio.put(0, true)
            mRadio.put(1, false)
            mRadio.put(0, false)
            alertType = 0
        }
        pl2.setOnClickListener {
            imag2.setBackgroundResource(R.drawable.radio_selected)
            imag1.setBackgroundResource(R.drawable.radio)
            imag3.setBackgroundResource(R.drawable.radio)
            mRadio.put(0, false)
            mRadio.put(1, true)
            mRadio.put(0, false)
            alertType = 1
        }

        pl3.setOnClickListener {
            imag3.setBackgroundResource(R.drawable.radio_selected)
            imag1.setBackgroundResource(R.drawable.radio)
            imag2.setBackgroundResource(R.drawable.radio)
            mRadio.put(0, false)
            mRadio.put(1, false)
            mRadio.put(0, true)
            alertType = 2
        }
        val id_edZoneName = contentView.findViewById(R.id.pw_zone_ed) as EditText
        clear.setOnClickListener { id_edZoneName.setText(null) }
        id_edZoneName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.length > 0) {
                    clear.visibility = View.VISIBLE
                } else {
                    clear.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        val popupWindow = PopupWindow(contentView,
                (width * 0.88).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.animationStyle = R.style.popwin_anim_style
        id_buttonCancel.setOnClickListener { popupWindow.dismiss() }
        id_buttonSend.setOnClickListener(View.OnClickListener {
            val name = id_edZoneName.text.toString().trim { it <= ' ' }
            if (name == "") {
                showMessage(resources.getString(R.string.text_tips_input_zone_name))
                return@OnClickListener
            }
            for (dg in Utils.fenceList) {
                val oriName = dg.name.trim { it <= ' ' }
                if (name == oriName) {
                    showMessage(resources.getString(R.string.text_tips_zone_name_exits))
                    return@OnClickListener
                }
            }
            nameString = name
            id_tvZoneName.text = name
            popupWindow.dismiss()
        })
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        //        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

}