package com.df.androidviewtest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

/**
 * <h3>水波纹按钮</h3>
 * 有四部分组成
 * <p>1 文字，居中布局，可设置文字，文字大小，文字颜色</p>
 * <p>2圆形按钮，可设置颜色和半径</p>
 * <p>3第一次波纹动画</p>
 * <p>4第二次波纹动画</p>
 * <p/>
 * 两次动画时间可设定
 *
 * @author df
 */
public class RippleView extends View {

    /** View  的长宽 */
    protected int mWidth, mHeight;

    // ------------- text -----------------------
    private String mText;
    private int textColor;
    private float textSize = 15;
    private float mTextWidth;
    private float mTextHeight;
    private TextPaint mTextPaint;


    // ------------- button -----------------------
    /** 圆形按钮半径 */
    private float mRippleButtonRadius;
    /** 最大半径，view 的一半 */
    private float mMaxRadiu;
    /** 圆形按钮颜色 */
    private int mRippleButtonColor;

    // --------------------------------------------
    /** 一次动画持续持续时间 */
    private int mAnimationDuraction;
    /** 剩余半径长度 */
    private float mResidueRadius;

    private AnimatorSet mRippleAnimatorSet;
    // ------------- Animation inner -----------------------
    /** 颜色 */
    private int mRippleInnerAnimationColor;
    /** 圆形动画半径1 */
    private float mRippleInnerAnimationRadius;
    private Shader mRippleInnerAnimationColorShader;
    private int mRippleInnerAnimationColorGradientStart;
    private int mRippleInnerAnimationColorGradientEnd;

    // ------------- Animation Outer -----------------------

    private int mRippleOuterAnimationColor;
    private float mRippleOuterAnimationRadius;
    private Shader mRippleOuterAnimationColorShader;
    private int mRippleOuterAnimationColorGradientStart;
    private int mRippleOuterAnimationColorGradientEnd;

    /** 外半径 */
    private float mRippleOuterDelayAnimationRadius, mRippleOuterDeferAnimationRadius;


    public RippleView(Context context) {
        super(context);
        init(null, 0);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RippleView, defStyle, 0);

        mText = a.getString(R.styleable.RippleView_text);
        textColor = a.getColor(R.styleable.RippleView_textColor, 0);
        textSize = a.getDimension(R.styleable.RippleView_textSize, textSize);
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        if (!TextUtils.isEmpty(mText)) {
            mTextWidth = mTextPaint.measureText(mText);
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            mTextHeight = fontMetrics.bottom;
        }
        //--------------------------------------------------------------------------------
        mAnimationDuraction = a.getInt(R.styleable.RippleView_animationDuraction, 0);
        //--------------------------------------------------------------------------------
        mRippleButtonRadius = a.getDimension(R.styleable.RippleView_buttonRadius, 0);
        mRippleButtonColor = a.getColor(R.styleable.RippleView_buttonColor, Color.TRANSPARENT);

        //--------------------------------------------------------------------------------
        mRippleInnerAnimationColor = a.getColor(R.styleable.RippleView_rippleInnerAnimationColor, mRippleButtonColor);
        mRippleInnerAnimationRadius = a.getDimension(R.styleable.RippleView_rippleInnerAnimationRadius, mRippleButtonRadius);
        mRippleInnerAnimationColorGradientStart = a.getColor(R.styleable.RippleView_rippleInnerAnimationColorGradientStart, mRippleInnerAnimationColor);
        mRippleInnerAnimationColorGradientEnd = a.getColor(R.styleable.RippleView_rippleInnerAnimationColorGradientEnd, mRippleInnerAnimationColor);

