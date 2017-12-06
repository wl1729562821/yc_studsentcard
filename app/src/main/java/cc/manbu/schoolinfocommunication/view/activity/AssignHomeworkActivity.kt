package cc.manbu.schoolinfocommunication.view.activity

import android.content.DialogInterface
import android.support.annotation.IdRes
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.R_Department
import cc.manbu.schoolinfocommunication.bean.R_Subject
import cc.manbu.schoolinfocommunication.bean.R_Users
import cc.manbu.schoolinfocommunication.config.Configs
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import kotlinx.android.synthetic.main.activity_assign_homework.*
import kotlinx.android.synthetic.main.layout_head.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * Created by Administrator on 2017/11/25 0025.
 */
class AssignHomeworkActivity:BaseActivityStudent(){

    private val departNames = ArrayList<R_Department>()
    private val subjectList = ArrayList<List<R_Subject>>()
    private var pos = -1
    private var subject = ""
    private var title = ""
    private var curDepartmentId = -1
    private var curSubjectId = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_assign_homework
    }

    override fun initView() {
        id_tvTitle?.setText(R.string.text_homework)
        val users = Configs.get(Configs.Config.CurUser, R_Users::class.java)
        users?.let {
            mLoadingDoialog?.show()
            mNetHelper.accessDepartmentList(it.id)
        }
        id_tvClass?.setOnClickListener {
            showDialog(true)
        }
        id_tvSubject?.setOnClickListener {
            subject = resources.getString(R.string.text_chose_subjects)
            if (pos < 0) {
                showMessage(resources.getString(R.string.text_chose_class_first))
            } else {
                showDialog(false)
            }
        }
        id_tvSubmit?.setOnClickListener {
            val content = id_edContent.text.toString()
            when {
                TextUtils.isEmpty(content) -> {
                    showMessage(resources.getString(R.string.hint_home_work))
                }
                curDepartmentId < 0 -> {
                    showMessage(resources.getString(R.string.text_chose_class_first))
                }
                curSubjectId < 0 -> {
                    showMessage(resources.getString(R.string.text_chose_subject_first))
                }
                else -> {
                    mLoadingDoialog?.show()
                    mNetHelper.addHomeWork(curDepartmentId, curSubjectId, content, title)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread1(event: ResposeEvent) {
        val msg = event.message
        if ("GetR_DepartmentListByTeacherId" == msg) {
            mLoadingDoialog?.dismiss()
            val datas = event.departments
            for (i in datas.indices) {
                val d = datas.get(i)
                departNames.add(d)
                subjectList.add(d.getR_Subject())
            }
        } else if ("UpdateOrAddHomework" == msg) {
            mLoadingDoialog?.dismiss()
            id_edContent.editableText.clear()
            showMessage(resources.getString(R.string.text_home_work_relase_success))
        }
    }

    private fun showDialog(b: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val p = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT)
        val rg = RadioGroup(this)
        rg.setPadding(30, 10, 0, 0)
        rg.layoutParams = p
        if (b) {
            builder.setTitle(R.string.text_chose_class)
            for (i in departNames.indices) {
                val rb = RadioButton(this)
                rb.text = departNames[i].depName
                rb.tag = i
                rg.addView(rb)
            }
        } else {
            builder.setTitle(R.string.text_chose_subjects)
            val list = subjectList[pos]
            if (list != null) {
                for (i in list.indices) {
                    val s = list[i]
                    val rb = RadioButton(this)
                    rb.text = s.name
                    rb.tag = i
                    rg.addView(rb)
                }
            }
        }
        rg.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById(checkedId) as RadioButton
            if (b) {
                pos = rb.tag as Int
                curDepartmentId = departNames[pos].id
            } else {
                val tag = rb.tag as Int
                subject = subjectList[pos][tag].name
                curSubjectId = subjectList[pos][tag].id
            }
        }
        builder.setPositiveButton(R.string.text_ok) { dialog, which ->
            if (b) {
                val str = departNames[pos].depName
                id_tvClass.text = str
                title = str
                id_tvSubject.setText(R.string.text_chose_subjects)
            } else {
                id_tvSubject.text = subject
                title += subject
            }
        }
        builder.setView(rg)
        builder.show()
    }
}