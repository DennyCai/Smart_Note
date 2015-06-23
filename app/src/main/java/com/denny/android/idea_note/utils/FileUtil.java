package com.denny.android.idea_note.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by hasee on 2015/6/23.
 */
public final class FileUtil {
    private final static String SDFILENAME = "SmartNote";
    private final static String IMAGEPATH = "images";

    private final static String Tag = FileUtil.class.getSimpleName();

    public static File getSDPath(){
        File file = Environment.getExternalStorageDirectory();
        return file;
    }

    public static File getAppInSDPath(){
        String path = getSDPath().getAbsolutePath();
        Log.i(Tag,"getAppInSDPath:"+path);
        return new File(path);
    }

    public static boolean createFile(File file) throws IOException {
        if(file.exists())
            return true;
        else{
            return file.createNewFile();
        }
    }

}
