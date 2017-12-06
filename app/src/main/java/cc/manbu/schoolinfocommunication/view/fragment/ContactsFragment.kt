package cn.yc.student.view.fragment


import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.ContactEntity
import cc.manbu.schoolinfocommunication.view.adapter.ContactsAdapter
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.config.Configs

import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.io.File
import java.io.InputStream
import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_contacts.*

/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : BaseFragmentStudent() {
    private var rootView: View? = null
    private var path = ""
    private val mContacts = ArrayList<ContactEntity>()
    private var SourceDateList: MutableList<ContactEntity> = ArrayList()
    private var isInitialize = true
    private val MSG_UPDATE_DATA = 0x10
    private val MSG_INITIALIZE_DATA = 0x11
    private var value = ""
    private var adapter: ContactsAdapter? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_UPDATE_DATA -> {
                    val filterDateList = msg.obj as List<ContactEntity>
                    if (adapter != null) {
                        adapter!!.updateListView(filterDateList)
                    }
                }
                MSG_INITIALIZE_DATA -> if (adapter != null) {
                    adapter!!.updateListView(SourceDateList)
                }
            }
        }
    }
    internal val mSearchRunnable: Runnable = Runnable {
        if (isInitialize) {
            SourceDateList = filledData(mContacts)
            handler.sendEmptyMessage(MSG_INITIALIZE_DATA)
        } else {
            filterData(value)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_contacts
    }

    override fun initView() {
        init()
        contacts_clear?.let {
            it.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    id_edSearch?.text=null
                }
            })
        }
        id_edSearch?.let {
            it.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                        if(it.toString().trim().length>0){
                            hideClear(View.VISIBLE)
                        }else{
                            hideClear(View.GONE)
                        }
                        value = it.toString()
                        if (isInitialize) {
                            isInitialize = false
                        }
                        Thread(mSearchRunnable).start()
                        return
                    }
                    hideClear(View.GONE)
                }
            })
        }
        getPhoneContacts()
    }

    //隐藏清除按钮
    fun hideClear(hide:Int){
        contacts_clear?.visibility=hide
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            if (adapter != null) {
                val tempList = ArrayList<ContactEntity>()
                tempList.addAll(SourceDateList)
                SourceDateList.clear()
                SourceDateList.addAll(tempList)
                adapter!!.updateListView(SourceDateList)
            }
        }
    }

    private fun init() {
        path = Configs.Photo_RCV + Configs.getCurDeviceSerialnumber() + File.separator + "images" + File.separator
        adapter = ContactsAdapter(SourceDateList, activity)
        contacts_list?.adapter = adapter
        Log.e("ContactsFragment","init $contacts_list $adapter $path")
    }

    /**
     * 获取手机联系人
     */
    private fun getPhoneContacts() {
        val resolver = activity.contentResolver
        try {
            // 获取手机联系人
            val phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null)
            if (phoneCursor != null) {

                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    val phoneNumber = phoneCursor
                            .getString(PHONES_NUMBER_INDEX)
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue

                    // 得到联系人名称
                    val contactName = phoneCursor
                            .getString(PHONES_DISPLAY_NAME_INDEX)

                    // 得到联系人ID
                    val contactid = phoneCursor
                            .getLong(PHONES_CONTACT_ID_INDEX)

                    // 得到联系人头像ID
                    val photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX)

                    // 得到联系人头像Bitamp
                    var contactPhoto: Bitmap? = null

                    // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                    if (photoid > 0) {
                        val uri = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI,
                                contactid)
                        val input = ContactsContract.Contacts
                                .openContactPhotoInputStream(resolver, uri)
                        contactPhoto = BitmapFactory.decodeStream(input)
                    } else {
                        contactPhoto = BitmapFactory.decodeResource(
                                resources, R.drawable.gray_head)
                    }
                    val mContact = ContactEntity(contactName,
                            phoneNumber, contactPhoto, photoid)
                    mContacts.add(mContact)
                }
                phoneCursor.close()
                Thread(mSearchRunnable).start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private fun filledData(date: List<ContactEntity>): MutableList<ContactEntity> {
        val mSortList = ArrayList<ContactEntity>()

        for (i in date.indices) {
            val itemInfo = date[i]
            val sortModel = ContactEntity()
            sortModel.name = itemInfo.name
            sortModel.number = itemInfo.number
            sortModel.photo = itemInfo.photo
            sortModel.photoid = itemInfo.photoid

            mSortList.add(sortModel)
        }
        return mSortList
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    @Synchronized private fun filterData(filterStr: String) {
        var filterDateList: MutableList<ContactEntity> = ArrayList()

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList
        } else {
            filterDateList.clear()
            for (sortModel in SourceDateList) {
                val number = sortModel.number
                if (number.contains(filterStr)) {
                    filterDateList.add(sortModel)
                }
            }
        }

        handler.obtainMessage(MSG_UPDATE_DATA, filterDateList).sendToTarget()
    }

    companion object {
        private val PHONES_PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        /** 联系人显示名称  */
        private val PHONES_DISPLAY_NAME_INDEX = 0
        /** 电话号码  */
        private val PHONES_NUMBER_INDEX = 1
        /** 头像ID  */
        private val PHONES_PHOTO_ID_INDEX = 2
        /** 联系人的ID  */
        private val PHONES_CONTACT_ID_INDEX = 3
    }
}// Required empty public constructor
