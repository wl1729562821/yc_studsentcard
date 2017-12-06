package cc.manbu.schoolinfocommunication.view.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.widget.Toast
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import android.os.Environment.getExternalStorageDirectory
import cc.manbu.schoolinfocommunication.base.AppData
import com.qihoo360.replugin.RePlugin
import java.io.File


/**
 * Created by Administrator on 2017/12/1 0001.
 */
class WelcomeActivity :BaseActivityStudent(){

    private var mTime: CountDownTimer?=null


    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e(TAG,"onNewIntent")
    }

    override fun initView() {
        Log.e(TAG,"initView")
        AppData.clear()
        mTime=object : CountDownTimer(1000,1500){
            override fun onFinish() {
                mLoadingDoialog?.show()
                val intent = Intent()
                intent.component = ComponentName("MusicModule",
                        "cn.yc.music.view.activity.LoadActivity")
                RePlugin.startActivityForResult(this@WelcomeActivity, intent,1002, null)
            }
            override fun onTick(millisUntilFinished: Long) {

            }
        }?.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLoadingDoialog?.dismiss()
        Log.e("WelcomeActivity","onActivityResult $requestCode $resultCode")
        if(resultCode==1002 && requestCode==1002){
            val intent = Intent()
            intent.setClass(baseContext,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        mTime?.run {
            this.cancel()
        }
        mTime=null
        super.onDestroy()
    }

    fun getSDPath(): String? {
        var sdDir: File? = null
        val sdCardExist = Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory()//获取跟目录
        }
        return sdDir.toString()
    }
}