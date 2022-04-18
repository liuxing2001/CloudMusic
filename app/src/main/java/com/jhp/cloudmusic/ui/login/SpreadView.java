package com.jhp.cloudmusic.ui.login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.view.View;

import com.jhp.cloudmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩散动画
 *
 * @author : jhp
 * @date : 2022-04-18 10:24
 */
public class SpreadView extends View {
    private final Paint centerPaint; //中心圆paint
    private int radius = 100; //中心圆半径
    private final Paint spreadPaint; //扩散圆paint
    private float centerX;//圆心x
    private float centerY;//圆心y
    private int distance = 5; //每次圆递增间距
    private int maxRadius = 80; //最大圆半径
    private final List<Integer> spreadRadius = new ArrayList<>();//扩散圆层级数，元素为扩散的距离
    private final List<Integer> alphas = new ArrayList<>();//对应每层圆的透明度

    public SpreadView(Context context) {
        this(context, null, 0);
    }

    public SpreadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpreadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpreadView, defStyleAttr, 0);
        radius = a.getInt(R.styleable.SpreadView_spread_radius, radius);
        maxRadius = a.getInt(R.styleable.SpreadView_spread_max_radius, maxRadius);
        int centerColor = a.getColor(R.styleable.SpreadView_spread_center_color,
                ContextCompat.getColor(context, android.R.color.holo_red_dark));
        int spreadColor = a.getColor(R.styleable.SpreadView_spread_spread_color,
                ContextCompat.getColor(context, R.color.white));
        distance = a.getInt(R.styleable.SpreadView_spread_distance, distance);
        a.recycle();
        //中心画笔
        centerPaint = new Paint();
        centerPaint.setColor(centerColor);
        centerPaint.setAntiAlias(true);
        //最开始不透明且扩散距离为0
        alphas.add(255);
        spreadRadius.add(0);
        spreadPaint = new Paint();
        spreadPaint.setAntiAlias(true);
        //空心圆
        spreadPaint.setStyle(Paint.Style.STROKE);
        spreadPaint.setAlpha(255);
        spreadPaint.setColor(spreadColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆心位置
        centerX = w >> 1;
        centerY = h >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = spreadRadius.size();
        for (int i = 0; i < size; i++) {
            int alpha = alphas.get(i);
            spreadPaint.setAlpha(alpha);
            int width = spreadRadius.get(i);
            //绘制扩散的圆
            canvas.drawCircle(centerX, centerY, radius + width, spreadPaint);

            //每次扩散圆半径递增,圆透明度递减
            if (alpha > 0 && width < 300) {
                alpha = alpha - distance > 0 ? alpha - distance : 1;
                alphas.set(i, alpha);
                //添加新的值
                spreadRadius.set(i, width + distance);
            }
        }
        //当最外层扩散圆半径达到最大半径时添加新扩散圆
        if (spreadRadius.get(spreadRadius.size() - 1) > maxRadius) {
            spreadRadius.add(0);
            alphas.add(255);
        }
        //超过8个扩散圆,删除最先绘制的圆,即最外层的圆
        if (spreadRadius.size() >= 8) {
            alphas.remove(0);
            spreadRadius.remove(0);
        }
        //中间的圆
        canvas.drawCircle(centerX, centerY, radius, centerPaint);

        //延迟更新,达到扩散视觉差效果
        //扩散延迟间隔，越大扩散越慢
        int delayMilliseconds = 33;
        postInvalidateDelayed(delayMilliseconds);
    }
}
