package com.wifyee.greenfields.Utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sumanta on 12/30/16.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean mAcceptEvents = false;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public void setAcceptEvents(boolean mAcceptEvents) {
        this.mAcceptEvents = mAcceptEvents;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mAcceptEvents ? super.onInterceptTouchEvent(ev) : true;
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAcceptEvents = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAcceptEvents = false;
    }
}