        //--------------------------------------------------------------------------------
        mRippleOuterAnimationColor = a.getColor(R.styleable.RippleView_rippleOuterAnimationColor, mRippleButtonColor);
        mRippleOuterAnimationRadius = a.getDimension(R.styleable.RippleView_rippleOuterAnimationRadius, mRippleButtonRadius);
        mRippleOuterAnimationColorGradientStart = a.getColor(R.styleable.RippleView_rippleOuterAnimationColorGradientStart, mRippleOuterAnimationColor);
        mRippleOuterAnimationColorGradientEnd = a.getColor(R.styleable.RippleView_rippleOuterAnimationColorGradientEnd, mRippleOuterAnimationColor);
        //--------------------------------------------------------------------------------
        a.recycle();
    }


    /**
     * Called from layout when this view should
     * assign a size and position to each of its children.
     * <p/>
     * Derived classes with children should override
     * this method and call layout on each of
     * their children.
     *
     * @param changed This is a new size or position for this view
     * @param left    Left position, relative to parent
     * @param top     Top position, relative to parent
     * @param right   Right position, relative to parent
     * @param bottom  Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getWidth();
        mHeight = getHeight();

        mMaxRadiu = mWidth > mHeight ? mHeight >> 1 : mWidth >> 1;
        mRippleButtonRadius = mRippleButtonRadius > mMaxRadiu ? mMaxRadiu : mRippleButtonRadius;

        mResidueRadius = mMaxRadiu - mRippleButtonRadius;

        mRippleInnerAnimationColorShader = new LinearGradient(mWidth, 0, 0, mHeight, mRippleInnerAnimationColorGradientStart, mRippleInnerAnimationColorGradientEnd, Shader.TileMode.CLAMP);
        mRippleOuterAnimationColorShader = new LinearGradient(mWidth, 0, 0, mHeight, mRippleOuterAnimationColorGradientStart, mRippleOuterAnimationColorGradientEnd, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mWidth >> 1,
                mHeight >> 1,
                mRippleButtonRadius,
                initmPaint(Paint.Style.FILL, mRippleInnerAnimationColor, mRippleInnerAnimationColorShader)
        );


        if (mRippleOuterAnimationRadius > mRippleInnerAnimationRadius && mRippleOuterAnimationRadius < mMaxRadiu) {
            canvas.drawCircle(mWidth >> 1,
                    mHeight >> 1,
                    mRippleOuterAnimationRadius,
                    initmPaint(Paint.Style.FILL,
                            mRippleOuterAnimationColor,
                            mRippleInnerAnimationColorShader,
                            (int) ((mMaxRadiu - mRippleOuterAnimationRadius) / mResidueRadius * 255)
                    )
            );
        }
        if (mRippleOuterDelayAnimationRadius > mRippleInnerAnimationRadius && mRippleOuterDelayAnimationRadius < mMaxRadiu) {
            canvas.drawCircle(mWidth >> 1,
                    mHeight >> 1,
                    mRippleOuterDelayAnimationRadius,
                    initmPaint(Paint.Style.FILL,
                            mRippleOuterAnimationColor,
                            mRippleOuterAnimationColorShader,
                            (int) ((mMaxRadiu - mRippleOuterDelayAnimationRadius) / mResidueRadius * 255)
                    )
            );
        }
        if (mRippleOuterDeferAnimationRadius > mRippleInnerAnimationRadius && mRippleOuterDeferAnimationRadius < mMaxRadiu) {
            canvas.drawCircle(mWidth >> 1,
                    mHeight >> 1,
                    mRippleOuterDeferAnimationRadius,
                    initmPaint(Paint.Style.FILL,
                            mRippleOuterAnimationColor,
                            mRippleOuterAnimationColorShader,
                            (int) ((mMaxRadiu - mRippleOuterDeferAnimationRadius) / mResidueRadius * 255)
                    )
            );
        }

        if (mTextWidth > 0 && mTextHeight > 0) {
            canvas.drawText(mText,
                    getPaddingLeft() + (mWidth - mTextWidth) / 2,
                    getPaddingTop() + (mHeight + mTextHeight) / 2,
                    mTextPaint);
        }

    }

    protected Paint mPaint;

    protected Paint initmPaint() {
        if (mPaint == null) {
            return mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        else {
            mPaint.reset();

            mPaint.setShader(null);
            mPaint.clearShadowLayer();

            mPaint.setAntiAlias(true);
        }

        return mPaint;
    }

    protected Paint initmPaint(Paint.Style style, int colorValue, Shader shader, int alpha) {
        return initmPaint(style, 0, colorValue, shader, 0, Color.TRANSPARENT, 0, 0, alpha);
    }

    protected Paint initmPaint(Paint.Style style, int colorValue, Shader shader) {
        return initmPaint(style, 0, colorValue, shader, 0, Color.TRANSPARENT, 0, 0, -1);
    }

    protected Paint initmPaint(Paint.Style style, float strokeWidth, int colorValue, Shader shader, float shadowRadius, int shadowColor, float shadowDx, float shadowDy, int alpha) {
        //
        initmPaint();
        //
        if (style != null) {
            mPaint.setStyle(style);
        }
        if (strokeWidth != 0) {
            mPaint.setStrokeWidth(strokeWidth);
        }

        // When the shadow color can not be set to transparent, but can not set
        if (shader == null) {
            mPaint.setColor(colorValue);
        }
        else {
            mPaint.setShader(shader);
        }

        if (shadowRadius != 0) {
            mPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }
        if (alpha != -1) {
            mPaint.setAlpha(alpha);
        }
        return mPaint;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void play() {
        mRippleAnimatorSet = new AnimatorSet();
        ObjectAnimator mRippleOuterCircleObjectAnimator = ObjectAnimator.ofFloat(
                this,
                mRippleAnimationRadiusProperty,
                mRippleButtonRadius,
                mRippleButtonRadius + mResidueRadius * 3
        );

        mRippleOuterCircleObjectAnimator.setDuration(mAnimationDuraction);
        mRippleAnimatorSet.playTogether(mRippleOuterCircleObjectAnimator);

        mRippleAnimatorSet.start();
    }


    private Property<RippleView, Float> mRippleAnimationRadiusProperty = new Property<RippleView, Float>(Float.class, "mRippleAnimationRadiusProperty") {
        @Override
        public Float get(RippleView object) {
            return object.mRippleButtonRadius;
        }

        @Override
        public void set(RippleView object, Float value) {
            object.mRippleOuterAnimationRadius = value;
            object.mRippleOuterDelayAnimationRadius = value - mResidueRadius * 0.5f;
            object.mRippleOuterDeferAnimationRadius = value - mResidueRadius * 1.0f;
            invalidate();
        }
    };

    /**
     * 设置颜色渐变值
     *
     * @param ColorGradientStart 渐变颜色起始值
     * @param ColorGradientEnd   渐变颜色结束值
     */
    public void setRippleInnerAnimationColor(int ColorGradientStart, int ColorGradientEnd) {
        mRippleInnerAnimationColorGradientStart = ColorGradientStart;
        mRippleInnerAnimationColorGradientEnd = ColorGradientEnd;
        mRippleInnerAnimationColorShader = new LinearGradient(mWidth, 0, 0, mHeight, mRippleInnerAnimationColorGradientStart, mRippleInnerAnimationColorGradientEnd, Shader.TileMode.CLAMP);
    }

    /**
     * 设置颜色渐变值
     *
     * @param ColorGradientStart 渐变颜色起始值
     * @param ColorGradientEnd   渐变颜色结束值
     */
    public void setRippleOuterAnimationColor(int ColorGradientStart, int ColorGradientEnd) {
        mRippleOuterAnimationColorGradientStart = ColorGradientStart;
        mRippleOuterAnimationColorGradientEnd = ColorGradientEnd;
        mRippleOuterAnimationColorShader = new LinearGradient(mWidth, 0, 0, mHeight, mRippleOuterAnimationColorGradientStart, mRippleOuterAnimationColorGradientEnd, Shader.TileMode.CLAMP);
    }

}
