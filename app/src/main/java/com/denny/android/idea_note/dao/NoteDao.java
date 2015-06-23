package com.denny.android.idea_note.dao;

import android.content.ContentValues;

import com.denny.android.idea_note.domain.NotePreview;

/**
 * Created by hasee on 2015/6/14.
 */
public interface NoteDao {
    NotePreview findNoteById(long id);

    long save(NotePreview note);

    boolean update(NotePreview note);

    boolean update(NotePreview note,ContentValues values);

    boolean delete(NotePreview note);

    boolean deleteById(long id);
}
