package com.denny.android.idea_note;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.denny.android.idea_note.dao.NoteDao;
import com.denny.android.idea_note.dao.impl.NoteDaoImpl;
import com.denny.android.idea_note.db.SQLHelper;
import com.denny.android.idea_note.domain.NotePreview;

import java.util.Date;

/**
 * Created by hasee on 2015/6/12.
 */
public class EditActivity extends AppCompatActivity {

    public final static String NOTE_ID = "note_id";

    Toolbar toolbar;
    FloatingActionButton fab;
    EditText editview;

    private NoteDao mDao;

    private boolean new_note ;
    private int mNote_id;

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
        mNote_id = getIntent().getIntExtra(NOTE_ID,-1);
        new_note = mNote_id ==-1;
    }

    private void initViews() {
        fab= (FloatingActionButton) findViewById(R.id.done);
        editview = (EditText) findViewById(R.id.note_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishEdit(true);
            }
        });
        if (!new_note){
            editview.setText(mDao.findNoteById(mNote_id).getContent());
            editview.setSelection(editview.getText().length());
        }
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
            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 完成编辑并退出
     * @param save 是否保存
     */
    private void finishEdit(boolean save) {
        setResult(0);
        if(save && saveNote()){
            this.supportFinishAfterTransition();
        }
    }

    /**
     * 保存Note
     * @return false：提示，true：写入数据库
     */
    private boolean saveNote() {
        String content = editview.getText().toString();
        if(null == content || "".equals(content)){
            Snackbar.make(findViewById(R.id.rootview),"请输入内容",Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        NotePreview note = new NotePreview();
        note.setContent(content);
        if(new_note) {
            mDao.save(note);
        }else{
            note.setId(mNote_id);
            mDao.update(note);
        }
        return true;
    }
}
