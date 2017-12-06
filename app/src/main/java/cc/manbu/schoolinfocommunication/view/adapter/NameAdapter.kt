package cn.yc.student.view.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.manbu.schoolinfocommunication.base.BaseActivityStudent
import cc.manbu.schoolinfocommunication.bean.NameListBean
import cn.yc.model.listener.BaseOnItemListener
import cn.yc.student.bean.ViewHoler
import kotlinx.android.synthetic.main.item_login_name.view.*
import cc.manbu.schoolinfocommunication.R
/**
 * Created by Administrator on 2017/10/26 0026.
 */

class NameAdapter(internal var mAtv: BaseActivityStudent,
                  internal var mData: ArrayList<NameListBean>,
                  internal var mListener: BaseOnItemListener<ViewHoler<NameListBean>>
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public var mViewHolder: NameViewHolder? = null
    public var mLayout: LayoutInflater? = null

    private var click = false

    fun setClick(click: Boolean) {
        this.click = click
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("EdAdapter", "onBindViewHolder" + itemCount)
        mData?.get(position)?.let {data->
            (holder as? NameViewHolder)?.let {
                with(it.itemView){
                    divider.visibility=if(position in 0 until (itemCount-1)){
                        View.VISIBLE
                    }else{
                        View.GONE
                    }

                    id_tvName.text = data.name
                    id_ivClear.setOnClickListener {
                        val data=ViewHoler(1000,data)
                        mListener?.itemClick(data)
                    }
                    id_ivClear.tag = position
                    setOnClickListener {
                        if(!click){
                            mListener?.let {
                                val data=ViewHoler(1001,data)
                                mListener?.itemClick(data)
                            }
                        }
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return mData?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        mLayout = mLayout ?: LayoutInflater.from(mAtv)
        mViewHolder = NameViewHolder(mLayout?.inflate(R.layout.item_login_name, parent, false))
        return mViewHolder
    }

    inner class NameViewHolder(view: View?) : RecyclerView.ViewHolder(view)
}