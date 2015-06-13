package com.denny.android.idea_note.dao;

import com.denny.android.idea_note.domain.NotePreview;

/**
 * Created by hasee on 2015/6/14.
 */
public interface NoteDao {
    NotePreview findNoteById(int id);
    boolean save(NotePreview note);
    boolean update(NotePreview note);
}
