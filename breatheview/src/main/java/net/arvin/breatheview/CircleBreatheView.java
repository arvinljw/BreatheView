package net.arvin.breatheview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by arvinljw on 17/7/24 16:37
 * Function：
 * Desc：
 */
public class CircleBreatheView extends BreatheView {
    private Point mDrawPoint;

    public CircleBreatheView(Context context) {
        super(context);
    }

    public CircleBreatheView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBreatheView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();

        mDrawPoint = new Point();
    }

    @Override
    protected void drawBreathe(Canvas canvas) {
        canvas.drawCircle(mDrawPoint.x, mDrawPoint.y, mScale, mPaintBreathe);
    }

    @Override
    protected void calculateMaxScale() {
        int width = mScreenWidth - mDrawPoint.x;
        width = width < mDrawPoint.x ? mDrawPoint.x : width;

        int height = mScreenHeight - mDrawPoint.y;
        height = height < mDrawPoint.y ? mDrawPoint.y : height;

        mMaxScale = (int) Math.ceil(Math.sqrt(width * width + height * height));
    }

    /**
     * @param drawPoint 设置绘制的展开的点，若是收回时则是终点
     */
    public void setDrawPoint(Point drawPoint) {
        this.mDrawPoint = drawPoint;
    }

    /**
     * 设置绘制的展开的点，若是收回时则是终点
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void setDrawPoint(int x, int y) {
        this.mDrawPoint.x = x;
        this.mDrawPoint.y = y;
    }
}
