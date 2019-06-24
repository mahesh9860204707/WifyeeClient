package com.wifyee.greenfields.Utils;

import android.content.Context;
import android.graphics.Typeface;


public class Fonts {
    public static Typeface getEstre(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/estre.ttf");
    }

    public static Typeface getFutura(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Book-BT.ttf");
    }

    public static Typeface getSemiBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Semibold.ttf");
    }

    public static Typeface getBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Bold.ttf");
    }

    public static Typeface getLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Light.ttf");
    }


    public static Typeface getRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Regular.ttf");
    }
}
