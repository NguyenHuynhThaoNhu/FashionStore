package com.fashionstore.fashion.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class DisableViewPager extends ViewPager {
    private boolean enabled;
    public DisableViewPager(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void setViewPagerEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
