package com.denny.android.idea_note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.denny.android.idea_note.domain.NotePreview;

/**
 * Created by hasee on 2015/6/13.
 */
public class SQLHelper extends SQLiteOpenHelper {

    public static final int VERSION = 2;

    public static final String PRIMARY_KEY = " primary key";
    public static final String INCREMENT = " autoincrement";
    public static final String INT = " integer";
    public static final String TEXT = " text";
    public static final String BOOL = " boolean";
    public static final String NOT_NULL= " not null ";


    public SQLHelper(Context context) {
        super(context, "note.db", null, VERSION);
    }

    public SQLHelper(Context context,SQLiteDatabase.CursorFactory cursorFactory){
        super(context, "note.db" , cursorFactory,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NotePreview.NoteEntry.TABLE + "(" +
                NotePreview.NoteEntry._ID + INT + PRIMARY_KEY + INCREMENT + "," +
                NotePreview.NoteEntry.CONTENT + TEXT + "," +
                NotePreview.NoteEntry.CREATED_TIME + VARCHAR(20) + "," +
                NotePreview.NoteEntry.UPDATED_TIME + VARCHAR(20) + "," +
                NotePreview.NoteEntry.ALARM_TIME + VARCHAR(20) + "," +
                NotePreview.NoteEntry._DELETE + BOOL + NOT_NULL + DEFAULT(0) +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table "+ NotePreview.NoteEntry.TABLE);
        onCreate(db);
    }

    private final String VARCHAR(int len){
        return " varchar("+ len +")";
    }
    private final String DEFAULT(String val){
        return " default("+ val +") ";
    }
    private final String DEFAULT(int val){
        return DEFAULT(val+"");
    }
}
