package cn.yc.student.view.fragment


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast

import com.github.siyamed.shapeimageview.CircularImageView

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

import java.io.File

import android.app.Activity.RESULT_OK
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.listener.HttpRespnse
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener
import cc.manbu.schoolinfocommunication.tools.CameraUtils
import cc.manbu.schoolinfocommunication.tools.Utils
import kotlinx.android.synthetic.main.fragment_edit_phone.*

/**
 * A simple [Fragment] subclass.
 */
class EditPhoneFragment : BaseFragmentStudent() {
    private var popupWindow: PopupWindow? = null
    private var cameraUtils: CameraUtils? = null
    private var path = ""
    private var pos = -1//if have a positon flag,it means users want to edit,else add a new number
    private var file: File? = null
    private var photo: Bitmap? = null
    private val phone_icon = arrayOf("", "", "", "", "", "", "", "", "", "")
    private val phone_Nums = arrayOf("", "", "", "", "", "", "", "", "", "")
    private val phone_name = arrayOf("", "", "", "", "", "", "", "", "", "")
    private val headPic = "_head.jpg"

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btn_cancel -> if (popupWindow != null && popupWindow?.isShowing==true)
                popupWindow?.dismiss()
            R.id.btn_photo -> {
                cameraUtils?.getLocalImage(REQ_CODE_LOCALE_BG)
                if (popupWindow != null && popupWindow?.isShowing==true) {
                    popupWindow?.dismiss()
                }
            }
            R.id.btn_carmera -> {
                cameraUtils?.takePhotoFromCarmera(PICTURE_CODE)
                if (popupWindow != null && popupWindow?.isShowing==true) {
                    popupWindow?.dismiss()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_phone
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            pos = Utils.position
            cameraUtils?.fileName = pos.toString() + headPic
            if (pos >= 0) {//edit
                for (i in Utils.datas.indices) {
                    phone_icon[i] = Utils.datas[i].iconStr
                    phone_Nums[i] = Utils.datas[i].contactNumber
                    phone_name[i] = Utils.datas[i].contactName
                    if (Utils.allPosition == i) {
                        setContentToView(phone_name[i], phone_Nums[i])
                    }
                }
            } else if (pos == -2) {//add from contacts
                if (Utils.contactEntity != null) {
                    setContentToView(Utils.contactEntity.name, Utils.contactEntity.number)
                    if (Utils.contactEntity.photoid <= 0) {
                        id_ivPortrait.setImageResource(R.drawable.gray_head)
                    }
                }
            } else {
                id_edName.getEditableText().clear()
                id_edNumber.getEditableText().clear()
                id_ivPortrait.setImageResource(R.drawable.gray_head)
            }
        }
    }

