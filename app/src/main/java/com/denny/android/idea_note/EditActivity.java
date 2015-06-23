package com.denny.android.idea_note;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AppCompatPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.denny.android.idea_note.dao.NoteDao;
import com.denny.android.idea_note.dao.impl.NoteDaoImpl;
import com.denny.android.idea_note.domain.NotePreview;
import com.denny.android.idea_note.utils.DateUtil;

import java.util.Calendar;


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
            long time = notePreview.getUpdateTime();
            if (time!=0) {
                String date = DateUtil.format(notePreview.getUpdateTime());
                mUpdateTime.setText(getString(R.string.update_time, date));
            }else{
                mUpdateTime.setVisibility(View.GONE);
            }
        }
    }




    private void showMoreMenu() {
        if (mMorePopup==null) {
            mMorePopup = new PopupMenu(this, fab, Gravity.NO_GRAVITY);
            mMorePopup.inflate(R.menu.more);
            Menu menu = mMorePopup.getMenu();
            if (mCreate){
                menu.findItem(R.id.del_note).setVisible(false);
            }
            mMorePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.del_note:
                            deleteNote();
                            break;
                        case R.id.add_alert:
                            showPickerDialog();
                            break;
                        case R.id.add_photo:
                            addPhoto();
                            break;
                    }
                    return false;
                }
            });
        }
        mMorePopup.show();
    }

    //启动相机
    private void addPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    private void showPickerDialog() {

    }

    private void deleteNote(){
        if(mDao.deleteById(mNote_id)){
            Intent data = makeIntent(MainActivity.ACTION_DELETE,mNote_id);
            setResult(0,data);
           this.supportFinishAfterTransition();
        }else{
            Snackbar.make(findViewById(R.id.rootview),"删除失败",Snackbar.LENGTH_SHORT)
                    .show();
        }
    }


    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mCreate)
            toolbar.setTitle(R.string.add);
        else
            toolbar.setTitle(R.string.edit);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        dispathNote();
        super.onBackPressed();
    }

    private void dispathNote() {
        String content = editview.getText().toString();
        Intent intent;
        //如果为新建模式且内容未空就不保存
        if(mCreate && content.equals(""))
            return;

        NotePreview note = new NotePreview();
        if(mCreate){//新建
            note.setContent(content);
            long id = mDao.save(note);
            intent= makeIntent(MainActivity.ACTION_ADD,id);
        }else{//更新操作
            note.setId(mNote_id);
            note.setContent(content);
            updateNote(note);
            intent = makeIntent(MainActivity.ACTION_UPDATE,mNote_id);
        }

//        Intent intent = makeIntent();
        setResult(0, intent);
    }

    private void updateNote(NotePreview note) {
        NotePreview old = mDao.findNoteById(note.getId());
        if(old.getContent().equals(note.getContent()))
            return;
        mDao.update(note);
    }

    private Intent makeIntent(String action,long id) {
        Intent intent =new Intent();
        intent.setAction(action);
        intent.putExtra(MainActivity.NOTE_DATA,id);
        return intent;
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
