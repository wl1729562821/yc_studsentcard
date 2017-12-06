package cn.yc.student.view.fragment


import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextPaint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import cc.manbu.schoolinfocommunication.base.BaseFragmentStudent
import cc.manbu.schoolinfocommunication.R
import cc.manbu.schoolinfocommunication.base.registerEventbus
import cc.manbu.schoolinfocommunication.base.unregisterEventbus
import cc.manbu.schoolinfocommunication.config.Constant
import cc.manbu.schoolinfocommunication.events.ResposeEvent
import cc.manbu.schoolinfocommunication.events.ViewEvent
import cc.manbu.schoolinfocommunication.listener.HttpRespnse
import cc.manbu.schoolinfocommunication.listener.http.HttpCallListener
import cc.manbu.schoolinfocommunication.tools.Utils
import cc.manbu.schoolinfocommunication.bean.Device_Geography
import cc.manbu.schoolinfocommunication.view.adapter.E_ZoneListAdapter

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.view.annotation.ContentView
import org.xutils.view.annotation.Event
import org.xutils.view.annotation.ViewInject
import org.xutils.x

import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_e_zone_list.*

/**
 * A simple [Fragment] subclass.
 */
class E_ZoneListFragment : BaseFragmentStudent() {
    private var rootView: View? = null

    private var adapter: E_ZoneListAdapter? = null
    private val list = ArrayList<Device_Geography>()

    private val mPaint = Paint()

    //绘制字体
    internal var p = Paint()

