package com.df.androidviewtest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * @author Tunasashimi
 * @date Jun 4, 2015 7:53:55 AM
 * @Copyright 2015 TunaSashimi. All rights reserved.
 * @Description
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TunaRipple extends TunaView {

    private float tunaRippleInnerCircleRadius;


    private int tunaRippleInnerCircleColor;

    //
    private float tunaRippleOuterCircleRadius;


    // delay
    private float tunaRippleOuterDelayCircleRadius;


    // defer
    private float tunaRippleOuterDeferCircleRadius;

    private int tunaRippleOuterCircleColor;


    private float tunaRippleTextSize;


    private int tunaRippleTextColor;


    private String tunaRippleTextValue;

    private float tunaRippleTextDx;


    private float tunaRippleTextDy;


    private float tunaRippleTextFractionDx;

    private float tunaRippleTextFractionDy;


    private int tunaRippleDuraction;

    private int tunaRippleInnerCircleAngle;

    private int tunaRippleOuterCircleAngle;


    // tunaRippleInnerCircleColorGradientStart default tunaRippleInnerCircleColor
    private int tunaRippleInnerCircleColorGradientStart;


    // tunaRippleInnerCircleColorGradientEnd default tunaRippleInnerCircleColor
    private int tunaRippleInnerCircleColorGradientEnd;

    // tunaRippleOuterCircleColorGradientStart default tunaRippleOuterCircleColor
    private int tunaRippleOuterCircleColorGradientStart;


    // tunaRippleOuterCircleColorGradientEnd default tunaRippleOuterCircleColor
    private int tunaRippleOuterCircleColorGradientEnd;


    // tunaRippleInnerCircleColorShader default null
    private Shader tunaRippleInnerCircleColorShader;


    // tunaRippleOuterCircleColorShader default null
    private Shader tunaRippleOuterCircleColorShader;


    //
    private TimeInterpolator tunaRippleTimeInterpolator;


    public enum TunaRippleTimeInterpolator {
        ACCELERATEDECELERATEINTERPOLATOR(0), ACCELERATEINTERPOLATOR(1), ANTICIPATEINTERPOLATOR(2), ANTICIPATEOVERSHOOTINTERPOLATOR(3), BOUNCEINTERPOLATOR(4), CYCLEINTERPOLATOR(5), DECELERATEINTERPOLATOR(
                6), LINEARINTERPOLATOR(7), OVERSHOOTINTERPOLATOR(8),;
        final int nativeInt;

        TunaRippleTimeInterpolator(int ni) {
            nativeInt = ni;
        }
    }

    //
    private static final TimeInterpolator[] tunaRippleTimeInterpolatorArray = {
            new AccelerateDecelerateInterpolator()
            , new AccelerateInterpolator()
            , new AnticipateInterpolator()
            , new AnticipateOvershootInterpolator()
            , new BounceInterpolator()
            , new CycleInterpolator(0)
            , new DecelerateInterpolator()
            , new LinearInterpolator()
            , new OvershootInterpolator()
            };

    public static TimeInterpolator[] getTunarippletimeinterpolatorarray() {
        return tunaRippleTimeInterpolatorArray;
    }

    //
    private float tunaRippleMaxRadius;
    private float tunaRippleDeltaRadius;
    private float tunaRippleAnimationCircleRadius;

    //
    private AnimatorSet tunaRippleAnimatorSet;

    public AnimatorSet getTunaRippleAnimatorSet() {
        return tunaRippleAnimatorSet;
    }

    public void setTunaRippleAnimatorSet(AnimatorSet tunaRippleAnimatorSet) {
        this.tunaRippleAnimatorSet = tunaRippleAnimatorSet;
    }

    private Property<TunaRipple, Float> tunaRippleAnimationCircleRadiusProperty = new Property<TunaRipple, Float>(Float.class, "tunaRippleAnimationCircleRadiusProperty") {
        @Override
        public Float get(TunaRipple object) {
            return object.tunaRippleAnimationCircleRadius;
        }

        @Override
        public void set(TunaRipple object, Float value) {
            object.tunaRippleAnimationCircleRadius = value;
            // We need to draw three radius
            object.tunaRippleOuterCircleRadius = value;
            object.tunaRippleOuterDelayCircleRadius = value - tunaRippleDeltaRadius * 0.5f;
            object.tunaRippleOuterDeferCircleRadius = value - tunaRippleDeltaRadius * 1.0f;
            invalidate();
        }
    };

    public TunaRipple(Context context, AttributeSet attrs) {
        super(context, attrs);

        tunaTag = TunaRipple.class.getSimpleName();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TunaRipple);

        tunaRippleInnerCircleRadius = typedArray.getDimension(R.styleable.TunaRipple_tunaRippleInnerCircleRadius, 0);
        tunaRippleInnerCircleColor = typedArray.getColor(R.styleable.TunaRipple_tunaRippleInnerCircleColor, Color.TRANSPARENT);

        //
        tunaRippleAnimationCircleRadius = tunaRippleInnerCircleRadius;

        tunaRippleOuterCircleColor = typedArray.getColor(R.styleable.TunaRipple_tunaRippleOuterCircleColor, tunaRippleInnerCircleColor);

        tunaRippleTextSize = typedArray.getDimension(R.styleable.TunaRipple_tunaRippleTextSize, 0);
        tunaRippleTextColor = typedArray.getColor(R.styleable.TunaRipple_tunaRippleTextColor, Color.TRANSPARENT);

        tunaRippleTextValue = typedArray.getString(R.styleable.TunaRipple_tunaRippleTextValue);

        //
        tunaRippleTextDx = typedArray.getDimension(R.styleable.TunaRipple_tunaRippleTextDx, 0);
        tunaRippleTextDy = typedArray.getDimension(R.styleable.TunaRipple_tunaRippleTextDy, 0);
        tunaRippleTextFractionDx = typedArray.getFraction(R.styleable.TunaRipple_tunaRippleTextFractionDx, 1, 1, 0);
        tunaRippleTextFractionDy = typedArray.getFraction(R.styleable.TunaRipple_tunaRippleTextFractionDy, 1, 1, 0);

        //
        tunaRippleInnerCircleAngle = typedArray.getInt(R.styleable.TunaRipple_tunaRippleInnerCircleAngle, Integer.MAX_VALUE);
        if (tunaRippleInnerCircleAngle != Integer.MAX_VALUE) {
            tunaRippleInnerCircleColorGradientStart = typedArray.getColor(R.styleable.TunaRipple_tunaRippleInnerCircleColorGradientStart, tunaRippleInnerCircleColor);
            tunaRippleInnerCircleColorGradientEnd = typedArray.getColor(R.styleable.TunaRipple_tunaRippleInnerCircleColorGradientEnd, tunaRippleInnerCircleColor);
        }

        tunaRippleOuterCircleAngle = typedArray.getInt(R.styleable.TunaRipple_tunaRippleOuterCircleAngle, tunaRippleInnerCircleAngle);
        if (tunaRippleOuterCircleAngle != Integer.MAX_VALUE) {
            tunaRippleOuterCircleColorGradientStart = typedArray.getColor(R.styleable.TunaRipple_tunaRippleOuterCircleColorGradientStart, tunaRippleInnerCircleColorGradientStart);
            tunaRippleOuterCircleColorGradientEnd = typedArray.getColor(R.styleable.TunaRipple_tunaRippleOuterCircleColorGradientEnd, tunaRippleInnerCircleColorGradientEnd);
        }

        tunaRippleDuraction = typedArray.getInt(R.styleable.TunaRipple_tunaRippleDuraction, 0);

        int tunaRippleTimeInterpolatorIndex = typedArray.getInt(R.styleable.TunaRipple_tunaRippleTimeInterpolator, -1);
        if (tunaRippleTimeInterpolatorIndex > -1) {
            tunaRippleTimeInterpolator = tunaRippleTimeInterpolatorArray[tunaRippleTimeInterpolatorIndex];
        }
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        tunaRippleMaxRadius = tunaWidth > tunaHeight ? tunaHeight >> 1 : tunaWidth >> 1;
        tunaRippleDeltaRadius = tunaRippleMaxRadius - tunaRippleInnerCircleRadius;

        if (tunaRippleInnerCircleRadius >= tunaRippleMaxRadius) {
            throw new IllegalArgumentException("The content attribute tunaRippleInnerCircleRadius length must be less than half of the width or height");
        }

        tunaRippleTextDx += tunaWidth * tunaRippleTextFractionDx;
        tunaRippleTextDy += tunaHeight * tunaRippleTextFractionDy;

        if (tunaRippleInnerCircleAngle != Integer.MAX_VALUE) {
            tunaRippleInnerCircleColorShader = getLinearGradient(tunaWidth, tunaHeight, tunaRippleInnerCircleAngle, tunaRippleInnerCircleColorGradientStart, tunaRippleInnerCircleColorGradientEnd);
        }

        if (tunaRippleOuterCircleAngle != Integer.MAX_VALUE) {
            tunaRippleOuterCircleColorShader = getLinearGradient(tunaWidth, tunaHeight, tunaRippleOuterCircleAngle, tunaRippleOuterCircleColorGradientStart, tunaRippleOuterCircleColorGradientEnd);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (tunaRippleInnerCircleRadius > 0) {
            // 后期把修改内容的判断加入到initTunaPaint中
            canvas.drawCircle(tunaWidth >> 1, tunaHeight >> 1, tunaRippleInnerCircleRadius,
                    initTunaPaint(Paint.Style.FILL, tunaRippleInnerCircleColor, tunaRippleInnerCircleColorShader));
        }

        // 255
        if (tunaRippleOuterCircleRadius > tunaRippleInnerCircleRadius && tunaRippleOuterCircleRadius < tunaRippleMaxRadius) {
            canvas.drawCircle(
                    tunaWidth >> 1,
                    tunaHeight >> 1,
                    tunaRippleOuterCircleRadius,
                    initTunaPaint(Paint.Style.FILL, tunaRippleOuterCircleColor, tunaRippleInnerCircleColorShader, (int) ((tunaRippleMaxRadius - tunaRippleOuterCircleRadius)
                            / tunaRippleDeltaRadius * 255)));
        }
        if (tunaRippleOuterDelayCircleRadius > tunaRippleInnerCircleRadius && tunaRippleOuterDelayCircleRadius < tunaRippleMaxRadius) {
            canvas.drawCircle(
                    tunaWidth >> 1,
                    tunaHeight >> 1,
                    tunaRippleOuterDelayCircleRadius,
                    initTunaPaint(Paint.Style.FILL, tunaRippleOuterCircleColor, tunaRippleOuterCircleColorShader, (int) ((tunaRippleMaxRadius - tunaRippleOuterDelayCircleRadius)
                            / tunaRippleDeltaRadius * 255)));
        }
        if (tunaRippleOuterDeferCircleRadius > tunaRippleInnerCircleRadius && tunaRippleOuterDeferCircleRadius < tunaRippleMaxRadius) {
            canvas.drawCircle(
                    tunaWidth >> 1,
                    tunaHeight >> 1,
                    tunaRippleOuterDeferCircleRadius,
                    initTunaPaint(Paint.Style.FILL, tunaRippleOuterCircleColor, tunaRippleOuterCircleColorShader, (int) ((tunaRippleMaxRadius - tunaRippleOuterDeferCircleRadius)
                            / tunaRippleDeltaRadius * 255)));
        }

        if (tunaRippleTextValue != null) {
            drawTunaText(canvas, tunaRippleTextValue, tunaWidth, (tunaWidth >> 1) + tunaRippleTextDx, (tunaHeight >> 1) + tunaRippleTextDy, 0, 0,
                    initTunaPaint(Paint.Style.FILL, tunaRippleTextColor, tunaRippleTextSize, Align.CENTER));
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void play() {
        tunaRippleAnimatorSet = new AnimatorSet();

        ObjectAnimator tunaRippleOuterCircleObjectAnimator = ObjectAnimator.ofFloat(this, tunaRippleAnimationCircleRadiusProperty, tunaRippleInnerCircleRadius,
                tunaRippleInnerCircleRadius + tunaRippleDeltaRadius * 3);

        tunaRippleOuterCircleObjectAnimator.setDuration(tunaRippleDuraction);
        tunaRippleAnimatorSet.playTogether(tunaRippleOuterCircleObjectAnimator);

        if (tunaRippleTimeInterpolator != null) {
            tunaRippleAnimatorSet.setInterpolator(tunaRippleTimeInterpolator);
        }
        tunaRippleAnimatorSet.start();
    }

}
