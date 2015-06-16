package com.denny.android.idea_note.adapter.impl;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denny.android.idea_note.EditActivity;
import com.denny.android.idea_note.R;
import com.denny.android.idea_note.adapter.RecyclerCursorAdapter;
import com.denny.android.idea_note.domain.NotePreview;
import com.denny.android.idea_note.utils.DateUtil;

import java.util.Date;


/**
 * Created by hasee on 2015/6/13.
 */
public class NoteCursorAdapter extends RecyclerCursorAdapter<NoteCursorAdapter.ViewHolder> {


    public NoteCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView contenttext;
        public TextView time;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            root=itemView;
            root.setOnClickListener(this);
//            root.setOnLongClickListener(this);
            contenttext = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        @Override
        public void onClick(View v) {
            Log.e("Adapter","Item Id:"+getItemId());
            Intent intent = new Intent(v.getContext(),EditActivity.class);
            intent.putExtra(EditActivity.NOTE_ID, getItemId());
            ((AppCompatActivity)v.getContext()).startActivityForResult(intent, 0);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        String content = cursor.getString(cursor.getColumnIndex(NotePreview.NoteEntry.CONTENT));
        long created_time = cursor.getLong(cursor.getColumnIndex(NotePreview.NoteEntry.CREATED_TIME));
        holder.time.setText(DateUtil.format(created_time));
        holder.contenttext.setText(content);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);
        ViewHolder vh =new ViewHolder(itemview);
        return vh;
    }


}
