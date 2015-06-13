package com.denny.android.idea_note.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.denny.android.idea_note.dao.NoteDao;
import com.denny.android.idea_note.db.SQLHelper;
import com.denny.android.idea_note.domain.NotePreview;
import com.denny.android.idea_note.domain.factory.NoteFactory;

import java.util.Date;

/**
 * Created by hasee on 2015/6/14.
 */
public class NoteDaoImpl implements NoteDao{

    private SQLHelper mHelper;

    public NoteDaoImpl(Context context){
        mHelper = new SQLHelper(context);
    }

    @Override
    public NotePreview findNoteById(int id) {
        NotePreview note= null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c =db.query(false,
                    NotePreview.NoteEntry.TABLE,
                    NotePreview.NoteEntry.ALL,
                    "_id = ?",
                    new String[]{id + ""}, "", "", "", "");
        if(c.moveToNext()){
           note = NoteFactory.createFromCursor(c);
        }
        db.close();
        return note;
    }

    @Override
    public boolean save(NotePreview note) {
        long id = -1;
        if (note!=null){
            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NotePreview.NoteEntry.CONTENT,note.getContent());
            values.put(NotePreview.NoteEntry.CREATED_TIME,new Date().getTime());
            id = mHelper.getWritableDatabase().insert(NotePreview.NoteEntry.TABLE,"",values);
        }
        return id!=-1;
    }

    @Override
    public boolean update(NotePreview note) throws RuntimeException{
        long eff;
        if(note.getId()==-1){
            throw new RuntimeException("NotePreview 没有id!");
        }else{
            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NotePreview.NoteEntry.CONTENT,note.getContent());
            values.put(NotePreview.NoteEntry.UPDATED_TIME,new Date().getTime());
            eff = db.update(NotePreview.NoteEntry.TABLE,values,"_id = ?",new String[]{note.getId()+""});
            db.close();
            return eff!=0;
        }

    }
}
