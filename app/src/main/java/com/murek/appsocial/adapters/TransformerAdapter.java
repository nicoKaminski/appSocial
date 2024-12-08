package com.murek.appsocial.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class TransformerAdapter implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setAlpha(0.5f + (scale - MIN_SCALE) / (1 - MIN_SCALE) * (1 - 0.5f));
    }
}
