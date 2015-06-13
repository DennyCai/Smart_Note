package com.denny.android.idea_note.domain.factory;

import android.database.Cursor;

import com.denny.android.idea_note.domain.NotePreview;

/**
 * Created by hasee on 2015/6/14.
 */
public class NoteFactory {
    public static NotePreview createFromCursor(Cursor c){
        NotePreview notePreview = null;
        if (c!=null &&c.getCount()!=0){
            notePreview=new NotePreview(c.getInt(c.getColumnIndex(NotePreview.NoteEntry._ID)),
                    c.getString(c.getColumnIndex(NotePreview.NoteEntry.CONTENT)),
                    c.getLong(c.getColumnIndex(NotePreview.NoteEntry.CREATED_TIME)),
                    c.getLong(c.getColumnIndex(NotePreview.NoteEntry.UPDATED_TIME)));
        }
        c.close();
        return notePreview;
    }
}
