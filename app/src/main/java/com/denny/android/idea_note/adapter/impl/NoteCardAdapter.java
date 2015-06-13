package com.denny.android.idea_note.adapter.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denny.android.idea_note.R;

/**
 * Created by hasee on 2015/6/13.
 */
public class NoteCardAdapter extends RecyclerView.Adapter<NoteCardAdapter.NoteCardViewHolder> {

    @Override
    public NoteCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false));
    }

    @Override
    public void onBindViewHolder(NoteCardViewHolder holder, int position) {
        holder.textview.setText("Position:"+position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public static class NoteCardViewHolder extends RecyclerView.ViewHolder{

        public TextView textview;

        public NoteCardViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
