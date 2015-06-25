package com.denny.android.idea_note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denny.android.idea_note.utils.FileUtil;

import java.io.File;


public class PhotoActivity extends AppCompatActivity {

    public static final String PHOTOS_PATH = "PHOTOS_PATH";
    public static final String NOTE_ID = "note_id";

    private ViewPager mPager;
    private TextView mPath;
    private String mId;
    private File[] photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mId = getIntent().getExtras().getString(NOTE_ID);
        photos = FileUtil.listFilesStart(FileUtil.getImagesPath().getAbsolutePath(),mId);
        initView();

    }

    private void initView() {
        mPager= (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PhotoFragment.newInstance(photos[position]);
            }

            @Override
            public int getCount() {
                return photos.length;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PhotoFragment extends Fragment{


        public static Fragment newInstance(File f){
            return new PhotoFragment(f);
        }

        private File photo;

        private PhotoFragment(File f){
            this.photo = f;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_photo,container,false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Log.e("Photo", photo.getAbsolutePath());

            ImageView imageView = (ImageView) view.findViewById(R.id.images);
            TextView mPath = (TextView) view.findViewById(R.id.path);
            mPath.setText(photo.getName());
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inSampleSize = 1;
            ops.inJustDecodeBounds =false;

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), ops);
            imageView.setImageBitmap(bitmap);
        }
    }
}
