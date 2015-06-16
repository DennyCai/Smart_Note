package com.denny.android.idea_note.domain;

import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by hasee on 2015/6/12.
 */
public class NotePreview {

    private long id;
    private String content;
    private long createTime;
    private long updateTime;

    public NotePreview(){
        this(-1,"",0,0);
    }

    public NotePreview(int id, String content, long createTime, long updateTime) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public static abstract class NoteEntry{
        public static final String TABLE = "notes";
        public static final String _ID = "_id";
        public static final String CONTENT = "content";
        public static final String CREATED_TIME = "created_time";
        public static final String UPDATED_TIME = "updated_time";
        public static final String[] ALL = new String[]{_ID,CONTENT,CREATED_TIME,UPDATED_TIME};
    }
}
