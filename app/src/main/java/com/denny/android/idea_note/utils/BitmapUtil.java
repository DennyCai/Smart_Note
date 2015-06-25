package com.denny.android.idea_note.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Created by hasee on 2015/6/25.
 */
public final class BitmapUtil {
    public static Rect getRect(Bitmap bitmap){
        return new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
    }

    public static Bitmap decodeBitmap(String filePath,BitmapFactory.Options options){
        return BitmapFactory.decodeFile(filePath,options);
    }

    public static int getScale(DisplayMetrics metrics ,Bitmap bitmap){
        return bitmap.getScaledHeight(metrics);
    }


}
