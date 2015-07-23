package com.denny.android.idea_note;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.DrawableUtils;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.denny.android.idea_note.dao.NoteDao;
import com.denny.android.idea_note.dao.impl.NoteDaoImpl;
import com.denny.android.idea_note.domain.NotePreview;
import com.denny.android.idea_note.recevier.NoteAlarmRecevier;
import com.denny.android.idea_note.utils.DateUtil;
import com.denny.android.idea_note.utils.DrawableUtil;
import com.denny.android.idea_note.utils.FileUtil;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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
    TextView alarm;
    TextView images;

    private NoteDao mDao;

    private boolean mCreate ;
    private long mNote_id;
    private PopupMenu mMorePopup;

    private Calendar alarmTime;

    private NotePreview mNote;

    private List<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mDao = new NoteDaoImpl(this);
        setupStat();
        setupToolBar();
//        initMore();
        initViews();
    }

    private void initMore() {
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
                   /* case R.id.del_alert:
                        deleteAlert();
                        break;
                    case R.id.upd_alert:
                        updateAlert();
                        break;*/
                }
                return false;
            }
        });
    }

    private void setupStat() {
        mNote_id = getIntent().getLongExtra(NOTE_ID, -1);
        mCreate = mNote_id ==-1;
        if(!mCreate)
            mNote = mDao.findNoteById(mNote_id);

        files = new ArrayList<>();
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

    private void initViews() {
        fab= (FloatingActionButton) findViewById(R.id.done);
        editview = (EditText) findViewById(R.id.note_edit);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        alarm= (TextView) findViewById(R.id.alarm);
        images = (TextView) findViewById(R.id.images);
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(images.getText().toString().equals("0"))
                    return;
                Intent intent = new Intent(EditActivity.this,PhotoActivity.class);
                String name = null;
                if(mCreate)
                    name = "new";
                else
                    name = mNote_id+"";
                intent.putExtra(PhotoActivity.NOTE_ID,
                        name);
                startActivity(intent);
            }
        });
        showImagesNumber();

        //若Note不是新建并且有设置提醒
        if(!mCreate && mNote.getAlarmTime()!=-1){
            showAlarmText(DateUtil.format(mNote.getAlarmTime(), "MM/dd/HH:mm"));
            alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(mNote.getAlarmTime());
        }

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

    //显示图片数量
    private void showImagesNumber() {
        if(images.getCompoundDrawables()[0]==null)
        images.setCompoundDrawables(
                DrawableUtil.getDrawable(this,android.R.drawable.ic_menu_gallery,R.color.my_primary),
                null,null,null);
        if(mCreate)
            images.setText("0");
        else{
            images.setText(getImagesNumber(mNote_id+"")+"");
        }
    }

    private int getImagesNumber(String mNote_id) {
        return FileUtil.listFilesNameStart(FileUtil.getImagesPath().getAbsolutePath(),mNote_id);
    }


    private void showAlarmText(String time) {

        if(alarm.getCompoundDrawables()[0]==null){
            Log.i("", "CreateDrawable");
            alarm.setCompoundDrawables(getAlarmDrawable(),null,null,null);
        }
        alarm.setText(time);
    }

    private  void removeAlarmText(){
        alarm.setCompoundDrawables(null,null,null,null);
        alarm.setText("");
        alarmTime=null;
    }


    //更多功能按钮
    private void showMoreMenu() {
        if(mMorePopup==null){
            initMore();
        }
        mMorePopup.show();
    }

    private void updateAlert() {
        showPickerDialog(alarmTime);
    }

    private void deleteAlert() {
        removeAlarmText();

    }

    //启动相机
    private void addPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String last = System.currentTimeMillis() + ".jpg";
        String path= FileUtil.getImageByName(
                mCreate ? "new-" + last : mNote_id + "-" + last);
        Log.e("photo",path);
//        try {
//            new File(path).createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        startActivityForResult(intent, 0);
    }

    //显示TimePicker
    private void showPickerDialog() {
        showPickerDialog(Calendar.getInstance());
    }

    private void showPickerDialog(Calendar c){
//        Calendar c = Calendar.getInstance();
        TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minutes) {
                Log.e("TimePicker", "Hour:" + hourOfDay + ":" + minutes);
                addAlert(hourOfDay,minutes);
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show(getFragmentManager(), null);
        //*/
    }

    //添加提醒
    private void addAlert(int hour,int min){
        Calendar c = Calendar.getInstance();
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMin = c.get(Calendar.MINUTE);


        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        if(hour<nowHour){
            c.add(Calendar.DATE,1);
        }else if(hour==nowHour){
            if(min<=nowMin){
                c.add(Calendar.DATE,1);
            }
        }
        String time = c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        Log.e("Alert", "time:" + time);
        alarmTime = c;

        showAlarmText(DateUtil.format(c.getTimeInMillis(), "MM/dd/HH:mm"));

    }

    //生成图标
    private Drawable getAlarmDrawable(){
        Drawable left = getResources().getDrawable(android.R.drawable.ic_lock_idle_alarm);
        // PorterDuff.Mode.SRC_ATOP 相交图像变色
        left.setColorFilter(getResources().getColor(R.color.my_primary), PorterDuff.Mode.SRC_ATOP);

        left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        return left;
    }

    /**
     * 设置闹钟
     * @param mil :提示的时间戳
     * @param noteId
     */
    private void setAlert(long mil,long noteId){
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,0,new Intent("action.alarm.note").setClass(this, NoteAlarmRecevier.class),0);
        alarm.set(AlarmManager.RTC_WAKEUP, mil, pendingIntent);
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
//        super.onActivityResult(requestCode, resultCode, data);
        if(mCreate)
            images.setText(getImagesNumber("new")+"");
        else
            images.setText(getImagesNumber(mNote_id+"")+"");
    }

    @Override
    public void onBackPressed() {
        dispathNote();
        super.onBackPressed();
    }

    private void dispathNote() {
        String content = editview.getText().toString();
        long newId=-1;
        Intent intent;
        //如果为新建模式且内容未空就不保存
        if(mCreate && content.equals("")){
            FileUtil.delete(FileUtil.listFilesStart(FileUtil.getImagesPath().getAbsolutePath(),"new"));
            return;
        }

        NotePreview note = new NotePreview();

        //是否设置闹钟
        if(alarmTime!=null){
            note.setAlarmTime(alarmTime.getTimeInMillis());
        }else{
            note.setAlarmTime(-1);
        }

        if(mCreate){//新建
            note.setContent(content);
            newId = mDao.save(note);
            renameFiles(FileUtil.listFilesStart(FileUtil.getImagesPath().getAbsolutePath(),"new"),newId+"");
            intent= makeIntent(MainActivity.ACTION_ADD, newId);
        }else{//更新操作
            note.setId(mNote_id);
            note.setContent(content);
            updateNote(note);
            intent = makeIntent(MainActivity.ACTION_UPDATE,mNote_id);
        }


        if(note.getAlarmTime()!=-1)
            setAlert(note.getAlarmTime(),mCreate ? newId : note.getId() );


//        Intent intent = makeIntent();
        setResult(0, intent);
    }

    //将"new-21312341.jpg"改名为"123-123124.jpg"
    private void renameFiles(File[] files,String newName) {
        FileUtil.replaceName(files,"new",newName);
    }

    //更行Note
    private void updateNote(NotePreview note) {
        NotePreview old = mDao.findNoteById(note.getId());
        if(old.getContent().equals(note.getContent())&&old.getAlarmTime()==note.getAlarmTime())
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
