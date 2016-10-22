package com.example.beloo.circlerectbitmapanimator.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.beloo.circlerectbitmapanimator.R;

public class CircleRectView extends ImageView {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private int circleRadius;
    private float cornerRadius;
    private float eps = 0.001F;

    private RectF bitmapRect;
    private Path clipPath;

    public CircleRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleRectView, 0, 0);
        init(a);
    }

    public CircleRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleRectView, defStyleAttr, 0);
        init(a);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleRectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleRectView, defStyleAttr, defStyleRes);
        init(a);
    }

    private void init(TypedArray a) {
        if (a.hasValue(R.styleable.CircleRectView_circleRadius)) {
            circleRadius = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0);
            cornerRadius = circleRadius;
        }

        clipPath = new Path();

        a.recycle();
    }

    public Animator animator(int rectHeight, int rectWidth) {
        return animator(getMeasuredHeight(), getMeasuredWidth(), rectHeight, rectWidth);
    }

    public Animator animator(int startHeight, int startWidth, int endHeight, int endWidth) {
        AnimatorSet animatorSet = new AnimatorSet();

//        Log.d(CircleRectView.class.getSimpleName(), "startHeight =" + startHeight
//                + ", startWidth =" + startWidth
//                + ", endHeight = " + endHeight
//                + " endWidth =" + endWidth);

        ValueAnimator heightAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        ValueAnimator widthAnimator = ValueAnimator.ofInt(startWidth, endWidth);

        heightAnimator.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = val;

//            Log.d(CircleRectView.class.getSimpleName(), "height updated =" + val);

            setLayoutParams(layoutParams);
            requestLayoutSupport();
        });

        widthAnimator.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = val;

            setLayoutParams(layoutParams);
            requestLayoutSupport();
        });

        ValueAnimator radiusAnimator;
        if (startWidth < endWidth) {
            radiusAnimator = ValueAnimator.ofFloat(circleRadius, 0);
        } else {
            radiusAnimator = ValueAnimator.ofFloat(0, circleRadius);
        }

        radiusAnimator.setInterpolator(new AccelerateInterpolator());
        radiusAnimator.addUpdateListener(animator -> cornerRadius = (float) (Float) animator.getAnimatedValue());

        animatorSet.playTogether(heightAnimator, widthAnimator, radiusAnimator);

        return animatorSet;
    }

    /**
     * this needed because of that somehow {@link #onSizeChanged} NOT CALLED when requestLayout while activity transition end is running
     */
    private void requestLayoutSupport() {
        View parent = (View) getParent();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.EXACTLY);
        parent.measure(widthSpec, heightSpec);
        parent.layout(parent.getLeft(), parent.getTop(), parent.getRight(), parent.getBottom());
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.

//        Log.d("size changed", "w = " + w + " h = " + h);

        bitmapRect = new RectF(0, 0, w, h);
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        clipPath.reset();
        if (Math.abs(cornerRadius + cornerRadius - bitmapRect.height()) < eps) {
            clipPath.addCircle(bitmapRect.centerX(), bitmapRect.centerY(), cornerRadius, Path.Direction.CW);
        } else {
            clipPath.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW);
        }

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

}
