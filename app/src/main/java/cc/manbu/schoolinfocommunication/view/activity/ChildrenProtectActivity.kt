package cc.manbu.schoolinfocommunication.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

import com.jaeger.library.StatusBarUtil

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.MobileDevicAndLocation
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.bean.ViewItemBean
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.config.DeviceType
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.view.activity.*
import cc.manbu.schoolinfocommunication.view.adapter.ChildrenProtectAdapter
import kotlinx.android.synthetic.main.activity_children_protect.*
import kotlinx.android.synthetic.main.layout_head.*


class ChildrenProtectActivity : BaseActivityStudent() {

    private var devicAndLocation: MobileDevicAndLocation? = null
    private var mode = 1
    private var state = 0
    private var isFirst = false
    private var isFirstClassTime = false
    private var isFirstGPS = false
    private var isFirstKeyLock = false
    private var adapter: ChildrenProtectAdapter? = null
    private val list = ArrayList<ViewItemBean>()

    override fun getLayoutId(): Int {
        return R.layout.activity_children_protect
    }

    override fun initView() {
        ButterKnife.bind(this)
        devicAndLocation = Configs.get(Configs.Config.CurDevice, MobileDevicAndLocation::class.java)
        init()
        mLoadingDoialog!!.show()
        mNetHelper.getDeviceDetial()
    }

