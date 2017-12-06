package cc.manbu.schoolinfocommunication.view.activity


import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.R2
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.config.DeviceType
import cc.manbu.schoolinfocommunication.config.ManbuApplication
import cc.manbu.schoolinfocommunication.listener.HttpRespnse
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener
import cc.manbu.schoolinfocommunication.view.activity.*
import cn.yc.student.bean.DrawableBean
import cn.yc.student.view.fragment.StudentOneFragment
import kotlinx.android.synthetic.main.activity_main1.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *@Author:余慈
 *@Date:2017/11/7 0007
 *@Description:主界面activity
 **/
class MainActivity: BaseActivityStudent(){

    //是否是学生
    private var mChild = false

    private var mList: MutableList<BaseFragmentStudent> = arrayListOf()

    private var mDrwableList: ArrayList<DrawableBean>? = null
    private val mPage = 6

    private var mPageType=0

    override fun getLayoutId(): Int {
        return R.layout.activity_main1
    }

    //初始化控件,onCreate里面执行
    override fun initView() {
        mDrwableList = mDrwableList ?: arrayListOf()
        val student: (Int) -> Boolean = { type ->
            type == 0

        }
        mChild = student(Configs.whichRole)
        //如果是学生
        if (mChild) {
            mLoadingDoialog?.show()
            val user = Configs.get(Configs.Config.CurUser, R_Users::class.java)
            mNetHelper?.accessMobileDeviceAndLocation(user.getSerialnumber(),
                    object : HttpCallListener {
                        override fun onError(code: Int, msg: String?) {
                            mLoadingDoialog?.dismiss()
                            Toast.makeText(this@MainActivity, "${msg ?: ""}", Toast.LENGTH_SHORT).show()
                        }

                        override fun <T> onNext(data: HttpRespnse<T>) {
                            data.data?.let {
                                val event = it as? ResposeEvent
                                Log.e(TAG, "onNext ${data.data}")
                                mLoadingDoialog?.dismiss()
                                event?.let {
                                    Log.e(TAG, "onNext2 ${it.devicAndLocation}")
                                    it.devicAndLocation?.let {
                                        Log.e(TAG, "onNext3 ${it.deviceTypeID}")
                                        Configs.put(Configs.Config.CurDevice,it)
                                        Configs.curDeviceType = it.deviceTypeID
                                        initData()
                                        return
                                    }
                                    Configs.curDeviceType = DeviceType.S520Watch_with_StudentCard
                                    initData()
                                }
                            }
                        }
                    })
        } else {//如果是老师
            initData()
        }
        Log.e(TAG, "initView ${Configs.whichRole} $mChild ${student(Configs.whichRole)}")
    }

