package com.orosys.dayrate.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by oro on 2017. 12. 11..
 */

public class RateStar extends View {
    private static final String TAG = RateStarFrame.class.getSimpleName();
    private boolean isInit = false;
    private Bitmap starBitmap = null;
    private float space = 10;
    private int rateCount = 5;
    private Point starSize;
    private float progress = 1;
    private float bgAlpha = 0.3f;
    private boolean isEnableHalf = true;

    public RateStar(Context context) {
        super(context);
    }

    public RateStar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RateStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStarImage(int res) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        setStarImage(drawable);
    }

    public void setStarImage(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                setStarImage(bitmapDrawable.getBitmap());
            }
        }
    }

    public void setStarImage(Bitmap bitmap) {
        starBitmap = bitmap;
        if (starSize != null) {
            starBitmap = Bitmap.createScaledBitmap(starBitmap, starSize.x, starSize.y, true);
        }
    }

    public void setSpace(float dp) {
        this.space = dpFromPx(getContext(), dp);
    }

    public void setRateCount(int count) {
        this.rateCount = count;
    }

    public void setBgAlpha(float alpha) {
        this.bgAlpha = alpha;
    }

    public void setRateStar(float rate) {
        progress = rate / this.rateCount;
        invalidate();
    }

    public float getRateStar() {
        return this.rateCount * progress;
    }

    public void setEnableHalf(boolean enableHalf) {
        isEnableHalf = enableHalf;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initView();
    }

    private void initView() {
        if (isInit) {
            return;
        }
        isInit = true;

        starSize = getStarSize();
        if (starBitmap != null) {
            starBitmap = Bitmap.createScaledBitmap(starBitmap, starSize.x, starSize.y, true);
        }
        setEnabled(true);
        setClickable(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (starBitmap == null) {
            super.onDraw(canvas);
            return;
        }
        starSize = getStarSize();

        int y = getHeight() / 2;
        y -= starSize.y / 2;

        Rect rect = new Rect();
        rect.set(0, y, (int) (((float) getWidth()) * progress), starSize.y + y);

        Bitmap result = null;
        if (rect.width() == 0) {
            result = Bitmap.createBitmap(1, rect.height(), Bitmap.Config.ARGB_8888);
        } else {
            result = Bitmap.createBitmap(rect.width() < 1 ? 1 : rect.width(), rect.height() < 1 ? 1 : rect.height(), Bitmap.Config.ARGB_8888);
        }
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Paint bgPaint = new Paint();
        bgPaint.setAlpha((int) (bgAlpha * 100));
        for (int i = 0; i < rateCount; i++) {
            float x = (starSize.x + space) * i;
            RectF rectF = new RectF(x, y, starSize.x + x, starSize.y + y);
            canvas.drawBitmap(starBitmap, null, rectF, bgPaint);

            if (rect.width() > 0) {
                tempCanvas.drawBitmap(starBitmap, x, 0, null);
            }
        }

        tempCanvas.drawBitmap(result, rect.left, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(result, 0, y, new Paint());

        super.onDraw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
            case (MotionEvent.ACTION_MOVE):
                progress = event.getX() / (float) getWidth();
                invalidate();
                return true;
            case (MotionEvent.ACTION_UP):
                progress = updateStarPosition(progress);
                invalidate();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    private float updateStarPosition(float progress) {
        float position = 0;
        int count = this.rateCount;
        if (isEnableHalf) {
            count = count * 2;
        }
        for (int i = 0; i < count + 1; i++) {
            int x = (int) ((starSize.x * ((float) (isEnableHalf ? i / 2.0 : i))) + (space * (isEnableHalf ? (i / 2) : i)));
            int offset = 0;
            if (isEnableHalf) {
                if (i % 2 == 0) {
                    offset = (int) space;
                }
            }
            if ((getWidth() * progress) > x - (starSize.x / (isEnableHalf ? 4 : 2)) - offset) {
                position = x;
            }
        }

        return position / getWidth();
    }

    public Point getStarSize() {
        Point size = new Point();
        float gapSize = space * (rateCount - 1);
        int width = (int) (getWidth() - gapSize);
        width /= rateCount;
        size.set(width, width);
        if (width > getHeight()) {
            size.set(getHeight(), getHeight());
        }

        return size;
    }

    private float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}