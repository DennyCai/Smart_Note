package com.denny.android.idea_note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AppCompatPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.denny.android.idea_note.dao.NoteDao;
import com.denny.android.idea_note.dao.impl.NoteDaoImpl;
import com.denny.android.idea_note.domain.NotePreview;
import com.denny.android.idea_note.utils.DateUtil;


/**
 * Created by hasee on 2015/6/12.
 */
public class EditActivity extends AppCompatActivity {

    public final static String NOTE_ID = "note_id";
    public static final String HAS_CHANGED = "note_changed";

    Toolbar toolbar;
    FloatingActionButton fab;
    EditText editview;
    TextView mUpdateTime;

    private NoteDao mDao;

    private boolean mCreate ;
    private long mNote_id;
    private PopupMenu mMorePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mDao = new NoteDaoImpl(this);
        setupStat();
        setupToolBar();
        initViews();
    }

    private void setupStat() {
        mNote_id = getIntent().getLongExtra(NOTE_ID, -1);
        mCreate = mNote_id ==-1;
    }

    private void initViews() {
        fab= (FloatingActionButton) findViewById(R.id.done);
        editview = (EditText) findViewById(R.id.note_edit);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreMenu();
            }
        });
        if (!mCreate){
            NotePreview notePreview = mDao.findNoteById(mNote_id);
            editview.setText(notePreview.getContent());
            editview.setSelection(editview.getText().length());
            String date = DateUtil.format(notePreview.getUpdateTime());
            mUpdateTime.setText(getString(R.string.update_time,date));
        }
    }




    private void showMoreMenu() {
        if (mMorePopup==null) {
            mMorePopup = new PopupMenu(this, fab, Gravity.NO_GRAVITY);
            mMorePopup.inflate(R.menu.more);
            mMorePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    return false;
                }
            });
        }
        mMorePopup.show();
    }


    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.editing);
        setSupportActionBar(toolbar);
        ActionBar bar=getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        dispathNote();
        super.onBackPressed();
    }

    private void dispathNote() {
        String content = editview.getText().toString();
        //如果为新建模式且内容未空就不保存
        if(mCreate && content.equals(""))
            return;

        NotePreview note = new NotePreview();
        if(mCreate){//新建
            note.setContent(content);
            mDao.save(note);
        }else{//更新操作
            note.setId(mNote_id);
            note.setContent(content);
            mDao.update(note);
        }
        setResult(0 ,new Intent().putExtra(HAS_CHANGED,true));
    }

    /**
     * 保存Note
     * @return false：提示，true：写入数据库
     */
    private boolean saveNote() {
        String content = editview.getText().toString();
        if(content ==null || "".equals(content)){
            Snackbar.make(findViewById(R.id.rootview),"请输入内容",Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        NotePreview note = new NotePreview();
        note.setContent(content);
        if(mCreate) {
            mDao.save(note);
        }else{
            note.setId(mNote_id);
            mDao.update(note);
        }
        return true;
    }
}
