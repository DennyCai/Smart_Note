package com.denny.android.idea_note;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
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
    NavigationView mNavi;
    FloatingActionButton fab;
    TextView mhintText;

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTION_UPDATE = "action.note.update";
    public static final String ACTION_ADD = "action.note.add";
    public static final String ACTION_DELETE = "action.note.delete";
    public static final String NOTE_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initViews();
        setupCollapsingToolbarLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        playAnimate();
        if(adapter.getCursor().getCount()==0){
            mhintText.setVisibility(View.VISIBLE);
        }else{
            mhintText.setVisibility(View.GONE);
        }
    }

    private void playAnimate() {

    }

    private void setupCollapsingToolbarLayout(){

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle(toolbar.getTitle());
//            collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);  从ActionBar收缩过程中Title颜色的变化
//            collapsingToolbarLayout.setExpandedTitleColor(0xED1C24); 扩张到ActionBar过程中Title颜色的变化
        }
    }

    //初始化控件
    private void initViews() {

        fab= (FloatingActionButton) this.findViewById(R.id.actbut);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mNavi = (NavigationView) findViewById(R.id.nav);
        mhintText = (TextView) findViewById(R.id.hint);

        mNavi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cloud:
                        startActivity(new Intent(MainActivity.this, UploadActivity.class));
                        return true;
                    case R.id.about:
                        showAboutDialog();
                        return true;
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEdit();
//                NotePreview note = new NotePreview();
//                note.setContent(System.currentTimeMillis()+"");
//                new NoteDaoImpl(getBaseContext()).save(note);
//                adapter.setCursor(getCursor());
//                adapter.notifyItemInserted(0);

            }
        });
        notes= (RecyclerView) findViewById(R.id.noteList);

        //准备RecyclerView的adapter
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        notes.setLayoutManager(layoutManager);
        adapter = getNoteAdapter();
        adapter.setHasStableIds(true);
        notes.setItemAnimator(new DefaultItemAnimator());
        notes.setAdapter(adapter);
        notes.addItemDecoration(new DividerDecoration(this));
    }

    private NoteCursorAdapter getNoteAdapter() {
        Cursor s = getCursor();
        return new NoteCursorAdapter(this,s);
    }

    //获取指针
    private Cursor getCursor() {
        Log.e(MainActivity.class.getSimpleName(), "flush cursor!");
        return new SQLHelper(this).getReadableDatabase().query(false,
                NotePreview.NoteEntry.TABLE,
                new String[]{NotePreview.NoteEntry._ID,NotePreview.NoteEntry.CONTENT, NotePreview.NoteEntry.CREATED_TIME},
                "_delete=?",new String[]{"0"},"","", NotePreview.NoteEntry.CREATED_TIME+" desc","");
    }

    private void initToolBar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void startEdit(){
        startActivityForResult(new Intent(this, EditActivity.class), 0);
    }

    //返回结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(MainActivity.class.getSimpleName(), "Result");
//        if(data!=null){//如过返回Intent并HAS_CHANGED为TRUE，则刷新Cursor
//            boolean change = data.getBooleanExtra(EditActivity.HAS_CHANGED,false);
//            if (change)
//                adapter.changeCursor(getCursor());
//        }
        handleResult(data);

    }

    private void handleResult(Intent data){
        if (data==null){
            return;
        }
        long id = -1;
        switch (data.getAction()){
            case ACTION_ADD:
                id = data.getLongExtra(NOTE_DATA,-1);
                if(id!=-1){
                    adapter.changeCursor(getCursor());
                }
                break;
            case ACTION_UPDATE:
                id = data.getLongExtra(NOTE_DATA,-1);
                if (id!=-1){
                    adapter.changeCursor(getCursor());
                }
                break;
            case ACTION_DELETE:
                adapter.changeCursor(getCursor());
                break;
            default:
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
        switch (item.getItemId()){
            case android.R.id.home:
                if (isDrawerOpne()){
                    mDrawer.closeDrawers();
                }else{
                    openDrawer();
                }
                break;
            case R.id.about:
                showAboutDialog();
                break;
            default:
                return false;
        }

        return true;
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this).setMessage("组员：\r\n122012002 蔡东\r\n122012006 陈俊汝\r\n 122012045 黄嘉林")
                .setTitle("关于本App")
                .setPositiveButton("OK",null)
                .show();
    }

    private void test() {
        Log.e(TAG,"test");
        new Baidu("rInbHcqX1KUopSW51GP1voqY",this).authorize(this,false,true, new BaiduDialog.BaiduDialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Log.e(TAG,"onComplete:"+bundle);
            }

            @Override
            public void onBaiduException(BaiduException e) {
                e.printStackTrace();
            }

            @Override
            public void onError(BaiduDialogError bde) {
                bde.printStackTrace();
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"onCancel");
            }
        });
    }

    private boolean isDrawerOpne(){
        return mDrawer.isDrawerOpen(GravityCompat.START);
    }

    private void openDrawer(){
        mDrawer.openDrawer(GravityCompat.START);
    }
}
