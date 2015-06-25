package com.denny.android.idea_note.application;

import android.app.Application;
import android.util.Log;

import com.denny.android.idea_note.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by hasee on 2015/6/25.
 */
public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFilesInSD();
    }

    private void initFilesInSD() {
        File appsd = FileUtil.getAppInSDPath();
        Log.e("Path",appsd.getAbsolutePath());
        if(!appsd.exists()) {
            FileUtil.createDir(appsd.getAbsolutePath());
        }
        File imagespath = FileUtil.getImagesPath();
        Log.e("Path",imagespath.getAbsolutePath());
        if(!imagespath.exists()){
            FileUtil.createDir(imagespath.getAbsolutePath());
        }
    }
}
