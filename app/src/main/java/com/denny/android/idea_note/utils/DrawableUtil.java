package com.denny.android.idea_note.utils;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * Created by hasee on 2015/6/24.
 */
public final class DrawableUtil {
    public static Drawable getDrawable(Context context,int id){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        return drawable;
    }

    public static Drawable getDrawable(Context context, int id,int colorId){
        Drawable drawable = getDrawable(context,id);
        drawable.setColorFilter(context.getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }
}
