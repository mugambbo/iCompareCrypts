package com.hellotractor.icomparecrypts.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Abdulmajid on 7/22/17.
 */

public class Fonts {
    public static Typeface titleFont(Context context) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
        return  fontFace;
    }

    public static Typeface subtitleFont(Context context) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        return  fontFace;
    }

    public static Typeface bodyFont(Context context) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        return  fontFace;
    }

    public static Typeface italicFont(Context context) {
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
        return  fontFace;
    }
}
