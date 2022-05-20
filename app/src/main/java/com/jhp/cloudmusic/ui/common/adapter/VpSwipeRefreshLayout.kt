package com.jhp.cloudmusic.ui.common.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.MotionEvent
import android.view.ViewConfiguration

internal class VpSwipeRefreshLayout(context: Context?, attrs: AttributeSet?) : SwipeRefreshLayout(
    context!!, attrs
) {
    private var startY = 0f
    private var startX = 0f

    // 记录viewPager是否拖拽的标记
    private var mIsVpDragger = false
    private val mTouchSlop: Int
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                startY = ev.y
                startX = ev.x
                // 初始化标记
                mIsVpDragger = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false
                }

                // 获取当前手指位置
                val endY = ev.y
                val endX = ev.x
                val distanceX = Math.abs(endX - startX)
                val distanceY = Math.abs(endY - startY)
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->                 // 初始化标记
                mIsVpDragger = false
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev)
    }

    init {
        //触发移动事件的最短距离
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }
}