    /**
     * 需要绘制的文字
     */
    private var mText: String? = null
    /**
     * 文本的颜色
     */
    private var mTextColor: Int = 0
    /**
     * 文本的大小
     */
    private var mTextSize: Int = 0
    /**
     * 绘制时控制文本绘制的范围
     */
    private var mTextPaint: Paint? = null
    private var mBound: Rect? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_e_zone_list
    }

    override fun initView() {
        registerEventbus(this)
        mLoadingDoialog?.show()
        mNetHelper.accessE_ZoneList()
        mText = "删除"
        mTextColor = Color.WHITE
        mTextSize = 30

        mTextPaint = Paint()
        mTextPaint!!.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16f, context.resources.displayMetrics)
        mTextPaint!!.color = mTextColor
        mBound = Rect()
        mTextPaint!!.getTextBounds(mText, 0, mText!!.length, mBound)

        id_refreshLayout.setColorSchemeResources(R.color.chocolate,
                R.color.hotpink,
                R.color.crimson,
                R.color.orchid)
        id_refreshLayout.setOnRefreshListener {
            mNetHelper.accessE_ZoneList()
        }
        id_refreshLayout.setProgressViewEndTarget(true, 120)//设置距离顶端的距离
        adapter = E_ZoneListAdapter(context)
        id_lvE_zone.layoutManager = LinearLayoutManager(activity)
        val itemTouchHelper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val position = viewHolder.adapterPosition
                        if (adapter != null) {
                            if (adapter!!.itemCount > 0) {
                                if (adapter!!.itemCount > position - 1) {
                                    mLoadingDoialog?.show()
                                    val data = adapter!!.data[position]
                                    mNetHelper.deleteE_Zone(data._id,
                                            object : HttpCallListener {
                                                override fun <T> onNext(data: HttpRespnse<T>) {
                                                    mLoadingDoialog?.dismiss()
                                                    adapter!!.remove(position)
                                                }

                                                override fun onError(code: Int, msg: String?) {
                                                    mLoadingDoialog?.dismiss()
                                                    Toast.makeText(activity, getString(R.string.network_error1), Toast.LENGTH_SHORT).show()
                                                    adapter!!.remove(position, true)
                                                }
                                            })
                                }
                            }

                        }
                    }

                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            if (viewHolder is E_ZoneListAdapter.MyViewHolder) {
                                val itemView = viewHolder.itemView
                                if (itemView != null) {
                                    if (dX < 0) {
                                        mPaint.color = ContextCompat.getColor(context, R.color.red500)
                                        val x = -dX
                                        val left = itemView.right - x + Utils.dpToPx(0)
                                        val right = itemView.right + Utils.dpToPx(0)
                                        c.drawRect(left,
                                                itemView.top.toFloat(),
                                                right.toFloat(),
                                                itemView.bottom.toFloat(), mPaint)
                                        val height = ((itemView.bottom - itemView.top) / 2 - mBound!!.height() / 2).toFloat()
                                        c.drawText(mText!!,
                                                itemView.width * 0.96f - mBound!!.right,
                                                itemView.bottom - height, mTextPaint!!)
                                    }
                                }
                            }
                        }
                    }

                    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                        var swipeFlags = ItemTouchHelper.LEFT
                        if (viewHolder is E_ZoneListAdapter.AddViewHolder) {
                            swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE
                        }
                        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
                    }
                }
        )
        itemTouchHelper.attachToRecyclerView(id_lvE_zone)
        id_lvE_zone.setAdapter(adapter)
    }

    override fun onDestroy() {
        unregisterEventbus(this)
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            if (adapter != null) {
                val events = ViewEvent()
                if (adapter!!.itemCount >= 6) {
                    events.setMessage(Constant.EVENT_HIDE_RIGHT_BUTTON)
                } else {
                    events.setMessage(Constant.EVENT_SHOW_RIGHT_BUTTON)
                }
                EventBus.getDefault().post(events)
                //adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ResposeEvent) {
        val msg = event.getMessage()
        if ("SearchDevice_Geography_V2" == msg) {
            mLoadingDoialog?.dismiss()
            id_refreshLayout.setRefreshing(false)
            val geographies = event.getGeographies()
            list.clear()
            list.addAll(geographies)
            if (adapter != null) {
                adapter!!.data = list
                val events = ViewEvent()
                if (adapter!!.itemCount >= 6) {
                    events.setMessage(Constant.EVENT_HIDE_RIGHT_BUTTON)
                } else {
                    events.setMessage(Constant.EVENT_SHOW_RIGHT_BUTTON)
                }
                EventBus.getDefault().post(events)
                //adapter.notifyDataSetChanged();
            }
            fragment_zone_list_all.setVisibility(View.GONE)
            id_refreshLayout.setVisibility(View.VISIBLE)
            /*if (list.size() <= 0) {
                id_llEmpty.setVisibility(View.VISIBLE);
                id_refreshLayout.setVisibility(View.GONE);
            } else {
                id_llEmpty.setVisibility(View.GONE);
                id_refreshLayout.setVisibility(View.VISIBLE);
            }*/
        } else if ("DeleteGeography" == msg) {
            mLoadingDoialog?.dismiss()
            val pos = event.getFlg()
            list.removeAt(pos)
            if (adapter != null) {
                val events = ViewEvent()
                if (adapter!!.itemCount >= 5) {
                    events.setMessage(Constant.EVENT_HIDE_RIGHT_BUTTON)
                } else {
                    events.setMessage(Constant.EVENT_SHOW_RIGHT_BUTTON)
                }
                EventBus.getDefault().post(events)
                adapter!!.notifyDataSetChanged()
            }
            if (list.size <= 0) {
                fragment_zone_list_all.setVisibility(View.VISIBLE)
                id_refreshLayout.setVisibility(View.GONE)
            } else {
                fragment_zone_list_all.setVisibility(View.GONE)
                id_refreshLayout.setVisibility(View.VISIBLE)
            }
        }
    }


    private fun showDeleteDialog(pos: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.text_tips)
        builder.setMessage(R.string.text_delete_zone)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setPositiveButton(R.string.text_ok) { dialog, which ->
            dialog.dismiss()
            mLoadingDoialog?.show()
            mNetHelper.deleteGeography(list[pos], pos)
        }
        builder.create().show()
    }
}// Required empty public constructor
