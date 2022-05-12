package com.jhp.cloudmusic.ui.common.adapter
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.sign

/**
 * @CreateDate: 2021/1/14 13:25
 * @Author: ZZJ
 * @Description: 此类用于解决 ViewPager2  嵌套 ViewPager2 或者 RecyclerView 等相互嵌套的冲突问题
 */
class NestedScrollableHost constructor(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    init {
        val var10001 = ViewConfiguration.get(context)
        this.touchSlop = var10001.scaledTouchSlop
    }

    private fun getParentViewPager(): ViewPager2? {
        var mViewParent = this.parent
        if (mViewParent !is View) {
            mViewParent = null
        }
        var view: View?
        view = mViewParent as View

        while (view != null && view !is ViewPager2) {
            mViewParent = view.parent
            if (mViewParent !is View) {
                mViewParent = null
            }
            view = if (mViewParent == null) null else mViewParent as View
        }
        val var2: View? = if (view !is ViewPager2) null else view
        return if (var2 == null) null else var2 as ViewPager2
    }

    private fun getChild(): View? {
        return if (this.childCount > 0) getChildAt(0) else null
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val var5 = false
        val direction = -sign(delta).toInt()
        val var10000: View?
        var var6 = false
        when (orientation) {
            0 -> {
                var10000 = getChild()
                var6 = var10000?.canScrollHorizontally(direction) ?: false
            }
            1 -> {
                var10000 = getChild()
                var6 = var10000?.canScrollVertically(direction) ?: false
            }
            else -> {
            }
        }
        return var6
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        this.handleInterceptTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val var10000 = getParentViewPager()
        if (var10000 != null) {
            val orientation = var10000.orientation
            if (canChildScroll(orientation, -1.0f) || canChildScroll(orientation, 1.0f)) {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = e.x
                        initialY = e.y
                        this.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = e.x - initialX
                        val dy = e.y - initialY
                        val isVpHorizontal = orientation == 0
                        val var8 = false
                        val scaledDx = abs(dx) * if (isVpHorizontal) 0.5f else 1.0f
                        val var9 = false
                        val scaledDy = abs(dy) * if (isVpHorizontal) 1.0f else 0.5f
                        if (scaledDx > touchSlop.toFloat() || scaledDy > touchSlop.toFloat()) {
                            when {
                                isVpHorizontal == scaledDy > scaledDx -> {
                                    this.parent.requestDisallowInterceptTouchEvent(false)
                                }
                                canChildScroll(orientation, if (isVpHorizontal) dx else dy) -> {
                                    this.parent.requestDisallowInterceptTouchEvent(true)
                                }
                                else -> {
                                    this.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}