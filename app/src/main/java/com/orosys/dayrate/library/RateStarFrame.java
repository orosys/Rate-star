package com.orosys.dayrate.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.orosys.dayrate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oro on 2017. 11. 29..
 */

public class RateStarFrame extends LinearLayout {
    private static final String TAG = RateStarFrame.class.getSimpleName();
    private boolean isInit = false;
    private float lastProgress = -10;
    private List<LottieAnimationView> starList;

    public RateStarFrame(Context context) {
        super(context);
    }

    public RateStarFrame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RateStarFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        int width = getWidth() / 5;
        starList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LottieAnimationView lottieAnimationView = new LottieAnimationView(getContext());
            lottieAnimationView.setLayoutParams(new ViewGroup.LayoutParams(width, getHeight()));
            lottieAnimationView.setAnimation(R.raw.favorite_star);
            lottieAnimationView.pauseAnimation();
            starList.add(lottieAnimationView);
            addView(lottieAnimationView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                lastProgress = -10;
                return true;
            case (MotionEvent.ACTION_MOVE):
            case (MotionEvent.ACTION_UP):
                float current = event.getX() / (float) getWidth();
                if (Math.abs(current - lastProgress) <= 0.1f) {
                    return true;
                }
                lastProgress = current;
                for (int i = 0; i < starList.size(); i++) {
                    float listCurrent = (float) i / (float) starList.size();
                    if (current >= listCurrent) {
                        if (starList.get(i).getFrame() == 0) {
                            starList.get(i).playAnimation();
                        }
                    } else {
                        if (starList.get(i).getFrame() != 0) {
                            starList.get(i).pauseAnimation();
                            starList.get(i).setProgress(0);
                        }
                    }
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
}
