package cn.yc.student.view.fragment


import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.PhoneListBean
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.tools.Utils
import cc.manbu.schoolinfocommunication.view.adapter.PhoneListAdapter
import kotlinx.android.synthetic.main.fragment_phone_list.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * A simple [Fragment] subclass.
 */
class PhoneListFragment : BaseFragmentStudent(), EasyPermissions.PermissionCallbacks{
    private val list = ArrayList<PhoneListBean>()
    private var adapter: PhoneListAdapter? = null
    private val phone_icon = arrayOf("", "", "", "", "", "", "", "", "", "")
    private val phone_Nums = arrayOf("", "", "", "", "", "", "", "", "", "")
    private val phone_name = arrayOf("", "", "", "", "", "", "", "", "", "")

    override fun getLayoutId(): Int {
        return R.layout.fragment_phone_list
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        init()
        mLoadingDoialog?.show()
        mNetHelper.accessPhoneList()
    }

    private fun init() {
        id_refreshLayout!!.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid)
        id_refreshLayout.setProgressViewEndTarget(true, 120)//设置距离顶端的距离
        id_refreshLayout.setOnRefreshListener {
            mNetHelper.accessPhoneList()
        }
        adapter = PhoneListAdapter(context, list)
        id_lvPhoneList!!.adapter = adapter
        id_lvPhoneList.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            showDeleteDialog(position)
            true
        }
        id_lvPhoneList?.setOnItemClickListener { parent, view, position, id ->
            matchPosition(position)
            val event = ViewEvent()
            event.setMessage(Constant.EVENT_EDIT_PHONE_LIST)
            event.setFlg(1)
            EventBus.getDefault().post(event)
        }
    }

    private fun matchPosition(position: Int) {
        for (i in 0 until Utils.datas.size) {
            if (list[position] == Utils.datas.get(i)) {
                Utils.position = position
                Utils.allPosition = i
                break
            }
        }
    }

    private fun showDeleteDialog(pos: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.text_tips)
        val message = resources.getString(R.string.text_del_phone)
        builder.setMessage(String.format(message, list[pos].contactName))
        builder.setPositiveButton(R.string.text_ok, DialogInterface.OnClickListener { dialog, which ->
            for (i in 0 until Utils.datas.size) {
                if (list[pos] == Utils.datas.get(i)) {
                    for (j in 0 until Utils.stringList.size) {
                        if (Utils.stringList.get(j).equals(list[pos].contactNumber)) {
                            Utils.stringList.removeAt(j)
                            break
                        }
                    }
                    phone_icon[i] = ""
                    phone_name[i] = ""
                    phone_Nums[i] = ""
                    Utils.datas.set(i, PhoneListBean())
                    list.removeAt(pos)
                    adapter!!.notifyDataSetChanged()
                    break
                }
            }
            dialog.dismiss()
            mLoadingDoialog?.show()
            setPhoneBooks()
        })
        builder.show()
    }

    private fun setPhoneBooks() {
        val js = JSONObject()
        val phonenum = JSONArray()
        val phonename = JSONArray()
        val phoneicon = JSONArray()
        for (i in phone_Nums.indices) {
            phonenum.put(phone_Nums[i])
            phonename.put(phone_name[i])
            phoneicon.put(phone_icon[i])
        }
        try {
            js.put("_id", Configs.getCurDeviceSerialnumber())
            js.put("PL", phonenum)
            js.put("Name", phonename)
            js.put("Icon", phoneicon)
            val par = js.toString()
            val str = "{\"entity\":$par}"
            mLoadingDoialog?.show()
            mNetHelper.setPhoneList(str, 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        val flgs = event.getFlg()
        if ("SHX520GetPhoneBook_V2" == msg) {
            val content = event.getContent()
            mLoadingDoialog?.dismiss()
            id_refreshLayout!!.isRefreshing = false
            list.clear()
            Utils.datas.clear()
            Utils.stringList.clear()
            try {
                val `object` = JSONObject(content)
                val jsonObject = `object`.optJSONObject("d")
                if (jsonObject != null) {
                    val jsonArraypl = jsonObject!!.optJSONArray("PL")
                    val jsonArrayname = jsonObject!!.optJSONArray("Name")
                    val jsonArrayicon = jsonObject!!.optJSONArray("Icon")
                    var name = ""
                    var number = ""
                    for (i in 0..9) {
                        val phoneListBean = PhoneListBean()
                        if (jsonArraypl != null) {
                            number = jsonArraypl!!.getString(i)
                            phoneListBean.contactNumber = number
                            Utils.stringList.add(number)
                            phone_Nums[i] = number
                        }
                        if (jsonArrayicon != null) {
                            phoneListBean.iconStr = jsonArrayicon!!.getString(i)
                            phone_icon[i] = jsonArrayicon!!.getString(i)
                        }
                        if (jsonArrayname != null) {
                            name = jsonArrayname!!.getString(i)
                            phoneListBean.contactName = name
                            phone_name[i] = name
                        }
                        if (name != "" && number != "")
                            list.add(phoneListBean)
                        Utils.datas.add(phoneListBean)
                    }
                }
                if (adapter != null) {
                    adapter!!.notifyDataSetChanged()
                }
                if (list.size <= 0) {
                    id_llEmpty!!.visibility = View.VISIBLE
                    id_refreshLayout.visibility = View.GONE
                } else {
                    id_llEmpty!!.visibility = View.GONE
                    id_refreshLayout.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if ("SHX520SetPhoneBook_V2" == msg && flgs == 1) {
            mLoadingDoialog?.dismiss()
            val result = event.getContent()
            try {
                val `object` = JSONObject(result)
                val str = `object`.getString("d")
                showMessage(str)
                if (list.size <= 0) {
                    id_llEmpty!!.visibility = View.VISIBLE
                    id_refreshLayout!!.visibility = View.GONE
                } else {
                    id_llEmpty!!.visibility = View.GONE
                    id_refreshLayout!!.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    override fun onPermissionsDenied(p0: Int, p1: MutableList<String>?) {
        Log.e(TAG,"onPermissionsDenied $p0  ${p1?.toString()}")
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>?) {
        Log.e(TAG,"onPermissionsGranted $p0  ${p1?.toString()}")
    }
}// Required empty public constructor