    fun addArray(type: Int) {
        var titleId = 0
        var drawableId = 0
        var intentList= arrayListOf<Any>()
        if (type == 0) {
            titleId = R.array.array_54_50_40_36_43
            drawableId = R.array.array_drawable_54_50_40_36_43
            intentList.add(EZoneActivity::class.java)
            intentList.add(PhoneActivity::class.java)
            intentList.add(CheckWorkActivity::class.java)
            intentList.add(ClockActivity::class.java)
            intentList.add(ChildrenProtectActivity::class.java)
            intentList.add(HomeWorkActivity::class.java)
            intentList.add(ExamActivity::class.java)
            intentList.add(DayOffActivity::class.java)
            intentList.add(MyClassActivity::class.java)
            intentList.add(RelativesActivity::class.java)
            intentList.add(SchoolActivity::class.java)
            intentList.add(TimerActivity::class.java)
            intentList.add("cc.manbu.schoolinfocommunication.view.activity.ModelActivity")
        } else if (type == 1) {
            titleId = R.array.array_22
            drawableId = R.array.array_drawable_22
            intentList.add(EZoneActivity::class.java)
            intentList.add(CheckWorkActivity::class.java)
            intentList.add(ClockActivity::class.java)
            intentList.add(ChildrenProtectActivity::class.java)
            intentList.add(HomeWorkActivity::class.java)
            intentList.add(ExamActivity::class.java)
            intentList.add(DayOffActivity::class.java)
            intentList.add(MyClassActivity::class.java)
            intentList.add(RelativesActivity::class.java)
            intentList.add(SchoolActivity::class.java)
            intentList.add(SceneModeActivity::class.java)
            intentList.add("cc.manbu.schoolinfocommunication.view.activity.ModelActivity")
        } else if (type == 2) {
            titleId = R.array.array_27
            drawableId = R.array.array_drawable_27
            intentList.add(EZoneActivity::class.java)
            intentList.add(CheckWorkActivity::class.java)
            intentList.add(ClockActivity::class.java)
            intentList.add(ChildrenProtectActivity::class.java)
            intentList.add(HomeWorkActivity::class.java)
            intentList.add(ExamActivity::class.java)
            intentList.add(DayOffActivity::class.java)
            intentList.add(MyClassActivity::class.java)
            intentList.add(RelativesActivity::class.java)
            intentList.add(SchoolActivity::class.java)
            intentList.add("cc.manbu.schoolinfocommunication.view.activity.ModelActivity")
        } else if (type == 3) {
            titleId = R.array.array_41
            drawableId = R.array.array_drawable_41
            intentList.add(EZoneActivity::class.java)
            intentList.add(CheckWorkActivity::class.java)
            intentList.add(ClockActivity::class.java)
            intentList.add(ChildrenProtectActivity::class.java)
            intentList.add(HomeWorkActivity::class.java)
            intentList.add(ExamActivity::class.java)
            intentList.add(DayOffActivity::class.java)
            intentList.add(MyClassActivity::class.java)
            intentList.add(RelativesActivity::class.java)
            intentList.add(SchoolActivity::class.java)
            intentList.add(TimerActivity::class.java)
            intentList.add("cc.manbu.schoolinfocommunication.view.activity.ModelActivity")
        } else {
            titleId = R.array.array_54_50_40_36_43
            drawableId = R.array.array_drawable_54_50_40_36_43
            intentList.add(EZoneActivity::class.java)
            intentList.add(PhoneActivity::class.java)
            intentList.add(CheckWorkActivity::class.java)
            intentList.add(ClockActivity::class.java)
            intentList.add(ChildrenProtectActivity::class.java)
            intentList.add(HomeWorkActivity::class.java)
            intentList.add(ExamActivity::class.java)
            intentList.add(DayOffActivity::class.java)
            intentList.add(MyClassActivity::class.java)
            intentList.add(RelativesActivity::class.java)
            intentList.add(SchoolActivity::class.java)
            intentList.add(TimerActivity::class.java)
        }
        val array = resources.getStringArray(titleId)
        val drawableArray = resources.obtainTypedArray(drawableId)
        Log.e(TAG,"addarray ${array.size}")
        mDrwableList?.let {
            for (i in 0..(array.size - 1)) {
                it.add(DrawableBean(array.get(i), drawableArray.getResourceId(i, 0), i + 1,intentList[i]))
            }
        }
    }

