package cc.manbu.schoolinfocommunication.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.manbu.schoolinfocommunication.bean.ViewItemBean
import cn.yc.model.listener.BaseOnItemListener
import kotlinx.android.synthetic.main.item_ed_empty.view.*
import kotlinx.android.synthetic.main.item_me_header.view.*
import kotlinx.android.synthetic.main.item_view_content.view.*
import org.xutils.common.util.LogUtil
import org.xutils.image.ImageOptions
import org.xutils.x
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.httputils.MyCallBack

/**
 * Created by manbuAndroid5 on 2017/4/19.
 */

class MeFragmentAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder?> {

    var context: Context?=null
    var list: ArrayList<ViewItemBean>?=null

    var mViewHolder:MyViewHoder?=null
    var mHeaderViewHolder:MyHeaderViewHoder?=null
    var mEmptyViewHolder:MyEmptyViewHoder?=null

    var mListener: BaseOnItemListener<ViewItemBean?>?=null

    fun setListener(listener:BaseOnItemListener<ViewItemBean?>){
        mListener=listener
    }

    constructor(context: Context,list: ArrayList<ViewItemBean>){
        this.context=context
        this.list=list
    }

    fun addItem(bean:ViewItemBean){
        list?.let {
            if(it.size>0){
                it[0] = bean
                notifyItemChanged(0)
            }else{
                it.add(0,bean)
                notifyItemInserted(0)
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return list?.get(position)?.type?:10
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder?{
        when(viewType){
            20->{
                mHeaderViewHolder= MyHeaderViewHoder(LayoutInflater.from(context)
                        .inflate(R.layout.item_me_header,parent,false))
                return mHeaderViewHolder
            }
            30->{
                mEmptyViewHolder= MyEmptyViewHoder(LayoutInflater.from(context)
                        .inflate(R.layout.item_ed_empty,parent,false))
                return mEmptyViewHolder
            }
            else->{
                mViewHolder= MyViewHoder(LayoutInflater.from(context)
                        .inflate(R.layout.item_view_content,parent,false))
                return mViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder?.let {
            val type=list?.get(position)?.type?:10
            val data=list?.get(position)
            when(type){
                20->{
                    mHeaderViewHolder=it as? MyHeaderViewHoder
                    mHeaderViewHolder?.let {
                        it.itemView.id_tvLoginName?.text=data?.name
                        val options = ImageOptions.Builder().setFadeIn(true).setCircular(true).setFailureDrawableId(R.drawable.pic_head).build() //淡入效果
                        data?.let {
                            it.uri?.let {
                                x.image().loadDrawable(it, options, object : MyCallBack<Drawable>() {
                                    override fun onError(ex: Throwable, isOnCallback: Boolean) {
                                        super.onError(ex, isOnCallback)
                                        LogUtil.e("======onError======")
                                    }

                                    override fun onFinished() {
                                        super.onFinished()
                                        LogUtil.e("======onFinished======")
                                    }

                                    override fun onSuccess(result: Drawable?) {
                                        super.onSuccess(result)
                                        LogUtil.e("======onSuccess======")
                                        if (result != null)
                                            mHeaderViewHolder?.itemView?.id_ivHead?.setImageDrawable(result)
                                        else
                                            mHeaderViewHolder?.itemView?.id_ivHead?.setImageResource(R.drawable.pic_head)
                                    }
                                })
                                return
                            }
                        }
                        mHeaderViewHolder?.itemView?.id_ivHead?.setImageResource(R.drawable.pic_head)
                    }
                }
                30->{
                    mEmptyViewHolder=it as? MyEmptyViewHoder
                    mEmptyViewHolder?.itemView?.let {
                        it.empty_8?.visibility=View.GONE
                        it.empty_6?.visibility=View.VISIBLE
                        it.empty_6.setBackgroundColor(Color.BLACK)
                        context?.run {
                            it.empty_6.setBackgroundColor(resources.getColor(R.color.black3))
                        }
                    }
                }
                else->{
                    mViewHolder=it as? MyViewHoder
                    mViewHolder?.let {
                        it.itemView?.let {
                            with(it){

                                id_tvName?.setTextColor(resources.getColor(R.color.toolbar_text_color))
                                id_tvValue?.text=data?.value
                                id_tvName?.text=data?.name
                                id_tvValue?.setTextColor(resources.getColor(R.color.toolbar_text_color))
                                if(position==3 || position==itemCount-1){
                                   devider?.visibility=View.GONE
                                } else{
                                   devider?.visibility=View.VISIBLE
                                }
                                setOnClickListener { mListener?.itemClick(list?.get(position)) }
                            }
                        }
                    }
                }
            }
        }
    }

     class MyViewHoder:RecyclerView.ViewHolder{
         constructor(view:View):super(view)
    }

    class MyHeaderViewHoder:RecyclerView.ViewHolder{
        constructor(view:View):super(view)
    }

    class MyEmptyViewHoder:RecyclerView.ViewHolder{
        constructor(view:View):super(view)
    }
}
