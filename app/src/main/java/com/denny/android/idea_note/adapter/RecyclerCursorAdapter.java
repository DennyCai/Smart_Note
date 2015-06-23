package com.denny.android.idea_note.adapter;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

/**
 * Created by hasee on 2015/6/12.
 */
public abstract class RecyclerCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    private Context mContext;

    private Cursor mCursor;

    private DataSetObserver mDataSetObserver;

    private boolean mDataValid;

    private int mRowIdColumn;


    public RecyclerCursorAdapter(Context context, Cursor cursor){
        mContext = context ;
        mCursor = cursor ;
        mDataValid = cursor!=null;
        mRowIdColumn = mDataValid ? cursor.getColumnIndex("_id") : -1 ;
        mDataSetObserver = new NotifyingDataSetObserver();
        if(cursor!=null){
            cursor.registerDataSetObserver(mDataSetObserver);//注册Data观察者
        }
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }


    public abstract void onBindViewHolder(VH holder,Cursor cursor);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public int getItemCount() {
        if(mDataValid && mCursor!=null){
            return mCursor.getCount();
        }else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        if(mDataValid && mCursor!=null && mCursor.moveToPosition(position) ){
            return mCursor.getLong(mRowIdColumn);
        }else
            return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    /*
    改变当前Cursor指针对象
     */
    public void changeCursor(Cursor cursor){
        Cursor old = swapCursor(cursor);
        if (old!=null){
            old.close();
        }
    }




    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {//指针相同不更换
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {//注销旧指针的Data观察者
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {//注册新的观察者
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
//            notifyItemRangeChanged(0,newCursor.getCount());
        } else {
            //指针没有内容
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private int[] compareCursor(Cursor oldCursor,Cursor newCursor){
        int desc = newCursor.getCount() - oldCursor.getCount();
        List<Integer> list =new ArrayList<Integer>();

        if (desc>0){
            for (int i=0;i<desc;i++)
                list.add(i);
        }else{

        }

        return new int[]{};
    }

    public class NotifyingDataSetObserver extends DataSetObserver{


        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            Log.e("Adapter","onChanged");
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            Log.e("Adapter","onInvalidated");

        }
    }
}
