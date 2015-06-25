package com.denny.android.idea_note.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
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
        return new File(path+"/"+SDFILENAME);
    }

    public static File getImagesPath(){
        String path = getAppInSDPath().getAbsolutePath();
        Log.i(Tag,path);
        return new File(path+"/"+IMAGEPATH);
    }

    public static String getImageByName(String name){
        return getImagesPath().getAbsolutePath()+"/"+name;
    }

    public static File createImage(String name){
        File file = new File(getImagesPath().getAbsolutePath(),name);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean createFile(File file) throws IOException {
        if(file.exists())
            return true;
        else{
            return file.createNewFile();
        }
    }

    public static boolean createDir(String path){
        Log.e(FileUtil.class.getSimpleName(),"mkdir:"+path);
        return new File(path).mkdir();
    }

    public static int listFilesNameStart(String path, final String startName){
        return listFilesStart(path,startName).length;
    }

    public static File[] listFilesStart(String path, final String startName){
        return new File(path).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(startName);
            }
        });
    }

    public static Uri[] listFilesUriStart(String path,String name) {
        File[] files = listFilesStart(path,name);
        Uri[] uris = new Uri[files.length];
        for(int i = 0;i<files.length;i++){
            uris[i]=Uri.fromFile(files[i]);
        }
        return uris;
    }

    public static boolean rename(File file,String newName){
        return file.renameTo(new File(file.getParentFile().getAbsoluteFile(),newName));
    }

    public static void replaceName(File[] files,String old,String replace){
        for (File file:files){
            file.renameTo(new File(file.getParentFile().getAbsolutePath(),file.getName().replace(old,replace)));
        }
    }

    public static void delete(File[] fs){
        for (File file:fs){
            delete(file);
        }
    }

    public static boolean delete(File file){
        return file.delete();
    }
}