    fun initData() {
        if (!mChild) {
            main_message?.visibility = View.GONE
            main_mine?.setOnClickListener(mListener)
            main_setting?.setOnClickListener(mListener)
        } else {
            main_mine?.setOnClickListener(mListener)
            main_setting?.setOnClickListener(mListener)
            main_message?.setOnClickListener(mListener)
        }
        mDrwableList?.let {
            with(it) {
                if (mChild) {
                    var array = arrayListOf<DrawableBean>()
                    when (Configs.curDeviceType) {
                        54, 50, 40, 36, 43 ->
                            addArray(0)
                        22 ->
                            addArray(1)
                        27 ->
                            addArray(2)
                        41 ->
                            addArray(41)

                        else -> addArray(0)
                    }
                } else {
                    val array = resources.getStringArray(R.array.teacher_array)
                    val drawableArray = resources.obtainTypedArray(R.array.teacher_drawable_array)
                    for (i in 0..(array.size - 1)) {
                        var atv:Any?=null
                        when(i){
                            0->atv= SchoolActivity::class.java
                            1->atv= LeaveManageActivity::class.java
                            2->atv=SubjectListActivity::class.java
                            3->atv=TeacherClassActivity::class.java
                            4->atv=MyClassActivity::class.java
                            5->atv=SettingHomeWorkActivity::class.java
                        }
                        add(DrawableBean(array[i], drawableArray.getResourceId(i, 0), i + 1,atv))
                    }
                }
            }
            var count = it.size / mPage
            val balance = it.size % mPage
            if (balance > 0) {
                count++
            }
            if(count==2){
                main_index_1?.visibility=View.GONE
                main_index_2?.visibility=View.GONE
                main_index_3?.visibility=View.GONE
                main_index_parent?.visibility=View.VISIBLE
                mPageType=1
            }else if(count==3){
                main_index_parent?.visibility=View.GONE
                main_index_1?.visibility=View.VISIBLE
                main_index_2?.visibility=View.VISIBLE
                main_index_3?.visibility=View.VISIBLE
                mPageType=2
            }else{
                main_index_1?.visibility=View.GONE
                main_index_2?.visibility=View.GONE
                main_index_3?.visibility=View.GONE
                main_index_parent?.visibility=View.GONE
                mPageType=0
            }
            Log.e(TAG, "initData $count $balance ${it.size}")
            var start = 1
            mDrwableList?.run {
                for (i in 1..count) {
                    val list = arrayListOf<DrawableBean>()
                    for (x in start..mPage * i) {
                        start = x
                        if (x <= mDrwableList?.size ?: 0) {
                            list.add(get(x - 1))
                        }
                    }
                    start++
                    mList?.add(StudentOneFragment.newFragment("STUDENT_KEY"+i,list))
                }
            }
            mList?.let {
                if (it.size <= 1) {
                    main_index_parent?.visibility = View.GONE
                }
            }
            main_pager?.let {
                Log.e(TAG, "initView $it ${mList?.size}")
                if (it.adapter == null) {
                    it.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

                        override fun getItem(position: Int): Fragment {
                            Log.e(TAG, "getItem ${mList?.get(position)}")
                            return mList?.get(position)
                        }

                        override fun getCount(): Int {
                            return mList?.size
                        }
                    }
                    it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                        override fun onPageScrollStateChanged(state: Int) {

                        }

                        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                            Log.e(TAG,"onPageScrolled $position $positionOffset $positionOffsetPixels" )
                        }

                        override fun onPageSelected(position: Int) {
                            mList?.let {
                                if (it.size > 1) {
                                    if(mPageType==1){
                                        if (position == 0) {
                                            main_index_two?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_one?.setBackgroundResource(R.drawable.yellow_oval)
                                        } else {
                                            main_index_one?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_two?.setBackgroundResource(R.drawable.yellow_oval)
                                        }
                                    }else if(mPageType==2){
                                        if (position == 0) {
                                            main_index_3?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_2?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_1?.setBackgroundResource(R.drawable.yellow_oval)
                                        } else if(position==1){
                                            main_index_3?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_1?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_2?.setBackgroundResource(R.drawable.yellow_oval)
                                        }else if(position==2){
                                            main_index_2?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_1?.setBackgroundResource(R.drawable.black_oval)
                                            main_index_3?.setBackgroundResource(R.drawable.yellow_oval)
                                        }
                                    }
                                }
                            }
                        }
                    })
                }
            }

        }
    }

    private val mListener = View.OnClickListener { v ->
        v?.let {
            val intent = Intent()
            var index = -1
            if (it.id == R.id.main_mine) {
                index = 0
                intent.setClass(this@MainActivity, AboutMeActivity::class.java)
            } else if (it.id == R.id.main_setting) {
                index = 1
                intent.setClass(this@MainActivity, SettingsActivity::class.java)
            } else if (it.id == R.id.main_message) {
                index = 2
                intent.setClass(this@MainActivity, MessageActivity::class.java)
            } else {

            }
            if (index >= 0) {
                startActivity(intent)
            }
        }
    }

    private var mExitTime:Long?=null

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            if ((System.currentTimeMillis() - (mExitTime?:0)) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
            } else {
                ManbuApplication.exit()
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