    private fun init() {
        id_tvTitle?.setText(R.string.text_children)
        id_lvList?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val ids = list[position].id
                when (ids) {
                    R.string.text_sleep_on_time -> {
                        val mIntent = Intent(this@ChildrenProtectActivity, TimerActivity::class.java)
                        startActivity(mIntent)
                    }
                    R.string.text_kinship -> {
                        val intent = Intent(this@ChildrenProtectActivity, RelativesActivity::class.java)
                        startActivity(intent)
                    }
                    R.string.text_search_dev -> {
                        mLoadingDoialog!!.show()
                        mNetHelper.findDevice()
                    }
                    R.string.text_loacte_fast -> {
                        mLoadingDoialog!!.show()
                        mNetHelper.serachLocate()
                    }
                    R.string.text_loacte_mode -> showModeDialog(true)
                    R.string.text_remote -> showModeDialog(false)
                    R.string.text_reset -> {
                        mLoadingDoialog!!.show()
                        mNetHelper.resetDevice()
                    }
                    R.string.text_upload_time -> {
                        val intent = Intent(this@ChildrenProtectActivity, IntervalActivity::class.java)
                        startActivity(intent)
                    }
                    R.string.text_scence_mode -> {
                        val intent = Intent(this@ChildrenProtectActivity, SceneModeActivity::class.java)
                        startActivity(intent)
                    }
                    R.string.text_listener -> {
                        val intent = Intent(this@ChildrenProtectActivity, ListenActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
        val address = String.format(resources.getString(R.string.text_cur_address),
                devicAndLocation?.address)
        id_tvAddress!!.text = address

        if (Configs.curDeviceType == DeviceType.StudentCard) {
            val names = intArrayOf(/*R.string.text_kinship,*/R.string.text_upload_time, /*R.string.text_scence_mode,*/
                    R.string.text_class_time, R.string.text_fair_wall, R.string.text_gps, R.string.text_loacte_fast)
            filledArray(names)
        } else if (Configs.curDeviceType == DeviceType.S520Watch) {
            val names = intArrayOf(/*R.string.text_kinship,*/R.string.text_listener, R.string.text_fair_wall, R.string.text_search_dev, R.string.text_loacte_fast, R.string.text_loacte_mode, R.string.text_reset)
            filledArray(names)
        } else if (Configs.curDeviceType == DeviceType.S520Watch2_without_Wifi || Configs.curDeviceType == DeviceType.S520Watch2) {
            val names = intArrayOf(/*R.string.text_kinship,*/R.string.text_upload_time, R.string.text_listener, /*R.string.text_sleep_on_time,*/
                    R.string.text_fair_wall, R.string.text_search_dev, R.string.text_loacte_fast, R.string.text_loacte_mode, R.string.text_remote, R.string.text_reset)
            filledArray(names)
        } else if (Configs.curDeviceType == DeviceType.S582Watch || Configs.curDeviceType == 44) {// 44,521为成人版手表，在此app无意义
            val names = intArrayOf(/*R.string.text_kinship,*/R.string.text_listener, R.string.text_lock, R.string.text_loacte_fast,
                    /*R.string.text_sleep_on_time,*/
                    R.string.text_remote, R.string.text_reset)
            filledArray(names)
        } else {
            val names = intArrayOf(/*R.string.text_kinship,*/R.string.text_upload_time, R.string.text_listener, R.string.text_fair_wall, R.string.text_search_dev, R.string.text_loacte_fast, R.string.text_loacte_mode, /*R.string.text_sleep_on_time,*/
                    R.string.text_remote, R.string.text_reset)
            filledArray(names)
        }
    }

    private fun filledArray(arr: IntArray) {
        for (anArr in arr) {
            val bean = ViewItemBean()
            bean.id = anArr
            bean.state = 0
            bean.stateClassTime = 0
            bean.stateGPS = 0
            list.add(bean)
        }
        adapter = ChildrenProtectAdapter(list, this)
        id_lvList!!.adapter = adapter

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ViewEvent) {
        val msg = event.message
        val isChecked = event.isCheck
        if (Constant.EVENT_FAIR_WALL == msg) {
            if (!isFirst) {
                mLoadingDoialog!!.show()
                if (Configs.curDeviceType == DeviceType.StudentCard)
                    mNetHelper.setFairWall(isChecked)
                else
                    mNetHelper.setFiarwall(Configs.getCurDeviceSerialnumber(), isChecked)
            }
            isFirst = false
        } else if (Constant.EVENT_CLASS_TIME == msg) {
            if (!isFirstClassTime) {
                mLoadingDoialog!!.show()
                mNetHelper.setClassTime(Configs.getCurDeviceSerialnumber(), isChecked)
            }
            isFirstClassTime = false
        } else if (Constant.EVENT_GPS_OPEN == msg) {
            if (!isFirstGPS) {
                mLoadingDoialog!!.show()
                mNetHelper.setGPS(Configs.getCurDeviceSerialnumber(), isChecked)
            }
            isFirstGPS = false
        } else if (Constant.EVENT_KEY_LOCK == msg) {
            if (!isFirstKeyLock) {
                mLoadingDoialog!!.show()
                mNetHelper.setKeyLock(Configs.getCurDeviceSerialnumber(), isChecked)
            }
            isFirstKeyLock = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventMainThread(event: ResposeEvent) {
        val msg = event.message
        if ("FireWall" == msg) {
            mLoadingDoialog!!.dismiss()
            val result = event.content
            try {
                val `object` = JSONObject(result)
                val isSuccess = `object`.getBoolean("d")
                if (isSuccess) {
                    showMessage(resources.getString(R.string.text_fairwall))
                } else {
                    showMessage(resources.getString(R.string.text_fairwall_failed))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if ("GetDeviceDetial" == msg) {
            val d = event.device
            mLoadingDoialog!!.dismiss()
            if (d != null) {
                val config = d.shX007Device_Config
                val onfigs520 = d.shX520Device_Config
                if (config != null) {
                    val firewallState = config.fanghuoqiang
                    val classTimeOpen = config.isClassTimeOpen
                    val gpsState = config.gpsState
                    for (i in list.indices) {
                        val b = list[i]
                        if (b.id == R.string.text_fair_wall) {
                            if ("1" == firewallState || "true" == firewallState) {
                                list[i].state = 1
                                isFirst = true
                            } else {
                                list[i].state = 0
                                isFirst = false
                            }
                        }
                        if (b.id == R.string.text_class_time) {
                            if (classTimeOpen) {
                                list[i].stateClassTime = 1
                                isFirstClassTime = true
                            } else {
                                list[i].stateClassTime = 0
                                isFirstClassTime = false
                            }
                        }
                        if (b.id == R.string.text_gps) {
                            if (gpsState) {
                                list[i].stateGPS = 1
                                isFirstGPS = true
                            } else {
                                list[i].stateGPS = 0
                                isFirstGPS = false
                            }
                        }
                    }
                    if (adapter != null) {
                        adapter!!.notifyDataSetChanged()
                    }
                }
                if (onfigs520 != null) {
                    val isFairWall = onfigs520.fanghuoqiang
                    val keyLockState = onfigs520.keyLockState
                    for (i in list.indices) {
                        val b = list[i]
                        if (b.id == R.string.text_fair_wall) {
                            if ("1" == isFairWall || "true" == isFairWall) {
                                list[i].state = 1
                                isFirst = true
                            } else {
                                list[i].state = 0
                                isFirst = false
                            }
                        }
                        if (b.id == R.string.text_lock) {
                            if (keyLockState == 1) {
                                list[i].stateKeyLock = 1
                                isFirstKeyLock = true
                            } else {
                                list[i].stateKeyLock = 0
                                isFirstKeyLock = false
                            }
                        }
                    }
                    if (adapter != null) {
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        } else if ("SHX520FindDevice" == msg) {
            mLoadingDoialog!!.dismiss()
            showMessage(resources.getString(R.string.text_find_device))
        } else if ("SHX520SigleAddressQuery" == msg) {
            mLoadingDoialog!!.dismiss()
            showMessage(resources.getString(R.string.text_find_locate))
        } else if ("SHX520SetWorkMode" == msg) {
            mLoadingDoialog!!.dismiss()
            val str = String.format(resources.getString(R.string.text_set_mode_success),
                    if (mode == 2)
                        resources.getString(R.string.text_mode_2)
                    else
                        resources.getString(R.string.text_mode_1))
            showMessage(str)
        } else if ("SHX520RemotePowerSet" == msg) {
            mLoadingDoialog!!.dismiss()
            showMessage(if (state == 0)
                resources.getString(R.string.text_off_success)
            else
                resources.getString(R.string.text_reboot_success))
        } else if ("SHX520Factory" == msg) {
            mLoadingDoialog!!.dismiss()
            showMessage(resources.getString(R.string.text_send_reset))
        } else if ("SHX002ClassTimeControl" == msg || "SHX007SetGPSOpen" == msg
                || "SHX520FireWallSetting" == msg || "SHX520KeylockSet" == msg) {
            val result = event.content
            mLoadingDoialog!!.dismiss()
            try {
                val o = JSONObject(result)
                val s = o.getString("d")
                showMessage(s)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    private fun showModeDialog(b: Boolean) {
        val builder = AlertDialog.Builder(this)
        if (b)
            builder.setTitle(R.string.text_mode_set)
        else
            builder.setTitle(R.string.text_remote_set)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_chose, null)
        val radioGroup = view.findViewById(R.id.id_radioGroup) as RadioGroup
        val radioButton1 = view.findViewById(R.id.id_rbFirst) as RadioButton
        val radioButton2 = view.findViewById(R.id.id_rbSecond) as RadioButton
        builder.setCancelable(true)
        if (b) {
            if (mode == 2) {
                radioButton2.isChecked = true
            } else {
                radioButton1.isChecked = true
            }
        } else {
            radioButton1.setText(R.string.text_mode_power_off)
            radioButton2.setText(R.string.text_mode_power_boot)
            if (state == 0) {
                radioButton1.isChecked = true
            } else {
                radioButton2.isChecked = true
            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.id_rbFirst -> if (b)
                    mode = 1
                else
                    state = 0
                R.id.id_rbSecond -> if (b)
                    mode = 2
                else
                    state = 1
            }
        }
        builder.setPositiveButton(R.string.text_ok) { dialog, which ->
            mLoadingDoialog!!.dismiss()
            mLoadingDoialog!!.show()
            if (b)
                mNetHelper.setMode(mode)
            else
                mNetHelper.reomtePower(state)
        }
        builder.setView(view)
        builder.show()
    }
}
