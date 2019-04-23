package com.wifyee.greenfields.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

/**
 * Created by amit on 12/23/2017.
 */

@SuppressLint("AppCompatCustomView")
public class CustomAutoCompleteTextView  extends AutoCompleteTextView
{

        public CustomAutoCompleteTextView(Context context) {
                 super(context);
    // TODO Auto-generated constructor stub
}

        public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }

        // this is how to disable AutoCompleteTextView filter
        @Override
        protected void performFiltering(final CharSequence text, final int keyCode) {
            String filterText = "";
            super.performFiltering(filterText, keyCode);
        }
}
