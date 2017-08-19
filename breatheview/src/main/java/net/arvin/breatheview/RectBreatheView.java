package net.arvin.breatheview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.util.AttributeSet;

/**
 * Created by arvinljw on 17/7/24 16:37
 * Function：
 * Desc：
 */
public class RectBreatheView extends BreatheView {
    private Rect mDrawRect;

    private Rect mBreatheRect;

    public RectBreatheView(Context context) {
        super(context);
    }

    public RectBreatheView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectBreatheView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();

        mDrawRect = new Rect(0, mScreenHeight / 2, mScreenWidth, mScreenHeight / 2);

        mBreatheRect = new Rect(0, 0, 0, 0);

        mDuration = 400;
    }

    public void setScale(int scale) {
        this.mScale = scale;

        int left = mDrawRect.left - mScale;
        mBreatheRect.left = left < 0 ? 0 : left;

        int right = mDrawRect.right + mScale;
        mBreatheRect.right = right > mScreenWidth ? mScreenWidth : right;

        int top = mDrawRect.top - mScale;
        mBreatheRect.top = top < 0 ? 0 : top;

        int bottom = mDrawRect.bottom + mScale;
        mBreatheRect.bottom = bottom > mScreenHeight ? mScreenHeight : bottom;

        postInvalidate();
    }

    @Override
    protected void drawBreathe(Canvas canvas) {
        canvas.drawRect(mBreatheRect, mPaintBreathe);
    }

    @Override
    protected void calculateMaxScale() {
        int scaleX = mDrawRect.left < mScreenWidth - mDrawRect.right ? mScreenWidth - mDrawRect.right : mDrawRect.left;

        int scaleY = mDrawRect.top < mScreenHeight - mDrawRect.bottom ? mScreenHeight - mDrawRect.bottom : mDrawRect.top;

        mMaxScale = Math.max(scaleX, scaleY);
    }

    /**
     * @param drawRect 设置绘制的展开的矩形，若是收回时则是结束的矩形
     */
    public void setDrawRect(Rect drawRect) {
        this.mDrawRect = drawRect;
    }

    public void setDrawRect(int left, int top, int right, int bottom) {
        this.mDrawRect.left = left;
        this.mDrawRect.top = top;
        this.mDrawRect.right = right;
        this.mDrawRect.bottom = bottom;
    }
}
