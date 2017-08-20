package net.arvin.breatheview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by arvinljw on 17/6/20 16:20
 * Function：
 * Desc：
 */
public abstract class BreatheView extends View {
    public static final int ANIM_DEFAULT_DURATION = 500;
    public static final String BG_DEFAULT_COLOR = "#cccccc";

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Paint mBgPaint;
    private RectF mBgRect;

    protected Paint mPaintBreathe;
    protected PorterDuffXfermode mMode;

    protected int mScale;
    protected int mMaxScale;
    protected int mMinScale;

    protected long mDuration = ANIM_DEFAULT_DURATION;

    protected IAnimEndListener mAnimEndListener;
    protected boolean canClose;

    public BreatheView(Context context) {
        this(context, null);
    }

    public BreatheView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreatheView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.parseColor(BG_DEFAULT_COLOR));

        mBgRect = new RectF(0, 0, getScreenWidth(), getScreenHeight());

        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

        mScale = 0;
        mMinScale = 0;

        mPaintBreathe = new Paint();
        mPaintBreathe.setAntiAlias(true);

        mScreenWidth = getScreenWidth();
        mScreenHeight = getScreenHeight();

        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        drawBg(canvas);
        mPaintBreathe.setXfermode(mMode);
        drawBreathe(canvas);
        mPaintBreathe.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }

    protected void drawBg(Canvas canvas) {
        canvas.drawRect(mBgRect, mBgPaint);
    }

    public void setScale(int scale) {
        this.mScale = scale;
        postInvalidate();
    }

    public int getScale() {
        return mScale;
    }

    /**
     * 打开
     */
    public void startScaleBigger() {
        calculateMaxScale();
        startScaleAnim(mMinScale, mMaxScale);
    }

    /**
     * 关闭
     */
    public void startScaleSmaller() {
        calculateMaxScale();
        boolean isOpen = mMaxScale < mMinScale;
        if (!isOpen && !canClose) {
            return;
        }
        startScaleAnim(mMaxScale, mMinScale);
    }

    protected void startScaleAnim(int start, int end) {
        final boolean isOpen = start < end;
        ObjectAnimator scale = ObjectAnimator.ofInt(this, "scale", start, end).setDuration(mDuration);
        scale.setInterpolator(new LinearInterpolator());
        scale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                canClose = isOpen;
                if (mAnimEndListener != null) {
                    mAnimEndListener.onAnimEnd(isOpen);
                }
            }
        });
        scale.start();
    }

    protected void calculateMaxScale() {
        mMaxScale = (int) Math.ceil(Math.sqrt(mScreenWidth * mScreenWidth + mScreenHeight * mScreenHeight));
    }

    public void setAnimEndListener(IAnimEndListener animEndListener) {
        this.mAnimEndListener = animEndListener;
    }

    /**
     * @param color 背景颜色
     */
    public void setBgColor(int color) {
        mBgPaint.setColor(color);
    }

    protected DisplayMetrics getScreenDisplay() {
        return getResources().getDisplayMetrics();
    }

    public int getScreenWidth() {
        return getScreenDisplay().widthPixels;
    }

    public int getScreenHeight() {
        return getScreenDisplay().heightPixels;
    }

    protected int dp2px(float dp) {
        return (int) (dp * getScreenDisplay().density + 0.5f);
    }

    protected int sp2px(float sp) {
        return (int) (sp * getScreenDisplay().scaledDensity + 0.5f);
    }

    protected abstract void drawBreathe(Canvas canvas);

    public interface IAnimEndListener {
        void onAnimEnd(boolean isOpen);
    }
}