    override fun initView() {
        registerEventbus(this)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun init() {
        path = Configs.Photo_RCV + Configs.getCurDeviceSerialnumber() + File.separator + "images" + File.separator
        pos = Utils.position
        cameraUtils = CameraUtils(activity, path)
        cameraUtils?.fileName = pos.toString() + headPic
        if (pos >= 0) {
            for (i in Utils.datas.indices) {
                phone_icon[i] = Utils.datas[i].iconStr
                phone_Nums[i] = Utils.datas[i].contactNumber
                phone_name[i] = Utils.datas[i].contactName
                if (Utils.allPosition == i) {
                    setContentToView(phone_name[i], phone_Nums[i])
                }
            }
        } else if (pos == -2) {
            if (Utils.contactEntity != null) {
                setContentToView(Utils.contactEntity.name, Utils.contactEntity.number)
            }
        }
        id_llHead.setOnClickListener {
            showPopupWindow(id_llHead)
        } 
        id_tvSure.setOnClickListener {
            val str_Phone = id_edNumber.getText().toString()
            val str_Name = id_edName.getText().toString()
            var iconStr = ""
            if (str_Phone == "") {
                showMessage(resources.getString(R.string.text_not_null_phone))
            } else if (str_Name == "") {
                showMessage(resources.getString(R.string.tips_name_not_be_null))
            } else {
                val f = File(path + pos + headPic)
                if (f.exists() && f.length() > 0)
                    iconStr = Utils.getImageStr(path + pos + headPic)
                if (pos >= 0) {//edit the phone list
                    phone_icon[pos] = iconStr
                    phone_name[pos] = str_Name
                    phone_Nums[pos] = str_Phone
                    mLoadingDoialog?.show()
                    setPhoneBooks()
                } else if (pos <= -1) {
                    var position = 0
                    for (i in 0..9) {
                        if (phone_Nums[i] != "" && phone_name[i] != "" &&
                                phone_name[i] == str_Name && phone_Nums[i] == str_Phone) {
                            showMessage(resources.getString(R.string.hint_contact_exits))
                        }else if(phone_Nums[i] == ""){
                            position = i
                        }
                    }
                    phone_Nums[position] = str_Phone
                    phone_name[position] = str_Name
                    phone_icon[position] = iconStr
                    if (Utils.contactEntity != null) {
                        if (Utils.contactEntity.photoid <= 0)
                            phone_icon[position] = ""
                    }
                    mLoadingDoialog?.show()
                    setPhoneBooks()
                }
            }
        }
    }

    private fun setContentToView(name: String, number: String) {
        id_edName.setText(name)
        id_edNumber.setText(number)
        val ea = id_edName.getText()
        id_edName.setSelection(ea.length)
        file = File(path + pos + headPic)
        if (file?.exists()==true && file?.length()?:-1 > 0) {
            photo = BitmapFactory.decodeFile(path + pos + headPic, null)
            id_ivPortrait.setImageBitmap(photo)
        } else {
            id_ivPortrait.setImageResource(R.drawable.gray_head)
        }
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
            mNetHelper.setPhoneList(str, 2)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun showPopupWindow(view: View) {
        val contentView = LayoutInflater.from(context).inflate(
                R.layout.layout_pop_camera, null)
        val btn_carmera = contentView.findViewById(R.id.btn_carmera) as Button
        val btn_photo = contentView.findViewById(R.id.btn_photo) as Button
        val btn_cancel = contentView.findViewById(R.id.btn_cancel) as Button

        popupWindow = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)
        btn_carmera.setOnClickListener(listener)
        btn_photo.setOnClickListener(listener)
        btn_cancel.setOnClickListener(listener)
        popupWindow?.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow?.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_LOCALE_BG) {// 读取本地相片
                cameraUtils?.readLocalImage(data, REQ_CODE_PHOTO_CROP)
            } else if (requestCode == REQ_CODE_PHOTO_CROP) {// 裁剪图片后的返回结果
                cameraUtils?.readCropImage(data)
                file = File(path + cameraUtils?.fileName)
                if (file?.exists()==true && file?.length()?:-1 > 0) {
                    photo = BitmapFactory.decodeFile(path + cameraUtils?.fileName, null)
                    id_ivPortrait.setImageBitmap(photo)
                }
            } else if (requestCode == PICTURE_CODE) {//拍照
                val mfile = File(path, cameraUtils?.tempPicName)
                if (mfile.exists()) {
                    cameraUtils?.startPhotoCrop(Uri.fromFile(mfile), null, REQ_CODE_PHOTO_CROP) // 图片裁剪
                } else {
                    Toast.makeText(context, "Errors?!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        val flgs = event.getFlg()
        if ("SHX520SetPhoneBook_V2" == msg && flgs == 2) {
            mLoadingDoialog?.dismiss()
            val result = event.getContent()
            try {
                val `object` = JSONObject(result)
                val str = `object`.getString("d")
                showMessage(str)
                mNetHelper.accessPhoneList()
                Utils.stringList.add(id_edNumber.getText().toString())
                id_edNumber.editableText.clear()
                id_edName.editableText.clear()
                val event=ViewEvent()
                event.type=4000
                EventBus.getDefault().post(event)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        }
    }

    companion object {
        private val REQ_CODE_LOCALE_BG = 101
        private val REQ_CODE_PHOTO_CROP = 100
        private val PICTURE_CODE = 102
    }
}// Required empty public constructor
