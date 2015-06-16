package com.denny.android.idea_note;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.denny.android.idea_note.adapter.impl.NoteCursorAdapter;
import com.denny.android.idea_note.db.SQLHelper;
import com.denny.android.idea_note.decorator.DividerDecoration;
import com.denny.android.idea_note.domain.NotePreview;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView notes;
    NoteCursorAdapter adapter;
    DrawerLayout mDrawer;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initViews();
        setupCollapsingToolbarLayout();
    }

    private void setupCollapsingToolbarLayout(){

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle(toolbar.getTitle());
//            collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);  从ActionBar收缩过程中Title颜色的变化
//            collapsingToolbarLayout.setExpandedTitleColor(0xED1C24); 扩张到ActionBar过程中Title颜色的变化
        }
    }

    private void initViews() {

        FloatingActionButton fab= (FloatingActionButton) this.findViewById(R.id.actbut);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEdit();
            }
        });
        notes= (RecyclerView) findViewById(R.id.noteList);

        //准备RecyclerView的adapter
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        notes.setLayoutManager(layoutManager);
        adapter = getNoteAdapter();
        adapter.setHasStableIds(true);
        notes.setAdapter(adapter);
        notes.addItemDecoration(new DividerDecoration(this));
    }

    private NoteCursorAdapter getNoteAdapter() {
        Cursor s = getCursor();
        return new NoteCursorAdapter(this,s);
    }

    //获取指针
    private Cursor getCursor() {
        Log.e(MainActivity.class.getSimpleName(),"flush cursor!");
        return new SQLHelper(this).getReadableDatabase().query(false,
                NotePreview.NoteEntry.TABLE,
                new String[]{NotePreview.NoteEntry._ID,NotePreview.NoteEntry.CONTENT, NotePreview.NoteEntry.CREATED_TIME},
                "",new String[]{},"","","","");
    }

    private void initToolBar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void startEdit(){
        startActivityForResult(new Intent(this,EditActivity.class),0);
    }

    //返回结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(MainActivity.class.getSimpleName(), "Result");
        if(data!=null){//如过返回Intent并HAS_CHANGED为TRUE，则刷新Cursor
            boolean change = data.getBooleanExtra(EditActivity.HAS_CHANGED,false);
            if (change)
                adapter.changeCursor(getCursor());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if( item.getItemId() == android.R.id.home){
            Log.e(TAG,"Opne Menu");
            if (mDrawer.isDrawerOpen(GravityCompat.START)){
                mDrawer.closeDrawers();
            }else{
                mDrawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
