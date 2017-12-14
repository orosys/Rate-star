package com.orosys.dayrate.library;

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
    private float gap = 10;
    private int count = 5;
    private Point starSize;
    private float progress = 1;
    private float bgAlpha = 0.3f;

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

    public void setGap(float dp) {
        this.gap = dpFromPx(getContext(), dp);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBgAlpha(float alpha) {
        this.bgAlpha = alpha;
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
            result = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        }
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        Paint bgPaint = new Paint();
        bgPaint.setAlpha((int) (bgAlpha * 100));
        for (int i = 0; i < count; i++) {
            float x = (starSize.x + gap) * i;
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
        for (int i = 0; i < (count * 2) + 1; i++) {
            int x = (int) ((starSize.x * ((float) i / 2.0)) + (gap * (i / 2)));
            if ((getWidth() * progress) > x - (starSize.x / 4) - gap) {
                position = x;
            }
        }

        return position / getWidth();
    }

    public Point getStarSize() {
        Point size = new Point();
        float gapSize = gap * (count - 1);
        int width = (int) (getWidth() - gapSize);
        width /= count;
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