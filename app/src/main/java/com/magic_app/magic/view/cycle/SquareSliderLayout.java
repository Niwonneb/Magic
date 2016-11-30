package com.magic_app.magic.view.cycle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

import com.magic_app.magic.view.slider.SliderLayout;

public class SquareSliderLayout extends SliderLayout {
    public SquareSliderLayout(Context context) {
        super(context);
    }

    public SquareSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int squareLen = width > height ? height : width;
        setMeasuredDimension(squareLen, squareLen);
    }
}
